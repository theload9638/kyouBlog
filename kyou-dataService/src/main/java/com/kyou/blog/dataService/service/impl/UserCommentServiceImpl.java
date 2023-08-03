package com.kyou.blog.dataService.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kyou.blog.dataService.mapper.UserCommentMapper;
import com.kyou.blog.dataService.service.UserCommentService;
import com.kyou.blog.model.entity.UserComment;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cc
 * @since 2023-07-27
 */
@DubboService(interfaceClass = UserCommentService.class)
public class UserCommentServiceImpl extends ServiceImpl<UserCommentMapper, UserComment> implements UserCommentService {

}
