package com.kyou.blog.common.constant;

/**
 * @author CC
 * time 2023-07-16
 * description
 */
public class StatusConstant {
    public static final Integer ARTICLE_EDIT=1;
    public static final Integer ARTICLE_SHOW=0;
    /**
     * 评论区状态
     */
    public static final Integer OPEN_COMMENT=1;
    public static final Integer CLOSE_COMMENT=0;
    /**
     * 用户账号状态
     */
    public static final Integer ENABLED=0;
    public static final Integer DISABLE=1;
    /**
     * 逻辑删除状态
     */
    public static final String EXISTS="0";
    public static final String NOT_EXISTS="1";
    /**
     * 用户性别
     */
    public static final String MAN="1";
    public static final String WOMAN="0";
    public static final String UN_KNOW="2";
    /**
     * 文章状态
     * 0下架,1编辑中,2审核中,3已发布,4审核未通过
     */
    public static final int TAKE_DOWN=0;
    public static final int EDIT=1;
    public static final int UNDER_REVIEW=2;
    public static final int PUBLISHED=3;
    public static final int REVIEW_FAILED=4;
    /**
     * 文章是否允许评论
     * 0不允许 1允许
     */
    public static final int COMMENT_ALLOWED=1;
    public static final int COMMENT_NOT_ALLOWED=0;
    /**
     * 等级经验
     */
    public static final int EX_MIN=0;
    public static final int EX_2=3000;
    public static final int EX_3=6000;
    public static final int EX_4=9000;
    public static final int EX_5=12000;
    public static final int EX_MAX=30000;
    /**
     * 关注状态
     */
    public static final int FOLLOW=1;
    public static final int UNFOLLOW=0;

}
