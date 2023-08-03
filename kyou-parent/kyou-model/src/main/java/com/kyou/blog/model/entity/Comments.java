package com.kyou.blog.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 评论表
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
@Data
public class Comments implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 上级评论ID
     */
    private Long parentId;

    /**
     * 上级评论人昵称
     */
    private String pname;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 评论人id
     */
    private Long uid;

    /**
     * 评论人昵称
     */
    private String nickname;
    /**
     * 评论人头像
     */
    private String avatar;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 浏览器信息地址
     */
    private String address;

    /**
     * 评论人IP
     */
    private String ip;

    /**
     * 评论时间
     */
    private LocalDateTime createTime;
    /**
     * 评论点赞数
     */
    private Long likes;

}
