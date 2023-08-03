package com.kyou.blog.model.vo;

import com.kyou.blog.model.entity.Article;
import lombok.Data;

import java.io.Serializable;

/**
 * @author CC
 * time 2023-07-26
 * description
 */
@Data
public class ArticleUserVo extends Article implements Serializable {

    public static final long serialVersionUID=1L;

    private UserVo userVo;

}
