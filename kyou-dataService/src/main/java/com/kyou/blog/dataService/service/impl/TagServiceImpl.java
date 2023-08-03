package com.kyou.blog.dataService.service.impl;

import com.kyou.blog.dataService.mapper.TagMapper;
import com.kyou.blog.dataService.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kyou.blog.model.entity.Tag;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
@DubboService(interfaceClass = TagService.class)
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public List<String> getHotTag() {
        try {
            List<Long> ids=baseMapper.getHotTagIds();
            List<String> hotTag = baseMapper.getHotTag(ids);
            return hotTag;
        } catch (Exception e) {
            log.error("[获取最热标签错误]",e);
           return Collections.emptyList();
        }
    }

    @Override
    public List<String> getArticleTags(Long id) {
       return baseMapper.getArticleTags(id);
    }

    @Override
    public void delArticleTags(Long id) {
       baseMapper.delArticleTags(id);
    }

    @Override
    public void addArticleTags(Long id, List<Long> tids) {
        baseMapper.addArticleTags(id,tids);
    }

    @Override
    public List<Long> selectIdsByNames(List<String> tags) {
       return baseMapper.selectIdsByNames(tags);
    }
}
