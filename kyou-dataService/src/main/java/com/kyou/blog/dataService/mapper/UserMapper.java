package com.kyou.blog.dataService.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kyou.blog.model.entity.User;
import com.kyou.blog.model.vo.UserVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
public interface UserMapper extends BaseMapper<User> {

    UserVo getWithRole(Long userId);

    List<UserVo> getLastRegisterUsers();
    @Select("select id from user")
    List<Long> getRegisterUserId();
}

