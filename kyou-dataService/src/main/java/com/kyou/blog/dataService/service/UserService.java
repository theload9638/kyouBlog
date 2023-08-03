package com.kyou.blog.dataService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kyou.blog.model.dto.PageUserDto;
import com.kyou.blog.model.entity.User;
import com.kyou.blog.model.vo.CodeVo;
import com.kyou.blog.model.vo.LoginVo;
import com.kyou.blog.model.vo.PageVo;
import com.kyou.blog.model.vo.UserVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
public interface UserService extends IService<User> {


    UserVo getWithRole(Long userId);


    PageVo<UserVo> listWithRoles(PageUserDto query);

    List<UserVo> getLastRegisterUsers();

    List<Long> getRegisterUserId();

    List<User> getExistUsers();
}


