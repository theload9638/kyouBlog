package com.kyou.blog.model.vo;

import com.kyou.blog.model.entity.Article;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author CC
 * time 2023-08-02
 * description
 */
@Data
public class SearchVo implements Serializable {

    public static final long serialVersionUID=1L;

    private List<Article> articles;
    private List<String> tagNames;

}
