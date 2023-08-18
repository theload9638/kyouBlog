package com.kyou.blog.background.web;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kyou.blog.background.properties.CCProperties;

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

import com.kyou.blog.dataService.service.*;
import com.kyou.blog.model.dto.ArticleDto;
import com.kyou.blog.model.dto.PageArticleDto;
import com.kyou.blog.model.entity.*;
import com.kyou.blog.model.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * <p>
 * 文章表 前端控制器
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
@Api(tags = "文章相关接口")
@RestController
@RequestMapping("/sys/article")
public class ArticleController extends BaseController{
    @Autowired
    private MinioUtil minioUtil;
    @Autowired
    private CCProperties ccProperties;
    @Autowired
    private ThreadService threadService;


    @PostMapping("/uploadImg")
    @ApiOperation("上传文章的缩略图")
    @PreAuthorize("@auth.authenticate()")
    public Result uploadArticleImg(@RequestBody MultipartFile file)
    {
        String contentType = file.getContentType();
        if (!WebUtil.strContains(contentType, MinioUtil.types)) {
            return Result.clientError(MsgConstant.NO_SUPPORT_FILE_TYPE);
        }
        Long userId = SysContext.getUserId();
        String src = minioUtil.simpleUploadFile(file);
        redisUtil.setValForSet(RedisConstant.ARTICLE_CACHE_IMG + WebUtil.formatTime("yyyy/MM/dd:") + userId,
                src, RedisConstant.ARTICLE_IMG_TTL, TimeUnit.DAYS);
        return Result.success(src);
    }

    @ApiOperation("文章内容图")
    @PostMapping("/contentImg")
    @PreAuthorize("@auth.hasPerms('os:blog:edit')")
    public Result insertImg(@RequestBody MultipartFile image)
    {
        String contentType = image.getContentType();
        if (!WebUtil.strContains(contentType, MinioUtil.types)) {
            return Result.clientError(MsgConstant.NO_SUPPORT_FILE_TYPE);
        }
        String src = minioUtil.simpleUploadFile(image);
        return Result.success();
    }

    @ApiOperation("新增文章")
    @PostMapping("/addArticle")
    @Log(value = OperationType.INSERT,title = "文章模块")
    @PreAuthorize("@auth.hasPerms('os:blog:add')")
    public Result addArticle(@RequestBody @Valid ArticleDto articleDto)
    {
        Article article = new Article();
        BeanUtils.copyProperties(articleDto, article, "id","isComment");
        article.setUserId(SysContext.getUserId());
        article.setIsComment(articleDto.getIsComment()?StatusConstant.
                COMMENT_ALLOWED:StatusConstant.COMMENT_NOT_ALLOWED);
        article.setOrderBy(10);
        articleService.save(article);
        if (!CollectionUtils.isEmpty(articleDto.getTags())) {
            //根据标签名获取标签id
            List<Long> tids = tagService.selectIdsByNames(articleDto.getTags());
            Long id = article.getId();
            //新增文章标签
            tagService.addArticleTags(id, tids);
        }
        //新增文章分类
        if (!CollectionUtils.isEmpty(articleDto.getCategoryIds())) {
            Long id = article.getId();
            categoryService.addArticleCategories(id, articleDto.getCategoryIds());
        }
        //不再生成模板文件，已采用 数据复制 代替 冗余文件
//        if (StatusConstant.PUBLISHED==article.getStatus()) {
//            //发布的文章，此时生成磁盘文件
//            threadService.generateArticle(article.getId(),
//                    article.getTitle(),
//                    ccProperties.getFreemarkerOutput());
//        }
        clearCache();
        return Result.success(article.getId());
    }

