package com.kyou.blog.dataService.service.impl;

import com.kyou.blog.dataService.mapper.UserRoleMapper;
import com.kyou.blog.dataService.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kyou.blog.model.entity.UserRole;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * <p>
 * 用户角色关系表 服务实现类
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
@DubboService(interfaceClass = UserRoleService.class)
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
