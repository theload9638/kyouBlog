package com.kyou.blog.dataService.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kyou.blog.common.constant.StatusConstant;
import com.kyou.blog.dataService.mapper.ArticleMapper;
import com.kyou.blog.dataService.mapper.CategoryMapper;
import com.kyou.blog.dataService.mapper.TagMapper;
import com.kyou.blog.dataService.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kyou.blog.model.dto.ArticleRankDto;
import com.kyou.blog.model.entity.Article;
import com.kyou.blog.model.entity.Category;
import com.kyou.blog.model.entity.Tag;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 文章表 服务实现类
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
@DubboService(interfaceClass =ArticleService.class)
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    public List<Article> getPublishedArticles() {
       return baseMapper.selectList(
                new LambdaQueryWrapper<Article>()
                        .eq(Article::getStatus, StatusConstant.PUBLISHED)
        );
    }

    @Override
    public List<Long> getPublishedIds(int published) {
       return baseMapper.getPublishedIds(published);
    }

    @Override
    public List<Category> getArticleCategories(Long id) {
        List<Long> cids= baseMapper.getCidsByAid(id);
        if (!CollectionUtils.isEmpty(cids)) {
            return categoryMapper.getCategoriesByCids(cids);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Tag> getArticleTags(Long id) {
        List<Long> tids=baseMapper.getTidsByAid(id);
        if (!CollectionUtils.isEmpty(tids)) {
            return tagMapper.getTagsByTids(tids);
        }
        return Collections.emptyList();
    }

    @Override
    public List<ArticleRankDto> getRankList() {
       return baseMapper.getRankList();
    }
}
