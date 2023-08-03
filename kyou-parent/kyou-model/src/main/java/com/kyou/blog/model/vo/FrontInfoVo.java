package com.kyou.blog.model.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kyou.blog.model.entity.Article;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author CC
 * time 2023-07-26
 * description
 */
@Data
public class FrontInfoVo implements Serializable {

    public static final long serialVersionUID=1L;

    private Integer articleNum;
    private Integer userNum;
    private Integer categoryNum;
    private Integer tagNum;
    private Integer commentNum;
    private Integer linkNum;
    private Integer viewNum;
    private List<Article> hotsArticles;
    private Page<ArticleUserVo> articleList;



}
