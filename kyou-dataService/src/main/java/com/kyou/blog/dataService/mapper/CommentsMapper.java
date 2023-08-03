package com.kyou.blog.dataService.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kyou.blog.model.entity.Comments;

import java.util.List;

/**
 * <p>
 * 评论表 Mapper 接口
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
public interface CommentsMapper extends BaseMapper<Comments> {

    void removeByAIds(List<Long> ids);
}
