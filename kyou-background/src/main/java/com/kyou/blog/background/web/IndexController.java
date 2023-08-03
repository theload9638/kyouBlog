package com.kyou.blog.background.web;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kyou.blog.background.webUtil.RedisUtil;
import com.kyou.blog.background.webUtil.WebUtil;
import com.kyou.blog.common.Result;
import com.kyou.blog.common.constant.RedisConstant;
import com.kyou.blog.common.constant.StatusConstant;
import com.kyou.blog.common.util.SysContext;
import com.kyou.blog.dataService.service.*;
import com.kyou.blog.model.dto.ArticleRankDto;
import com.kyou.blog.model.dto.PageUserDto;
import com.kyou.blog.model.entity.*;
import com.kyou.blog.model.vo.ArticleUserVo;
import com.kyou.blog.model.vo.BackInfoVo;
import com.kyou.blog.model.vo.FrontInfoVo;
import com.kyou.blog.model.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author CC
 * time 2023-07-26
 * description
 */
@Api("首页展示数据控制层")
@RestController
//@Slf4j
@RequestMapping("/sys/index")
public class IndexController {
    @DubboReference(interfaceClass = ArticleService.class)
    private ArticleService articleService;
    @DubboReference(interfaceClass = TagService.class)
    private TagService tagService;
    @DubboReference(interfaceClass = UserService.class)
    private UserService userService;
    @DubboReference(interfaceClass = CategoryService.class)
    private CategoryService categoryService;
    @DubboReference(interfaceClass = CommentsService.class)
    private CommentsService commentsService;
    @DubboReference(interfaceClass = LinksService.class)
    private LinksService linksService;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation("后台系统首页数据")
    @GetMapping("/back")
    @PreAuthorize("@auth.authenticate()")
    public Result<BackInfoVo> backInfo()
    {
        String val = redisUtil.getVal(RedisConstant.KYOU_BACK);
        if (StringUtils.hasText(val)) {
            return Result.success(JSONUtil.toBean(val,BackInfoVo.class));
        }
        BackInfoVo backInfoVo = new BackInfoVo();
        //获取发布的文章数量和评论数
        List<Article> articles = articleService.list(
                new LambdaQueryWrapper<Article>()
                        .eq(Article::getUserId, SysContext.getUserId())
                        .eq(Article::getStatus, StatusConstant.PUBLISHED)
                        .eq(Article::getIsComment,StatusConstant.OPEN_COMMENT)
        );
        if (CollectionUtils.isEmpty(articles)) {
            backInfoVo.setArticlePublished(0);
            backInfoVo.setCommentCount(0);
        }else {
            backInfoVo.setArticlePublished(articles.size());
            //开启评论的总评论数
            Integer count =  articleService.list(
                    new LambdaQueryWrapper<Article>()
                            .eq(Article::getUserId, SysContext.getUserId())
                            .eq(Article::getStatus, StatusConstant.PUBLISHED)
                            .eq(Article::getIsComment,StatusConstant.OPEN_COMMENT)
            ).stream().map(e -> Optional.ofNullable(e.getCommentCount()).orElse(0))
                    .reduce(0, Integer::sum);
            backInfoVo.setCommentCount(count);
        }
        //获取标签总数
        List<Tag> list = tagService.list();
        if (CollectionUtils.isEmpty(list)) {
            backInfoVo.setTagNum(0);
        }else {
            backInfoVo.setTagNum(list.size());
        }
        //获取加入天数
        User user = userService.getById(SysContext.getUserId());
        backInfoVo.setJoinDays(
                Period.between(user.getCreateTime().toLocalDate(), LocalDate.now()).getDays());
        //获取文章排行榜的前5位
        List<ArticleRankDto> rankDtoList=articleService.getRankList();
        if (!CollectionUtils.isEmpty(rankDtoList)) {
            rankDtoList.sort(Comparator.comparing(ArticleRankDto::getArticleNum).reversed());
            for (int i = 0; i < rankDtoList.size(); i++) {
                ArticleRankDto articleRankDto = rankDtoList.get(i);
                articleRankDto.setRankId((i+1));
            }
        }
        backInfoVo.setRankList(rankDtoList);
        //获取最晚注册的8个用户
        List<UserVo> userVos=userService.getLastRegisterUsers();
        backInfoVo.setUsers(userVos);
        redisUtil.setVal(RedisConstant.KYOU_BACK,backInfoVo, Duration.ofMinutes(20));
        return Result.success(backInfoVo);
    }

    @ApiOperation("获取首页数据")
    @GetMapping("/front")
    public Result<FrontInfoVo> frontInfo(@Valid PageUserDto params)
    {
        String val = redisUtil.getVal(RedisConstant.KYOU_FRONT);
        if (StringUtils.hasText(val)) {
            return Result.success(JSONUtil.toBean(val,FrontInfoVo.class));
        }
        //获取文章数据
        List<Article> articles = articleService.getPublishedArticles();
        FrontInfoVo frontInfoVo = new FrontInfoVo();
        frontInfoVo.setArticleNum(WebUtil.listSize(articles));
        //用户数量
        List<User> users = userService.getExistUsers();
        frontInfoVo.setUserNum(WebUtil.listSize(users));
        //分类数量
        List<Category> categories = categoryService.list();
        frontInfoVo.setCategoryNum(WebUtil.listSize(categories));
        //标签数量
        List<Tag> tags = tagService.list();
        frontInfoVo.setTagNum(WebUtil.listSize(tags));
        //评论数量
        List<Comments> comments = commentsService.list();
        frontInfoVo.setCommentNum(WebUtil.listSize(comments));
        //链接数量
        List<Links> links = linksService.list();
        frontInfoVo.setLinkNum(WebUtil.listSize(links));
        //浏览数量
        if (!CollectionUtils.isEmpty(articles)) {
            Integer viewNum = articles.stream().map(e -> Optional.ofNullable(e.getViewCount()).orElse(0))
                    .reduce(0, Integer::sum);
            //留言板的浏览量
            Article message = articleService.getById(3);
            frontInfoVo.setViewNum(viewNum+message.getViewCount());
        }else {
            frontInfoVo.setViewNum(0);
        }
        //获取前10个热门文章以及作者信息
        List<Article> hots = articles.stream().sorted(
                        Comparator.comparing(Article::getViewCount).reversed())
                .limit(10).collect(Collectors.toList());
        frontInfoVo.setHotsArticles(hots);
        //获取展示的分页文章列表
        Page<Article> articlePage = new Page<>(params.getPageNum(),params.getPageSize());
        Page<Article> page = articleService.page(articlePage,
                new LambdaQueryWrapper<Article>()
                        .eq(Article::getStatus,StatusConstant.PUBLISHED)
                        .orderByAsc(Article::getOrderBy));
        //对分页数据进行重构
        List<ArticleUserVo> articleList = page.getRecords().stream().sorted(
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
        Page<ArticleUserVo> res = new Page<>();
        res.setRecords(articleList);
        res.setTotal(articlePage.getTotal());
        frontInfoVo.setArticleList(res);
        redisUtil.setVal(RedisConstant.KYOU_FRONT,frontInfoVo,Duration.ofMinutes(20));
        return Result.success(frontInfoVo);
    }





}
