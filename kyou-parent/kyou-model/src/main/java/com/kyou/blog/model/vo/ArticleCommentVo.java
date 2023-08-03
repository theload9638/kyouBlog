package com.kyou.blog.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author CC
 * time 2023-07-31
 * description
 */
@Data
public class ArticleCommentVo implements Serializable {

    public static final long serialVersionUID=1L;

    private Long id;
    private Long articleId;
    private Long userId;
    private String title;
    private String content;
    private String ip;
    private String address;
    private String publishTime;
    private Long likes;

}
