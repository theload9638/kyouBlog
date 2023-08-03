package com.kyou.blog.dataService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kyou.blog.model.entity.Category;
import com.kyou.blog.model.vo.ArticleCategoryVo;

import java.util.List;

/**
 * <p>
 * 分类表 服务类
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
public interface CategoryService extends IService<Category> {

    List<ArticleCategoryVo> listCategroies();

    void addArticleCategories(Long id, List<Long> categoryIds);

    void delArticleCategories(Long id);

    List<Long> getArticleCategoryIds(Long id);

}


