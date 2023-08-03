package com.kyou.blog.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author CC
 * time 2023-07-27
 * description
 */
@Data
public class CommentsVo implements Serializable {

    private static final long serialVersionUID=1L;

    private Long id;
    private Long parentId;
    private Long articleId;
    private Long uid;
    private String address;
    private String content;
    private Long likes;
    private LocalDateTime createTime;
    private String username;
    private String avatar;
    private Integer level;
    private String homeLink;
    private List<CommentsVo> list;
    private Integer total;


}
