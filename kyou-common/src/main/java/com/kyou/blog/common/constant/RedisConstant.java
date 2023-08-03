package com.kyou.blog.common.constant;

/**
 * @author CC
 * time 2023-07-14
 * description
 */
public class RedisConstant {
    public static final String AUTHOR_INFO="author:info:";
    public static final String USER_LIST="user:list";
    public static final String USER_INFO="user:info";
    public static final String KYOU_LOG="kyou:log";
    public static final String KYOU_FRONT="kyou:front";
    public static final String KYOU_BACK="kyou:back";
    public static final String KYOU_TAGS="kyou:tags";
    public static final String KYOU_LINKS="kyou:links";
    public static final String USER_SIGN_REWARD="user:sign:reward:";
    public static final String USER_SIGN="user:sign:";
    public static final String TAG_LIMIT="tag:limit:";
    public static final String COMMENT_LIST="comment:list:";
    public static final String AUTHOR_IDS="author:ids";
    public static final String ARTICLE_PUBLISH="article:publish";
    /**
     * 文章点赞
     */
    public static final String ARTICLE_LIKE="article:like:";
    /**
     * 浏览量
     */
    public static final String ARTICLE_VIEW="article:view:";
    public static final String LIKE_COMMENT="comment:like:";
    /**
     * 每天评论新增经验3次
     */
    public static final String COMMENT_EX="comment:ex:";
    public static final long COMMENT_EX_TTL=1;
    /**
     * 文章内容图存储时间
     */
    public static final String ARTICLE_CONTENT="article:content:";
    public static final long ARTICLE_CONTENT_TTL=30;
    /**
     * 登录验证码key 和过期时间：3分钟
     */
    public static final String LOGIN_CODE="login:code:";
    public static final long LOGIN_CODE_TTL=3;
    /**
     * 缓存文件的时间：1天
     */
    public static final String CACHE_FILE="cache:file:";
    public static final long CACHE_FILE_TTL=1;
    /**
     * 真正上传的文件的缓存
     */
    public static final String CACHE_REAL_FILE="cache:real:file:";
    /**
     * 登录重试
     */
    public static final String RETRY_LOGIN="retry:login:";
    /**
     * 刷新token
     */
    public static final String REFLUSH_TOKEN="token:reflush:";
    /**
     * 刷新token的时间：30分钟
     */
    public static final long REFULSH_TOKEN_TTL=30;
    /**
     * 所有菜单项
     * 30分钟
     */
    public static final String ALL_MENUS="menu:all:";
    public static final long ALL_MENUS_TTL=30;
    /**
     * 所以角色列表
     * 30分钟
     */
    public static final String ALL_ROLES="role:all";
    public static final long ALL_ROLES_TTL=30;
    /**
     * 文章缩略图
     * 1个天
     */
    public static final String ARTICLE_CACHE_IMG="article:cache:img";
    public static final long ARTICLE_IMG_TTL=1;
    /**
     * 分类列表
     * 20分
     */
    public static final String CATEGORY_LIST="category:list";
    public static final long CATEGORY_LIST_TTL=20;
    /**
     * 分类下拉框
     */
    public static final String CATEGORY_SELECT="category:select";

}
