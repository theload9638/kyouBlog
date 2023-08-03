package com.kyou.blog.dataService.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kyou.blog.model.entity.Tag;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 标签表 Mapper 接口
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
public interface TagMapper extends BaseMapper<Tag> {

    List<Long> selectIdsByNames(List<String> tags);

    void addArticleTags(@Param("aid") Long id,
                        @Param("tids") List<Long> tids);
    @Delete("delete from article_tag where aid=#{id}")
    void delArticleTags(Long id);

    List<String> getArticleTags(Long id);

    List<Tag> getTagsByTids(List<Long> tids);

    List<String> getHotTag( List<Long> ids);
    @Select("SELECT tid from article_tag GROUP BY tid ORDER BY tid desc limit 5")
    List<Long> getHotTagIds();
}

