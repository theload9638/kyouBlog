package com.kyou.blog.dataService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kyou.blog.model.dto.ArticleRankDto;
import com.kyou.blog.model.dto.PageArticleDto;
import com.kyou.blog.model.entity.Article;
import com.kyou.blog.model.entity.Category;
import com.kyou.blog.model.entity.Tag;
import com.kyou.blog.model.vo.ArticleVo;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 文章表 服务类
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
public interface ArticleService extends IService<Article> {


    List<ArticleRankDto> getRankList();

    List<Tag> getArticleTags(Long id);

    List<Category> getArticleCategories(Long id);

    List<Long> getPublishedIds(int published);

    List<Article> getPublishedArticles();
}