    @ApiOperation("更新文章")
    @PutMapping("/updArticle")
    @Log(value = OperationType.UPDATE,title = "文章模块")
    @PreAuthorize("@auth.hasPerms('os:blog:edit')")
    @Transactional(rollbackFor = Exception.class)
    public Result updArticle(@RequestBody @Valid ArticleDto dto)
    {
        Article article = articleService.getById(dto.getId());
        if (article==null) {
            return Result.serverError(MsgConstant.DATA_NO_EXIST_ERR);
        }
        //先更新文章
        Article article1 = new Article();
        BeanUtils.copyProperties(dto, article1,"isComment");
        article1.setUserId(SysContext.getUserId());
        article1.setIsComment(dto.getIsComment()?StatusConstant.
                COMMENT_ALLOWED:StatusConstant.COMMENT_NOT_ALLOWED);
        articleService.updateById(article1);
        //更新标签
        //删除之前的标签
        tagService.delArticleTags(article.getId());
        //根据标签名获取标签id
        if (!CollectionUtils.isEmpty(dto.getTags())) {
            List<Long> tids = tagService.selectIdsByNames(dto.getTags());
            tagService.addArticleTags(article.getId(), tids);
        }
        //更新文章分类
        Long id = article.getId();
        categoryService.delArticleCategories(id);
        if (!CollectionUtils.isEmpty(dto.getCategoryIds())) {
            categoryService.addArticleCategories(id, dto.getCategoryIds());
        }
        clearCache();
        return Result.success();
    }

    @ApiOperation("分页查询文章")
    @GetMapping("/page")
    @PreAuthorize("@auth.authenticate()")
    public Result<Page<ArticleVo>> page(@Valid PageArticleDto params)
    {
        Page<Article> pag = new Page<>(params.getPageNum(), params.getPageSize());
        Long userId = SysContext.getUserId();
        SFunction<Article, ?> contentType = null;
        //文章类型
        String type = params.getType();
        if (StringUtils.hasText(type)) {
            if ("0".equals(type)) {
                contentType = Article::getTitle;
            } else {
                contentType = Article::getContent;
            }
        }
        SFunction<Article, ?> titleType = null;
        //文章排序值
        String order = params.getOrder();
        if (StringUtils.hasText(order)) {
            if ("0".equals(order)) {
                titleType = Article::getCreateTime;
            } else if ("1".equals(order)) {
                titleType = Article::getViewCount;
            } else if ("2".equals(order)) {
                titleType = Article::getLikeCount;
            } else {
                titleType = Article::getCommentCount;
            }
        }
        Page<Article> page;
        User loginUser = userService.getById(userId);
        List<Role> roles = roleService.getRolesByUId(userId);
        //权限展示数据
        if (WebUtil.containRole(roles, RoleConstant.ADMIN) || WebUtil.containRole(roles, RoleConstant.AUTHOR)) {
            //有管理员或者审核员权限
            page = articleService.page(pag,
                    new LambdaQueryWrapper<Article>()
                            .eq(Article::getStatus, Integer.parseInt(params.getStatus()))
                            .like(StringUtils.hasText(type), contentType, params.getKw())
                            .or()
                            .like(StringUtils.hasText(order), titleType, params.getKw()));

        } else {
            page = articleService.page(pag,
                    new LambdaQueryWrapper<Article>()
                            .eq(Article::getUserId, userId)
                            .eq(Article::getStatus, Integer.parseInt(params.getStatus()))
                            .like(StringUtils.hasText(type), contentType, params.getKw())
                            .or()
                            .like(StringUtils.hasText(order), titleType, params.getKw())
            );
        }
        //转化数据
        List<ArticleVo> list = page.getRecords().stream().map(article -> {
            ArticleVo articleVo = new ArticleVo();
            articleVo.setId(article.getId());
            articleVo.setTitle(article.getTitle());
            articleVo.setSummary(article.getSummary());
            articleVo.setAuthor(loginUser.getUsername());
            articleVo.setComment(article.getCommentCount());
            articleVo.setViewNum(article.getViewCount());
            articleVo.setPublishDate(article.getCreateTime());
            return articleVo;
        }).collect(Collectors.toList());
        Page<ArticleVo> result = new Page<ArticleVo>();
        result.setRecords(list);
        result.setTotal(page.getTotal());
        return Result.success(result);
    }

