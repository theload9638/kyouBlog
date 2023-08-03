package com.kyou.blog.dataService.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kyou.blog.common.constant.MsgConstant;
import com.kyou.blog.common.exception.DelDataErr;
import com.kyou.blog.common.exception.DelMenuAndSubMenuErr;
import com.kyou.blog.common.exception.QueryParamException;

import com.kyou.blog.dataService.mapper.MenuMapper;
import com.kyou.blog.dataService.mapper.RoleMapper;
import com.kyou.blog.dataService.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kyou.blog.model.entity.Menu;
import com.kyou.blog.model.entity.Role;
import com.kyou.blog.model.entity.User;
import com.kyou.blog.model.vo.MenuVo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
@DubboService(interfaceClass = MenuService.class)
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    private RoleMapper roleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assign(Long rid, List<Long> mids) {
        baseMapper.delPerms(rid);
        baseMapper.assign(rid,mids);
    }

    @Override
    public List<Menu> getMenuByRids(List<Long> rids) {
        return baseMapper.getMenuByRids(rids);
    }

    @Override
    public List<String> getMenuPermission(User user) {
        List<String> perms=new ArrayList<>();
        if(user.getUsername().equals("admin")){
            perms.add("*:*:*");
            return perms;
        }else {
            List<Role> roles=roleMapper.getRolesByUser(user.getId());
            for (Role role : roles) {
                Set<String> pers=baseMapper.selectMenuPersByRid(role.getId());
                perms.addAll(pers);
            }
            if(CollectionUtils.isEmpty(perms)){
                return new ArrayList<>();
            }
            return perms.stream().distinct().collect(Collectors.toList());
        }
    }

    @Override
    public MenuVo getInfo(Menu menu) {
        if (menu==null) {
            throw new QueryParamException();
        }
        List<Menu> children = baseMapper.selectList(
                new LambdaQueryWrapper<Menu>()
                        .eq(Menu::getParentId, menu.getId())
        );
        List<Menu> main = baseMapper.selectList(
                new LambdaQueryWrapper<Menu>()
                        .eq(Menu::getId,menu.getId())
        );
        if (CollectionUtils.isEmpty(main)) {
            throw new RuntimeException(MsgConstant.DATA_NO_EXIST_ERR);
        }
        MenuVo menuVo = new MenuVo();
        Menu x = main.get(0);
        BeanUtils.copyProperties(x,menuVo);
        menuVo.setChildren(children);
        return menuVo;
    }

    @Override
    public Menu getMenuInfo(Long id) {
        if (!(id!=null&&id.doubleValue()>0.0)) {
            throw new QueryParamException();
        }
        return baseMapper.selectById(id);
    }

    @Override
    public void delMenu(Long id) {
        if(id==null||id<0){
            throw new QueryParamException();
        }
        List<Menu> menus = baseMapper.selectList(
                new LambdaQueryWrapper<Menu>()
                        .eq(Menu::getParentId, id)
        );
        if (!CollectionUtils.isEmpty(menus)) {
            throw new DelMenuAndSubMenuErr();
        }
        int i = baseMapper.deleteById(id);
        if (i<0) {
            throw new DelDataErr();
        }
    }


}
