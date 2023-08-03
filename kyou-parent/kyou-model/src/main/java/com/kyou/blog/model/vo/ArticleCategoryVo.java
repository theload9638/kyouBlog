package com.kyou.blog.model.vo;

import com.kyou.blog.model.entity.Article;
import com.kyou.blog.model.entity.Category;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author CC
 * time 2023-07-24
 * description
 */
@Data
public class ArticleCategoryVo implements Serializable {
    public static final long serialVersionUID=1L;

    private Long id;
    private String name;
    private Integer articleNum;
    private Long pid;
    private List<ArticleCategoryVo> children;



}
