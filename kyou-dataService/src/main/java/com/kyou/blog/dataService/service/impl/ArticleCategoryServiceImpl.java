package com.kyou.blog.dataService.service.impl;

import com.kyou.blog.dataService.mapper.ArticleCategoryMapper;
import com.kyou.blog.dataService.service.ArticleCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kyou.blog.model.entity.ArticleCategory;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * <p>
 * 文章分类表 服务实现类
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */

@DubboService(interfaceClass = ArticleCategoryService.class)
public class ArticleCategoryServiceImpl extends ServiceImpl<ArticleCategoryMapper, ArticleCategory> implements ArticleCategoryService {

}
