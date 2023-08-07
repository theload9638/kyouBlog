package com.kyou.blog.background.web;

import com.kyou.blog.background.webUtil.RedisUtil;
import com.kyou.blog.dataService.service.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author CC
 * time 2023-08-07
 * description
 */
@Component
public class BaseController {

    @DubboReference(interfaceClass = ArticleService.class)
    protected ArticleService articleService;
    @DubboReference(interfaceClass = TagService.class)
    protected TagService tagService;
    @DubboReference(interfaceClass = UserService.class)
    protected UserService userService;
    @DubboReference(interfaceClass = CategoryService.class)
    protected CategoryService categoryService;
    @DubboReference(interfaceClass = CommentsService.class)
    protected CommentsService commentsService;
    @DubboReference(interfaceClass = LinksService.class)
    protected LinksService linksService;
    @Autowired
    protected RedisUtil redisUtil;
    @DubboReference(interfaceClass = RoleService.class)
    protected RoleService roleService;
    @DubboReference(interfaceClass = UserFollowService.class)
    protected UserFollowService userFollowService;
    @DubboReference(interfaceClass = UserCommentService.class)
    protected UserCommentService userCommentService;
    @DubboReference(interfaceClass = SysLogService.class)
    protected SysLogService sysLogService;
    @DubboReference(interfaceClass = MenuService.class)
    protected MenuService menuService;
}
