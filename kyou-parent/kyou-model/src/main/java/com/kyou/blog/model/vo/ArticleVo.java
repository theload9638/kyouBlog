package com.kyou.blog.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author CC
 * time 2023-07-26
 * description
 */
@Data
public class ArticleVo implements Serializable {

    public static final long serialVersionUID=1L;
    private Long id;
    private String author;
    private String summary;
    private String title;
    private Integer comment;
    private Integer viewNum;
    private LocalDateTime publishDate;
}