    @ApiOperation("审批文章")
    @GetMapping("/valid/{id}")
    @Log(value = OperationType.UPDATE,title = "文章模块")
    @PreAuthorize("@auth.hasPerms('os:blog:edit')")
    public Result validArticle(@PathVariable
                               @Min(value = 1, message = "文章编号不能为负数") Long id)
    {
        Article article = articleService.getById(id);
        if (article==null) {
            return Result.serverError(MsgConstant.DATA_NO_EXIST_ERR);
        }
        if (article.getStatus().equals(StatusConstant.UNDER_REVIEW)) {
            article.setStatus(StatusConstant.PUBLISHED);
            articleService.updateById(article);
            //不再生成模板文件，已采用 数据复制 代替 冗余文件
//            threadService.generateArticle(id,article.getTitle(),ccProperties.getFreemarkerOutput());
           clearCache();
            return Result.success();
        } else {
            return Result.clientError("审核失败");
        }
    }

    @ApiOperation("下架文章")
    @GetMapping("/takeDown/{id}")
    @Log(value = OperationType.UPDATE,title = "文章模块")
    @PreAuthorize("@auth.hasPerms('os:blog:edit')")
    public Result takeDownArticle(@PathVariable
                                  @Min(value = 1, message = "文章编号不能为负数") Long id)
    {
        Article article = articleService.getById(id);
        //文章不存在
        if (article==null) {
            return Result.serverError(MsgConstant.DATA_NO_EXIST_ERR);
        }
        //文章已下架或审核失败的无法下架
        if (article.getStatus().equals(StatusConstant.TAKE_DOWN) ||
                article.getStatus().equals(StatusConstant.REVIEW_FAILED)) {
            return Result.clientError("下架失败");
        } else {
            article.setStatus(StatusConstant.TAKE_DOWN);
            articleService.updateById(article);
            //下架后删除对应的磁盘文件
            //因为不采用 文件复制 所以 不用
//            String path=String.format("%s\\%s%s%s",ccProperties.getFreemarkerOutput(),"article",id,".vue");
//            WebUtil.delExistsFile(path);
            clearCache();
            return Result.success();
        }
    }

    @ApiOperation("批量删除")
    @DeleteMapping
    @PreAuthorize("@auth.hasPerms('os:blog:remove')")
    @Transactional
    @Log(value = OperationType.DELETE,title = "文章模块")
    public Result del(@RequestParam("ids") @NotNull List<Long> ids)
    {
        if (CollectionUtils.isEmpty(ids)) {
            return Result.success();
        }
        articleService.removeByIds(ids);
        //因为不采用 文件复制 所以 不用
//        ids.forEach(id->{
//            //删除对应的磁盘文件
//            String path=String.format("%s\\%s%s%s",
//                    ccProperties.getFreemarkerOutput(),
//                    "article",id,".vue");
//            WebUtil.delExistsFile(path);
//        });
        //删除所有对应的评论
        commentsService.removeByAIds(ids);
        clearCache();
        return Result.success();
    }

