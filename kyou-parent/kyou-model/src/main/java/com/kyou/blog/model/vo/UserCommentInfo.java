package com.kyou.blog.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author CC
 * time 2023-07-27
 * description
 */
@Data
public class UserCommentInfo implements Serializable {

    public static final long serialVersionUID=1L;
    private Long id;
    private String username;
    private String avatar;
    private Integer level;
    private Set<String> likeIds;


}
