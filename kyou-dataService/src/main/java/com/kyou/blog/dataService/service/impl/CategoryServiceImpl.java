package com.kyou.blog.dataService.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kyou.blog.dataService.mapper.ArticleMapper;
import com.kyou.blog.dataService.mapper.CategoryMapper;
import com.kyou.blog.dataService.service.CategoryService;
import com.kyou.blog.model.entity.Category;
import com.kyou.blog.model.vo.ArticleCategoryVo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 分类表 服务实现类
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
@DubboService(interfaceClass =CategoryService.class)
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public List<Long> getArticleCategoryIds(Long id) {
        return baseMapper.getArticleCategoryIds(id);
    }

    @Override
    public void delArticleCategories(Long id) {
        baseMapper.delArticleCategories(id);
    }

    @Override
    public void addArticleCategories(Long id, List<Long> categoryIds) {
       baseMapper.addArticleCategories(id,categoryIds);
    }

    @Override
    public List<ArticleCategoryVo> listCategroies() {
        List<Category> categories = baseMapper.selectList(null);
        return buildTree(categories);
    }
    private List<ArticleCategoryVo> buildTree(List<Category> categories){
        if (CollectionUtils.isEmpty(categories)) {
            return Collections.emptyList();
        }
        List<ArticleCategoryVo> origin = categories.stream().map(e -> {
            ArticleCategoryVo v = new ArticleCategoryVo();
            v.setId(e.getId());
            v.setPid(e.getPid());
            v.setName(e.getName());
            Integer num = articleMapper.countArticleCategoryByCid(e.getId());
            num=(num==0)?0:num;
            v.setArticleNum(num);
            return v;
        }).collect(Collectors.toList());
        List<ArticleCategoryVo> treeData = origin.stream().filter(e -> 0L == e.getPid())
                .peek(e -> findChildren(e, origin)).collect(Collectors.toList());
        return treeData;
    }
    private static void findChildren(ArticleCategoryVo par,List<ArticleCategoryVo> origin){
        if (CollectionUtils.isEmpty(par.getChildren())) {
            par.setChildren(new ArrayList<>());
        }
        origin.forEach(e->{
            if(par.getId().equals(e.getPid())){
                findChildren(e,origin);
                par.getChildren().add(e);
            }
        });
    }







}
