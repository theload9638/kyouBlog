package com.kyou.blog.model.vo;

import com.kyou.blog.model.entity.Category;
import com.kyou.blog.model.entity.Tag;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author CC
 * time 2023-07-28
 * description
 */
@Data

public class ArticleTCVo implements Serializable {

    public static final long serialVersionUID=1L;

    private Long id;
    private List<Tag> tags;
    private List<Category> categories;

}
