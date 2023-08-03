package com.kyou.blog.dataService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kyou.blog.model.entity.Tag;

import java.util.List;

/**
 * <p>
 * 标签表 服务类
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
public interface TagService extends IService<Tag> {

    List<Long> selectIdsByNames(List<String> tags);

    void addArticleTags(Long id, List<Long> tids);

    void delArticleTags(Long id);

    List<String> getArticleTags(Long id);

    List<String> getHotTag();
}
