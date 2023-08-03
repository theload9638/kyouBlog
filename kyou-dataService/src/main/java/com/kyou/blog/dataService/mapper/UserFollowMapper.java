package com.kyou.blog.dataService.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kyou.blog.model.entity.UserFollow;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cc
 * @since 2023-07-31
 */
public interface UserFollowMapper extends BaseMapper<UserFollow> {
    @Delete("delete from user_follow where cur_id=#{id} or fans_id=#{id}")
    void removeFollowAndFans(Long id);
    @Select("select cur_id from user_follow where fans_id=#{id}")
    List<Long> getFollowNum(Long id);
    @Select("select fans_id from user_follow where cur_id=#{id}")
    List<Long> getFansNum(Long id);
    @Delete("delete from user_follow where cur_id=#{curId} and fans_id=#{fansId}")
    void cancelFollow(UserFollow follow);
}
