package com.kyou.blog.dataService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kyou.blog.model.entity.UserFollow;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cc
 * @since 2023-07-31
 */
public interface UserFollowService extends IService<UserFollow> {

    void removeFollowAndFans(Long id);

    List<Long> getFollowNum(Long id);

    List<Long> getFansNum(Long id);

    void cancelFollow(UserFollow follow);
}
