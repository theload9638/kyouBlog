package com.kyou.blog.dataService.service.impl;

import com.kyou.blog.dataService.mapper.RoleMenuMapper;
import com.kyou.blog.dataService.service.RoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kyou.blog.model.entity.RoleMenu;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * <p>
 * 角色菜单关系表 服务实现类
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
@DubboService(interfaceClass =RoleMenuService.class)
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}
