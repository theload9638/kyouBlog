package com.kyou.blog.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author CC
 * time 2023-07-26
 * description
 */
@Data
public class ArticleRankDto implements Serializable {

    public static final long serialVersionUID=1L;
    private Integer id;
    private Integer rankId;
    private String username;
    private Integer articleNum;
    private LocalDateTime registerTime;

}