    @ApiOperation("获取文章信息")
    @GetMapping("/getInfo/{id}")
    public Result<ArticleDto> getInfo(@PathVariable
                                      @NotNull(message = "文章标识不存在")
                                      @Min(value = 1, message = "文章标识不存在") Long id,
                                     @RequestParam(value = "uid")
                                     @NotBlank(message = MsgConstant.ARTICLE_ACCESS_ERR) String uid,
                                      @RequestParam("mode") @NotNull(message = MsgConstant.ARTICLE_ACCESS_ERR)
                                          @Min(value = 0,message =MsgConstant.ARTICLE_ACCESS_ERR )
                                          @Max(value = 1,message = MsgConstant.ARTICLE_ACCESS_ERR) Integer mode)
    {
        //获取文章信息
        Article article = articleService.getById(id);
        if ((article)==null) {
            return Result.serverError(MsgConstant.DATA_NO_EXIST_ERR);
        }
        ArticleDto articleDto = new ArticleDto();
        BeanUtils.copyProperties(article,articleDto,"isComment");
        //设置评论状态
        articleDto.setIsComment(article.getIsComment()==StatusConstant.COMMENT_ALLOWED?true:false);
        //文章访问状态下设置
        if(StatusConstant.ARTICLE_SHOW.equals(mode)){
            //浏览量的key
            String key =RedisConstant.ARTICLE_VIEW+id;
            //当前用户是否访问过
            boolean isView = redisUtil.getSetVal(key, uid);
            //当前用户访问过了
            if (!isView) {
                //没访问过
                redisUtil.setValForSet(key,uid);
                articleDto.setViewCount(redisUtil.getSetSize(key).intValue());
            }
        }
        //文章评论数：是否开启评论区
        if(StatusConstant.CLOSE_COMMENT.equals(article.getIsComment())){
            articleDto.setCommentCount(0);
        }
        //当前用户是否点赞此文章
        String likeKey=RedisConstant.ARTICLE_LIKE+id;
        articleDto.setLikeStatus(WebUtil.isTrue(redisUtil.set().isMember(likeKey,uid)));
        //获取文章标签名
        List<String> tags=tagService.getArticleTags(id);
        if (CollectionUtils.isEmpty(tags)) {
            tags= Collections.emptyList();
        }
        articleDto.setTags(tags);
        //获取文章分类id
        List<Long> cids=categoryService.getArticleCategoryIds(id);
        if (CollectionUtils.isEmpty(cids)) {
            cids= Collections.emptyList();
        }
        articleDto.setCategoryIds(cids);
        return Result.success(articleDto);
    }
    @ApiOperation(value = "文章点赞",notes = "游客可进行点赞")
    @PutMapping("/likeArticle/{articleId}")
    public Result likeArticle(@PathVariable @NotNull(message = "文章不存在") Long articleId
            ,@RequestParam("uid")@NotBlank(message = "游客/用户不存在") String uid,
             @RequestParam("type")@NotNull(message = "点赞异常") Integer type)
    {
        String key=RedisConstant.ARTICLE_LIKE+articleId;
        //检查是用户还是游客
        boolean flag=WebUtil.isNumber(uid);
        if(flag){
            //检查是否登录
            User user = userService.getById(uid);
            if (user == null) {
                return Result.clientError(MsgConstant.LOGIN_ERR);
            }
        }
        Article a = articleService.getById(articleId);
        if (a == null) {
            return Result.serverError(MsgConstant.ARTICLE_NO_EXISTS);
        }
        Integer likeCount = a.getLikeCount();
        likeCount=likeCount==null?0:likeCount;
        if(type==-1){
            //取消点赞
            Article article = new Article();
            article.setId(a.getId());
            article.setLikeCount(likeCount-1);
            articleService.updateById(article);
            redisUtil.set().remove(key,uid);
        }else if(type==1){
            //点赞
            Article article = new Article();
            article.setId(a.getId());
            article.setLikeCount(likeCount+1);
            articleService.updateById(article);
            redisUtil.set().add(key,uid);
        }else {
            return Result.clientError(MsgConstant.QUERY_PARAM_ERR);
        }
        clearCache();
        return Result.success();
    }

    @ApiOperation("获取文章标签和分类")
    @GetMapping("/tagAndCategory/{id}")
    public Result<ArticleTCVo> getArticleTagAndCategory(@PathVariable
                                           @NotNull(message = MsgConstant.ARTICLE_NO_EXISTS)
                                           @Min(value = 1,message = MsgConstant.ARTICLE_NO_EXISTS) Long id)
    {
        Article a = articleService.getById(id);
        if (a == null) {
            return Result.serverError(MsgConstant.ARTICLE_NO_EXISTS);
        }
        ArticleTCVo v = new ArticleTCVo();
        List<Tag> tags=articleService.getArticleTags(id);
        List<Category> categories=articleService.getArticleCategories(id);
        v.setId(id);
        v.setTags(tags);
        v.setCategories(categories);
        return Result.success(v);
    }
    @ApiOperation("获取所有发布文章的id")
    @GetMapping("/getPublishedIds")
    public Result<List<Long>> getPublishedIds(){
        List<Long> ids=articleService.getPublishedIds(StatusConstant.PUBLISHED);
        return Result.success(ids);
    }
    @ApiOperation("获取作者已发布的文章")
    @GetMapping("/authorArticles/{userId}/{pageNum}/{pageSize}")
    public Result<PageVo<ArticleUserVo>> authorArticles(@PathVariable
                                     @NotNull(message = "无效的请求参数")
                                     @Min(value = 1,message = "无效的用户标识") Long userId,
                                 @PathVariable @NotNull(message = "无效的请求页码")@Min(value = 1) Integer pageNum,
                                 @PathVariable @NotNull(message = "无效的请求页大小")@Min(value = 1)Integer pageSize)
    {
        Page<Article> ap = new Page<>(pageNum, pageSize);
        Page<Article> page = articleService.page(ap, new LambdaQueryWrapper<Article>()
                .eq(Article::getStatus, StatusConstant.PUBLISHED)
                .eq(Article::getUserId, userId)
                .orderByAsc(Article::getOrderBy)
                .orderByDesc(Article::getCreateTime, Article::getLikeCount));

        List<ArticleUserVo> result = page.getRecords().stream().
                map(article->{
            ArticleUserVo articleUserVo = new ArticleUserVo();
            BeanUtils.copyProperties(article, articleUserVo);
            User u = userService.getById(article.getUserId());
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(u, userVo);
            articleUserVo.setUserVo(userVo);
            return articleUserVo;
        }).collect(Collectors.toList());
        PageVo<ArticleUserVo> auv = new PageVo<>();
        auv.setTotal(page.getTotal());
        auv.setRecords(result);

        return Result.success(auv);
    }

