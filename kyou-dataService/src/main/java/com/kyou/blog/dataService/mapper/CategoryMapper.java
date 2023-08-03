package com.kyou.blog.dataService.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kyou.blog.model.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 分类表 Mapper 接口
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
public interface CategoryMapper extends BaseMapper<Category> {

    void addArticleCategories(@Param("aid") Long id,
                              @Param("cids") List<Long> categoryIds);
    @Delete("delete from article_category where aid=#{id}")
    void delArticleCategories(Long id);

    List<Long> getArticleCategoryIds(Long id);

    List<Category> getCategoriesByCids(List<Long> cids);
}
