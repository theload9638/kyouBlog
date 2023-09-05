package com.kyou.blog.background.web;


import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kyou.blog.background.webUtil.WebUtil;
import com.kyou.blog.common.Result;
import com.kyou.blog.common.annotation.Log;
import com.kyou.blog.common.constant.MsgConstant;
import com.kyou.blog.common.constant.RedisConstant;
import com.kyou.blog.common.constant.StatusConstant;
import com.kyou.blog.common.emuration.OperationType;
import com.kyou.blog.common.util.SysContext;
import com.kyou.blog.model.entity.*;
import com.kyou.blog.model.vo.ArticleCommentVo;
import com.kyou.blog.model.vo.CommentsVo;
import com.kyou.blog.model.vo.PageVo;
import com.kyou.blog.model.vo.UserCommentInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 评论表 前端控制器
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
@Api(tags = "评论相关接口")
@RestController
@RequestMapping("/sys/comments")
public class CommentsController extends BaseController{

    @ApiOperation("新增评论")
    @PutMapping
    @Log(value = OperationType.INSERT,title = "评论模块")
    @PreAuthorize("@auth.authenticate()")
    public Result addComment(@RequestBody @NotEmpty Map<String,Object> map, HttpServletRequest request)
    {
        Long articleId = Long.valueOf((String) map.get("articleId"));
        Article a = articleService.getById(articleId);
        //文章是否存在
        if (a == null) {
            return Result.serverError(MsgConstant.ARTICLE_NO_EXISTS);
        }
        //是否可以评论
        if (StatusConstant.CLOSE_COMMENT.equals(a.getIsComment())) {
            return Result.serverError(MsgConstant.COMMENT_ALREADY_CLOSE);
        }
        Long uid=Long.valueOf(((Integer) map.get("uid")));
        Map<String, Object> user = (Map<String, Object>) map.get("user");
        Comments wrapper = WebUtil.wrapperComments(map, request);
        //保存评论
        commentsService.save(wrapper);
        //获取今天的个人经验key
        String key=RedisConstant.COMMENT_EX +
                WebUtil.formatTime(WebUtil.DATE_PATH_LINUX) + ":" + uid;
        String val = redisUtil.getVal(key);
        //每天的评论次数
        if (val==null){
            redisUtil.setVal(key,1, Duration.ofDays(RedisConstant.COMMENT_EX_TTL));
        }else {
            if(Integer.parseInt( val)<3){
                redisUtil.val().increment(key);
            }
        }
        return Result.success();
    }
    @ApiOperation("展示文章评论")
    @GetMapping("/{articleId}")
    @PreAuthorize("@auth.authenticate()")
    public Result<List<CommentsVo>> showComments(@PathVariable
                                    @NotNull(message = "文章不存在")
                                    @Min(value = 1,message = "文章不存在") Long articleId)
    {
        List<Comments> origin = commentsService.list(
                new LambdaQueryWrapper<Comments>()
                        .eq(Comments::getArticleId, articleId)
                        .orderByDesc(Comments::getLikes)
        );
        if (!CollectionUtils.isEmpty(origin)) {
            List<CommentsVo> cvs = origin.stream().map(e -> {
                CommentsVo v = new CommentsVo();
                BeanUtils.copyProperties(e,v);
                v.setUsername(e.getNickname());
                UserComment uc = userCommentService.getById(e.getUid());
                v.setLevel(uc.getLevel());
                v.setHomeLink(uc.getHomeLink());
                v.setTotal(0);
                return v;
            }).collect(Collectors.toList());
            //构建树级评论
            List<CommentsVo> res = cvs.stream().filter(e -> e.getParentId() == null)
                    .peek(e -> WebUtil.findCommentChild(e, cvs)).
                    collect(Collectors.toList());
            return Result.success(res);
        }
        return Result.success(Collections.emptyList());
    }
    @ApiOperation("获取当前评论用户信息")
    @GetMapping("/userCommentInfo")
    @PreAuthorize("@auth.authenticate()")
    public Result<UserCommentInfo> getUserCommentInfo()
    {

        Long userId = SysContext.getUserId();
        User u = userService.getById(userId);
        UserComment uc = userCommentService.getById(userId);
        UserCommentInfo info = new UserCommentInfo();
        info.setLevel(uc.getLevel());
        info.setId(userId);
        info.setUsername(u.getNickname());
        info.setAvatar(u.getHeadImage());
        //获取点赞数据
        String key=RedisConstant.LIKE_COMMENT+userId;
        info.setLikeIds(redisUtil.getSetMember(key));
        return Result.success(info);
    }
    @ApiOperation("评论点赞")
    @PutMapping("/likeComment")
    @PreAuthorize("@auth.authenticate()")
    public Result likeComment(@RequestParam("cid")
                                  @NotNull(message = "评论id不能为空")
                                  @Min(value = 1,message = "评论id不能为负数") Long cId,
                              @RequestParam("type")Integer type)
    {
        Comments c = commentsService.getById(cId);
        c.setLikes(c.getLikes()+type);
        commentsService.updateById(c);
        String key=RedisConstant.LIKE_COMMENT+SysContext.getUserId();
        if(type==1){
            redisUtil.addValForSet(key,cId);
        }else {
            redisUtil.delValForSet(key,cId);
        }
        return Result.success();
    }

    @ApiOperation("获取评论列表")
    @GetMapping("/list/{num}/{size}")
    @PreAuthorize("@auth.authenticate()")
    public Result<PageVo<ArticleCommentVo>> listComments(
            @PathVariable @NotNull(message = MsgConstant.QUERY_PARAM_ERR) @Min(value = 1) Integer num,
            @PathVariable @NotNull(message = MsgConstant.QUERY_PARAM_ERR) @Min(value = 1) Integer size
    )
    {
        Long userId = SysContext.getUserId();
        String val = redisUtil.getVal(RedisConstant.COMMENT_LIST + userId);
        if (StringUtils.hasText(val)) {
            return Result.success(JSONUtil.toBean(val,PageVo.class));
        }
        Page<Comments> cp = new Page<>(num, size);
        Page<Comments> page;
        List<Role> roles = roleService.getRolesByUId(userId);
        if (WebUtil.checkAdmin(userId,roles)) {
            page = commentsService.page(cp);
        }else {
           page=commentsService.page(cp,new LambdaQueryWrapper<Comments>()
                   .eq(Comments::getUid, userId));
        }
        List<ArticleCommentVo> acvs = page.getRecords().stream().map(c -> {
                    ArticleCommentVo acv = new ArticleCommentVo();
                    acv.setAddress(c.getAddress());
                    acv.setLikes(c.getLikes());
                    acv.setIp(c.getIp());
                    acv.setUserId(c.getUid());
                    acv.setContent(c.getContent());
                    acv.setArticleId(c.getArticleId());
                    acv.setId(c.getId());
                    acv.setPublishTime(c.getCreateTime().toString());
                    Article a = articleService.getById(c.getArticleId());
                    acv.setTitle(a.getTitle());
                    return acv;
                }).sorted(Comparator.comparing(ArticleCommentVo::getLikes).reversed())
                .collect(Collectors.toList());
        PageVo<ArticleCommentVo> pacvs = new PageVo<>();
        pacvs.setRecords(acvs);
        pacvs.setTotal(page.getTotal());
        redisUtil.setVal(RedisConstant.COMMENT_LIST + userId,pacvs,Duration.ofHours(3));
        return Result.success(pacvs);
    }




}

