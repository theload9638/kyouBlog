package com.kyou.blog.dataService.service.impl;

import com.kyou.blog.dataService.mapper.CommentsMapper;
import com.kyou.blog.dataService.service.CommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kyou.blog.model.entity.Comments;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
@DubboService(interfaceClass = CommentsService.class)
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments> implements CommentsService {

    @Override
    public void removeByAIds(List<Long> ids) {
        baseMapper.removeByAIds(ids);
    }
}