    @ApiOperation("按时间线获取文章")
    @GetMapping("/timeLine")
    public Result getTimeLineArticles()
    {
        List<Article> list = articleService.getPublishedArticles();
        Map<LocalDate, List<Article>> collect = list.stream().collect(Collectors.groupingBy(article -> {
            LocalDateTime createTime = article.getCreateTime();
            return LocalDate.of(createTime.getYear(), createTime.getMonth(),
                    createTime.getDayOfMonth());
        }));
        return Result.success(collect);
    }
    @ApiOperation("获取top10文章")
    @GetMapping("/top10Article")
    public Result getTop10Article(){
        List<Article> hots =  articleService.getPublishedArticles().stream().sorted(
                        Comparator.comparing(Article::getViewCount).reversed())
                .limit(10).collect(Collectors.toList());
        List<String> tagNames=tagService.getHotTag();
        SearchVo searchVo = new SearchVo();
        searchVo.setArticles(hots);
        searchVo.setTagNames(tagNames);
        return Result.success(searchVo);
    }
    @ApiOperation("搜索文章")
    @GetMapping("/searchArticle")
    public Result searchArticle(@Valid PageArticleDto dto){
        Page<Article> page = articleService.page(
                new Page<Article>(dto.getPageNum(), dto.getPageSize()),
                new LambdaQueryWrapper<Article>()
                        .eq(Article::getStatus, StatusConstant.PUBLISHED)
                        .like(Article::getContent,dto.getKw())
                        .or()
                        .like(Article::getTitle,dto.getKw())
                        .orderByDesc(Article::getCreateTime)
        );
        List<Article> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return Result.success(Collections.emptyList());
        }
        List<ArticleUserVo> collect = records.stream().sorted(
                        Comparator.comparing(Article::getLikeCount).reversed()
                                .thenComparing(Article::getCommentCount).reversed()
                                .thenComparing(Article::getViewCount).reversed())
                .map(article -> {
                    ArticleUserVo articleUserVo = new ArticleUserVo();
                    BeanUtils.copyProperties(article, articleUserVo);
                    User u = userService.getById(article.getUserId());
                    UserVo userVo = new UserVo();
                    BeanUtils.copyProperties(u, userVo);
                    articleUserVo.setUserVo(userVo);
                    return articleUserVo;
                }).collect(Collectors.toList());
        PageVo<ArticleUserVo> v = new PageVo<>();
        v.setTotal(page.getTotal());
        v.setRecords(collect);
        return Result.success(v);
    }

    public void clearCache(){
        redisUtil.del(RedisConstant.USER_LIST);
        redisUtil.delBatch(RedisConstant.USER_INFO+"*");
        redisUtil.del(RedisConstant.KYOU_FRONT);
        redisUtil.del(RedisConstant.KYOU_FRONT);
        redisUtil.delBatch(RedisConstant.AUTHOR_INFO+"*");
        redisUtil.del(RedisConstant.AUTHOR_IDS);
        redisUtil.del(RedisConstant.ARTICLE_PUBLISH);
    }
}