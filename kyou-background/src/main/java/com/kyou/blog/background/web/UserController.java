package com.kyou.blog.background.web;


import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kyou.blog.background.properties.CCProperties;
import com.kyou.blog.background.security.SysUser;
import com.kyou.blog.background.webUtil.AuthenticationContextHolder;
import com.kyou.blog.background.webUtil.RedisUtil;
import com.kyou.blog.background.webUtil.ThreadService;
import com.kyou.blog.background.webUtil.WebUtil;
import com.kyou.blog.common.Result;
import com.kyou.blog.common.annotation.Log;
import com.kyou.blog.common.constant.MsgConstant;
import com.kyou.blog.common.constant.RedisConstant;
import com.kyou.blog.common.constant.RoleConstant;
import com.kyou.blog.common.constant.StatusConstant;
import com.kyou.blog.common.emuration.OperationType;
import com.kyou.blog.common.util.MinioUtil;
import com.kyou.blog.common.util.SysContext;
import com.kyou.blog.common.util.VerifyCodeUtil;
import com.kyou.blog.dataService.service.*;
import com.kyou.blog.model.dto.PageUserDto;
import com.kyou.blog.model.dto.UserDto;
import com.kyou.blog.model.dto.UserPwdDto;
import com.kyou.blog.model.entity.*;
import com.kyou.blog.model.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
@Api(tags = "用户相关接口")
@RestController
@Slf4j
@RequestMapping("/sys/user")
public class UserController {
    @DubboReference(interfaceClass = UserService.class)
    private UserService userService;
    @DubboReference(interfaceClass = RoleService.class)
    private RoleService roleService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private MinioUtil minioUtil;
    @Autowired
    private CCProperties ccProperties;
    @Resource
    private AuthenticationManager authenticationManager;
    @DubboReference(interfaceClass = UserFollowService.class)
    private UserFollowService userFollowService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @DubboReference(interfaceClass = UserCommentService.class)
    private UserCommentService userCommentService;
    @DubboReference(interfaceClass = ArticleService.class)
    private ArticleService articleService;
    @DubboReference(interfaceClass = CommentsService.class)
    private CommentsService commentsService;
    @Autowired
    private ThreadService threadService;
    @Autowired
    private AuthenticationSuccessHandler successHandler;
    @Autowired
    private AuthenticationFailureHandler failureHandler;
    /**
     *
     * @param k 游客id
     * @param response
     * @return
     */
    @ApiOperation("获取登录验证码")
    @GetMapping("/loginCode")
    public Result<CodeVo> loginCode(String k, HttpServletResponse response)
    {
        if (!StringUtils.hasText(k)) {
            //游客id
            k=WebUtil.uuid(false);
        }
        String vo = redisUtil.getVal(RedisConstant.LOGIN_CODE + k);
        if (StringUtils.hasText(vo)) {
            CodeVo codeVo = JSONUtil.toBean(vo, CodeVo.class);
            return Result.success(codeVo);
        }
        CodeVo codeVo = VerifyCodeUtil.handlerCode(k, 4, response);
        log.info("生成的验证码:-->{}",codeVo.getCode());
        redisUtil.setVal(RedisConstant.LOGIN_CODE+k,
                codeVo,Duration.ofMinutes(RedisConstant.LOGIN_CODE_TTL));
        return Result.success(codeVo);
    }
    @ApiOperation("用户注册")
    @PostMapping("/register")
    @Transactional
    @Log(value = OperationType.INSERT,title = "用户模块")
    public Result register(@RequestBody @Valid LoginVo registerVo,
                           HttpServletRequest request)
    {
        if (!StringUtils.hasText(registerVo.getEmail())) {
            throw new RuntimeException(MsgConstant.EMAIL_PATTERN_ERR);
        }
        User u = userService.getOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, registerVo.getUsername())
        );
        //说明用户名重名
        if(u!=null){
            throw new RuntimeException(MsgConstant.REGISTER_ERR);
        }
        User user = new User();
        user.setEmail(registerVo.getEmail());
        user.setUsername(registerVo.getUsername());
        user.setPassword(passwordEncoder.encode(registerVo.getPassword()));
        user.setDelFlag(StatusConstant.EXISTS);
        user.setStatus(StatusConstant.ENABLED);
        user.setLastIp(request.getRemoteAddr());
        user.setGender(StatusConstant.UN_KNOW);
        user.setCreateBy("0");
        user.setUpdateBy("0");
        userService.save(user);
        roleService.addUserRole(user.getId(), RoleConstant.COMMON);
        //异步新增用户主页
        threadService.generateUserHome(user.getId(),ccProperties.getFreemarkerUserHomeOutPut());
        return Result.success();
    }
    @ApiOperation("获取用户个人信息")
    @GetMapping("/getInfo")
    @PreAuthorize("@auth.authenticate()")
    public Result<UserVo> getUserInfo(Long userId)
    {
        Long id = SysContext.getUserId();
        if(WebUtil.notNullNumber(userId)){
            id=userId;
        }
        String val = redisUtil.getVal(RedisConstant.USER_INFO+id);
        if (StringUtils.hasText(val)) {
            return Result.success(JSONUtil.toBean(val,UserVo.class));
        }
        UserVo userVo = userService.getWithRole(id);
        userVo.setId(id);
        redisUtil.setVal(RedisConstant.USER_INFO+id,userVo,Duration.ofMinutes(30));
        return Result.success(userVo);
    }

    @ApiOperation("用户退出登录")
    @GetMapping("/exit")
    @PreAuthorize("@auth.authenticate()")
    public Result exit(HttpServletRequest request)
    {
        Long userId = SysContext.getUserId();
        User u =new User() ;
        u.setId(userId);
        u.setLastIp(request.getRemoteAddr());
        userService.updateById(u);
        request.getSession().invalidate();
         clearUserCache();
        SysContext.remove();
        SecurityContextHolder.clearContext();
        AuthenticationContextHolder.clearContext();
        return Result.success();
    }
    @ApiOperation("修改用户基本信息")
    @PutMapping
    @Log(value = OperationType.UPDATE,title = "用户模块")
    @PreAuthorize("@auth.hasPerms('user:userInfo:edit')")
    public Result saveUser(@RequestBody @Valid UserDto u)
    {
        //确保邮箱唯一
        int count = userService.count(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, u.getEmail()));
        if(count>1){
            return Result.clientError("邮箱号重复");
        }
        String img = u.getHeadImage();
        User user = new User();
        BeanUtils.copyProperties(u,user);
        //更新用户信息
        userService.updateById(user);
        //如果上传了图片
        if (StringUtils.hasText(img)) {
            //图片是否在缓存中
            boolean status = redisUtil.getSetVal(RedisConstant.CACHE_FILE + WebUtil.
                    formatTime("yyyy/MM/dd:") + u.getId(),img);
            if (status){
                //说明不是多余的,是需要真正上传到服务器
                redisUtil.setValForSet(RedisConstant.CACHE_REAL_FILE+WebUtil.
                        formatTime("yyyy/MM/dd:")+u.getId(),img,RedisConstant.
                        CACHE_FILE_TTL,TimeUnit.DAYS);
            }
        }
        //更新角色
        roleService.updateUserRoleByNameWithUid(u.getId(),u.getRoles());
        clearUserCache();
        return Result.success();
    }
    @ApiOperation("上传文件")
    @PostMapping("/uploadImg")
    @PreAuthorize("@auth.hasPerms('user:userInfo:edit')")
    public Result uploadHeadImg(@RequestBody MultipartFile file)
    {
        String contentType = file.getContentType();
        if(!WebUtil.strContains(contentType, MinioUtil.types)){
            return Result.clientError(MsgConstant.NO_SUPPORT_FILE_TYPE);
        }
        Long userId = SysContext.getUserId();
        String src = minioUtil.simpleUploadFile(file);
        redisUtil.setValForSet(RedisConstant.CACHE_FILE+WebUtil.formatTime("yyyy/MM/dd:")+userId,
                src,RedisConstant.CACHE_FILE_TTL,TimeUnit.DAYS);
        return Result.success(src);
    }
    @ApiOperation("修改密码")
    @PostMapping("/updatePwd")
    @PreAuthorize("@auth.hasPerms('user:userInfo:edit')")
    public Result UserPwd(@RequestBody @Valid UserPwdDto dto)
    {
        Long userId = SysContext.getUserId();
        User u = userService.getOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getId, userId)
                        .eq(User::getPassword, passwordEncoder.encode(dto.getCurPwd()))
                        .eq(User::getStatus, StatusConstant.ENABLED)
                        .eq(User::getDelFlag,StatusConstant.EXISTS)
        );
        if(u==null){
            return Result.clientError(MsgConstant.INPUT_PWD_ERR);
        }
        if (!dto.getNextPwd().equals(dto.getCheckPwd())) {
            return Result.clientError(MsgConstant.TWO_PWD_INCONSISTENT);
        }
        User user = new User();
        user.setId(userId);
        user.setPassword(passwordEncoder.encode(dto.getNextPwd()));
        boolean b = userService.updateById(user);
        if (b) {
            return Result.success();
        }
        redisUtil.del(RedisConstant.USER_INFO+user.getId());
        return Result.clientError(MsgConstant.DATA_ERR);
    }

    @ApiOperation("获取用户列表")
    @GetMapping("/list")
    @PreAuthorize("@auth.hasPerms('user:userInfo:select')")
    public Result<PageVo<UserVo>> listUsers(@Valid PageUserDto query)
    {
        String val = redisUtil.getVal(RedisConstant.USER_LIST);
        if (StringUtils.hasText(val)) {
            return Result.success(JSONUtil.toBean(val,PageVo.class));
        }
        PageVo<UserVo> list= userService.listWithRoles(query);
        redisUtil.setVal(RedisConstant.USER_LIST,list,Duration.ofMinutes(30));
        return Result.success(list);
    }
    @ApiOperation("修改用户账号是否启用状态/是否激活状态")
    @PutMapping("/editStatus/{id}/{status}/{delFlag}")
    @Log(value = OperationType.UPDATE,title = "用户模块")
    @PreAuthorize("@auth.hasPerms('user:userInfo:edit')")
    public Result editUserStatus(@PathVariable @NotNull @Min(value = 0,message = "用户标识异常") Long id,
                                 @PathVariable @NotNull(message = MsgConstant.ENABLE_ERR)
                                 @Min(value = 0,message = MsgConstant.ENABLE_ERR)
                                 @Max(value = 1,message = MsgConstant.ENABLE_ERR) Integer status,
                                 @PathVariable
                                 @NotNull(message = MsgConstant.ACTIVATION_STATUS_ERR) String delFlag)
    {
        if(id.equals("1")){
            return Result.serverError("无法修改超级管理员");
        }
        User user = userService.getById(id);
        if (user == null) {
            return Result.serverError(MsgConstant.USER_NO_EXIST);
        }
        if (user.getStatus().equals(status)) {
            //表示修改激活状态
            if(StatusConstant.NOT_EXISTS.equals(delFlag)||StatusConstant.EXISTS.equals(delFlag) ){
                user.setDelFlag(delFlag);
            } else {
                return Result.clientError(MsgConstant.ACTIVATION_STATUS_ERR);
            }
            userService.updateById(user);
        } else if (user.getDelFlag().equals(delFlag)) {
            //表示修改账号启用状态
            if(StatusConstant.DISABLE.equals(status)||StatusConstant.ENABLED.equals(status)){
                user.setStatus(status);
            }else {
                return Result.clientError(MsgConstant.ENABLE_ERR);
            }
            userService.updateById(user);
        }else {
            return Result.clientError(MsgConstant.QUERY_PARAM_ERR);
        }
        return Result.success();
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/dels")
    @Log(value = OperationType.DELETE,title = "用户模块")
    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize("@auth.hasPerms('user:userInfo:delete')")
    public Result dels(@RequestParam("ids") List<Long> ids)
    {
        if (CollectionUtils.isEmpty(ids)) {
            return Result.clientError(MsgConstant.DATA_ERR);
        }
        if (ids.contains(1L)){
            return Result.serverError("不能删除超级管理员");
        }
        //删除用户信息
        userService.removeByIds(ids);
        //删除用户评论信息
        userCommentService.removeByIds(ids);
        ids.forEach(id-> {
            //删除用户角色
            roleService.delRoleByUid(id);
            //删除粉丝和关注
            userFollowService.removeFollowAndFans(id);
            //删除主页
            WebUtil.delExistsFile(ccProperties.getFreemarkerUserHomeOutPut()+"\\userHome"+id+".vue");
        });
        clearUserCache();
        return Result.success();
    }
    @ApiOperation("新增用户")
    @Log(value = OperationType.INSERT,title = "用户模块")
    @PostMapping("/addUserWithRole")
    @PreAuthorize("@auth.hasPerms('user:userInfo:add')")
    @Transactional
    public Result addUserWithRole(@RequestBody Map<String,Object> map,HttpServletRequest request)
    {
        String username = (String) map.get("username");
        if (!StringUtils.hasText(username)) {
            return Result.clientError(MsgConstant.QUERY_PARAM_ERR);
        }
        String password = (String) map.get("password");
        if (!StringUtils.hasText(password)||!password.matches("^\\S{6,16}$")) {
            return Result.clientError(MsgConstant.QUERY_PARAM_ERR);
        }
        String email = (String) map.get("email");
        if (!StringUtils.hasText(email)||!email.matches("^\\w{5,16}@.{2,8}\\.[a-zA-Z]{1,4}$")) {
            return Result.clientError(MsgConstant.QUERY_PARAM_ERR);
        }
        String gender = (String) map.get("gender");
        if (!StringUtils.hasText(gender)) {
            gender=StatusConstant.UN_KNOW;
        }
        String nickname = (String) map.get("nickname");
        if (!StringUtils.hasText(nickname)||!nickname.matches("^\\S{4,10}$")) {
            return Result.clientError(MsgConstant.QUERY_PARAM_ERR);
        }
        String remark = (String) map.get("remark");
        List<String> list = (List<String>) map.get("roles");
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setGender(gender);
        user.setLastIp(request.getRemoteAddr());
        user.setNickname(nickname);
        user.setRemark(remark);
        user.setStatus(StatusConstant.ENABLED);
        user.setDelFlag(StatusConstant.EXISTS);
        user.setCreateBy(String.valueOf(SysContext.getUserId()));
        user.setUpdateBy(String.valueOf(SysContext.getUserId()));
        userService.save(user);
        //是否设置角色
        if (!CollectionUtils.isEmpty(list)) {
            roleService.updateUserRoleByNameWithUid(user.getId(),list);
        }
        //设置用户评论信息
        UserComment userComment = new UserComment();
        userComment.setId(user.getId());
        userComment.setExperience(0);
        userComment.setLevel(1);
        userComment.setHomeLink("/userHome"+user.getId());
        userCommentService.save(userComment);
        clearUserCache();
        return Result.success();
    }

    @ApiOperation("获取用户作者信息")
    @GetMapping("/getAuthorInfo/{id}")
    public Result<UserHomeVo> getAuthorInfo(@PathVariable
                                @NotNull(message =MsgConstant.QUERY_PARAM_ERR)
                                @Min(value = 1,message ="用户标识不能为负数") Long id)
    {
        String val = redisUtil.getVal(RedisConstant.AUTHOR_INFO + id);
        if (StringUtils.hasText(val)) {
            return Result.success(JSONUtil.toBean(val,UserHomeVo.class));
        }
        User u = userService.getById(id);
        if (u == null) {
            return Result.serverError(MsgConstant.USER_NO_EXISTS);
        }
        UserHomeVo userHomeVo = new UserHomeVo();
        BeanUtils.copyProperties(u,userHomeVo);
        UserComment uc = userCommentService.getById(id);
        //获取经验和等级
        userHomeVo.setLevel(uc.getLevel());
        userHomeVo.setExperience(uc.getExperience());
        //获取文章数量
        int articleNum = articleService.count(
                new LambdaQueryWrapper<Article>()
                        .eq(Article::getUserId, id)
                        .eq(Article::getStatus,StatusConstant.PUBLISHED)
        );
        //获取评论数量
        int commentNum = commentsService.count(
                new LambdaQueryWrapper<Comments>()
                        .eq(Comments::getUid, id)
        );
        userHomeVo.setArticleNum(articleNum);
        userHomeVo.setCommentNum(commentNum);
        //获取关注数量和粉丝数量
        List<Long> followNum = userFollowService.getFollowNum(id);
        List<Long> fansNum = userFollowService.getFansNum(id);
        userHomeVo.setFollowIds(followNum);
        userHomeVo.setFollowNum(WebUtil.listSize(followNum));
        userHomeVo.setFansNum(WebUtil.listSize(fansNum));
        userHomeVo.setFansIds(fansNum);
        redisUtil.setVal(RedisConstant.AUTHOR_INFO+uc.getId(),userHomeVo,Duration.ofMinutes(20));
        return Result.success(userHomeVo);
    }
    @ApiOperation("获取作者信息根据文章id")
    @GetMapping("/authorInfo/{id}")
    public Result getAuthorInfoByAid(@PathVariable
                                    @NotNull(message =MsgConstant.QUERY_PARAM_ERR)
                                    @Min(value = 1,message ="用户标识不能为负数") Long id)
    {
        Article byId = articleService.getById(id);
        if (byId == null) {
            return Result.serverError(MsgConstant.ARTICLE_NO_EXISTS);
        }
        Long userId = byId.getUserId();
        User u = userService.getById(userId);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(u,userDto);
        return Result.success(userDto);
    }
    @ApiOperation("用户关注")
    @PutMapping("/follow/{status}")
    @PreAuthorize("@auth.authenticate()")
    public Result follow(@RequestBody @Valid UserFollow follow,
                         @PathVariable @NotNull(message = "关注异常,缺失参数")
                         @Min(value = 0,message = "没有此状态")@Max(value = 1,message = "没有此状态")
                         Integer status)
    {
        //说明关注
        if (StatusConstant.FOLLOW==status) {
          userFollowService.save(follow);
         //说明取关
        }else {
            userFollowService.cancelFollow(follow);
        }
        clearUserCache();
        return Result.success();
    }
    @ApiOperation("获取已注册的用户id")
    @GetMapping("/registeredID")
    public Result<List<Long>> getRegisterUserId(){
        String val = redisUtil.getVal(RedisConstant.AUTHOR_IDS);
        if (StringUtils.hasText(val)) {
            List<Long> uids = JSONUtil.toList(val, Long.class);
            return Result.success(uids);
        }
        List<Long> ids=userService.getRegisterUserId();
        redisUtil.setVal(RedisConstant.AUTHOR_IDS,ids,Duration.ofHours(3));
       return Result.success(ids);
    }
    @ApiOperation("是否是Admin用户")
    @GetMapping("/isAdmin")
    @PreAuthorize("@auth.authenticate()")
    public Result isAdmin(){
        Long userId = SysContext.getUserId();
        List<Role> rolesByUId = roleService.getRolesByUId(userId);
        return Result.success(WebUtil.checkAdmin(userId,rolesByUId));
    }

    @ApiOperation("获取已关注的用户列表信息")
    @GetMapping("/followList")
    @PreAuthorize("@auth.authenticate()")
    public Result<List<UserFollowVo>> getFollowList()
    {
        Long userId = SysContext.getUserId();
        List<UserFollow> list = userFollowService.
                list(new LambdaQueryWrapper<UserFollow>().
                eq(UserFollow::getFansId, userId));
        if (CollectionUtils.isEmpty(list)) {
            return Result.success(Collections.emptyList());
        }
        List<UserFollowVo> collect = list.stream().map(follow -> {
            Long curId = follow.getCurId();
            User u = userService.getById(curId);
            UserFollowVo wf = new UserFollowVo();
            wf.setCurId(curId);
            wf.setFansId(follow.getFansId());
            wf.setNickname(u.getNickname());
            wf.setUsername(u.getUsername());
            wf.setHeadImage(u.getHeadImage());
            return wf;
        }).collect(Collectors.toList());
        return Result.success(collect);
    }
    @ApiOperation("获取粉丝列表")
    @GetMapping("/fansList")
    @PreAuthorize("@auth.authenticate()")
    public Result<List<UserFollowVo>> getFansList()
    {
        Long userId = SysContext.getUserId();
        List<UserFollow> list = userFollowService.list(new LambdaQueryWrapper<UserFollow>()
                .eq(UserFollow::getCurId, userId));
        if (CollectionUtils.isEmpty(list)) {
            return Result.success(Collections.emptyList());
        }
        List<UserFollowVo> collect = list.stream().map(follow -> {
            Long fansId = follow.getFansId();
            User u = userService.getById(fansId);
            UserFollowVo wf = new UserFollowVo();
            wf.setCurId(userId);
            wf.setFansId(follow.getFansId());
            wf.setNickname(u.getNickname());
            wf.setUsername(u.getUsername());
            wf.setHeadImage(u.getHeadImage());
            return wf;
        }).collect(Collectors.toList());

        return Result.success(collect);
    }

    @ApiOperation("用户签到")
    @PutMapping("/signIn")
    @PreAuthorize("@auth.authenticate()")
    public Result signIn(){
        Long userId = SysContext.getUserId();
        LocalDate now = LocalDate.now();
        //本月
        int monthValue = now.getMonthValue();
        //当天
        int cur=now.getDayOfMonth();
        String k=RedisConstant.USER_SIGN+monthValue+":"+userId;
        //获取今天的状态
        Boolean bit = redisUtil.val().getBit(k, cur-1);
        log.info("{}-{}-是否签到：{}",monthValue,cur,bit);
        if(WebUtil.isTrue(bit)){
           return Result.clientError("今天已经签到过了，请明天在签到");
        }
        redisUtil.val().setBit(k,cur-1,true);
        UserComment uc = userCommentService.getById(userId);
        if (uc.getLevel()<6) {
            uc.setExperience(uc.getExperience()+10);
            userCommentService.updateById(uc);
        }
        int days = WebUtil.judgeDay(now);
        if (cur==7||cur==14||cur==21||(cur==28&&days!=28)||cur==days ) {
            //异步颁发奖励
            threadService.sendSignRewards(userId,monthValue,cur,days);
        }
        log.info("{}-{}：进行签到",monthValue,cur,WebUtil.judgeDay(now));
        return Result.success("签到成功");
    }
    @ApiOperation("获取用户本月签到次数")
    @GetMapping("/signInNum/{month}")
    @PreAuthorize("@auth.authenticate()")
    public Result<UserSignVo> getSignNum(@PathVariable
                                 @NotNull
                                 @Range(min = 1,max = 12) Integer month)
    {
        LocalDate now = LocalDate.now();
        //选中月
        LocalDate curCheck = now.withMonth(month);
        //选中月的总天数
        int days = WebUtil.judgeDay(curCheck);
        UserSignVo userSignVo = new UserSignVo();
        userSignVo.setDaysMonth(days);
        userSignVo.setSignDays(new int[days]);
        userSignVo.setSignMonthNum(0);
        userSignVo.setMaxCoiledNum(0);
        Long userId = SysContext.getUserId();
        userSignVo.setId(userId);
        //key
        String k=RedisConstant.USER_SIGN+month+":"+userId;
        ValueOperations<String, String> val = redisUtil.val();
        //最大连续和当前连续次数
        int max=0;
        int curNum=0;
        for (int i = 0; i < days; i++) {
            Boolean bit = val.getBit(k, i);
            //已签到
            if(bit){
                curNum++;
                userSignVo.setSignMonthNum(userSignVo.getSignMonthNum()+1);
                userSignVo.getSignDays()[i]=(i+1);
            }else {
                max=Math.max(max,curNum);
                curNum=0;
                userSignVo.getSignDays()[i]=0;
            }
        }
        max=Math.max(max,curNum);
        userSignVo.setMaxCoiledNum(max);
        log.info("{}月签到信息：{}",month,userSignVo);
        return Result.success(userSignVo);
    }
    @ApiOperation("检查今天是否签到")
    @GetMapping("/checkSignIn")
    @PreAuthorize("@auth.authenticate()")
    public Result checkSignIn()
    {
        Long userId = SysContext.getUserId();
        int monthValue = LocalDate.now().getMonthValue();
        String k=RedisConstant.USER_SIGN+monthValue+":"+userId;
        Boolean bit = redisUtil.val().getBit(k, LocalDate.now().getDayOfMonth()-1);
        log.info("用户{}-签到状态：{}",userId,bit);
        return Result.success(WebUtil.isTrue(bit));
    }


    public void clearUserCache(){
        redisUtil.del(RedisConstant.USER_LIST);
        redisUtil.delBatch(RedisConstant.USER_INFO+"*");
        redisUtil.del(RedisConstant.KYOU_FRONT);
        redisUtil.del(RedisConstant.KYOU_FRONT);
        redisUtil.delBatch(RedisConstant.AUTHOR_INFO+"*");
        redisUtil.del(RedisConstant.AUTHOR_IDS);
    }
}


