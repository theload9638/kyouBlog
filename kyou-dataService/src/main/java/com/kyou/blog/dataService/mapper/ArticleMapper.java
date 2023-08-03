package com.kyou.blog.dataService.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kyou.blog.model.dto.ArticleRankDto;
import com.kyou.blog.model.entity.Article;
import com.kyou.blog.model.entity.Tag;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 文章表 Mapper 接口
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
public interface ArticleMapper extends BaseMapper<Article> {
    @Select("select  count(1) from article_category where cid=#{id}")
    Integer countArticleCategoryByCid(Long id);

    List<ArticleRankDto> getRankList();

    @Select("select * from article_tag where aid=#{id}")
    List<Long> getTidsByAid(Long id);
    @Select("select cid from article_category where aid=#{id}")
    List<Long> getCidsByAid(Long id);
    @Select("select id from article where status=#{published}")
    List<Long> getPublishedIds(int published);
}

