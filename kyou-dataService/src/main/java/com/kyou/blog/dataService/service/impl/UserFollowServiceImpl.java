package com.kyou.blog.dataService.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kyou.blog.dataService.mapper.UserFollowMapper;
import com.kyou.blog.dataService.service.UserFollowService;
import com.kyou.blog.model.entity.UserFollow;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cc
 * @since 2023-07-31
 */
@DubboService(interfaceClass = UserFollowService.class)
public class UserFollowServiceImpl extends ServiceImpl<UserFollowMapper, UserFollow> implements UserFollowService {

    @Override
    public void cancelFollow(UserFollow follow) {
        baseMapper.cancelFollow(follow);
    }

    @Override
    public List<Long> getFansNum(Long id) {
        List<Long> fn = baseMapper.getFansNum(id);
        return  fn==null? Collections.emptyList():fn;
    }

    @Override
    public List<Long> getFollowNum(Long id) {
        List<Long> fn= baseMapper.getFollowNum(id);
        return fn==null? Collections.emptyList():fn;
    }

    @Override
    public void removeFollowAndFans(Long id) {
        baseMapper.removeFollowAndFans(id);
    }
}
