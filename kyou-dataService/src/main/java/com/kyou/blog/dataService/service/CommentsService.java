package com.kyou.blog.dataService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kyou.blog.model.entity.Comments;

import java.util.List;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
public interface CommentsService extends IService<Comments> {

    void removeByAIds(List<Long> ids);
}
