package com.kyou.blog.common.util;

import com.kyou.blog.common.constant.MenuConstant;
import com.kyou.blog.model.entity.Menu;
import com.kyou.blog.model.vo.MenuVo;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author CC
 * time 2023-07-12
 * description
 */
public class MenuHelper {

    public static List<MenuVo> buildTree(List<MenuVo> menuVos){
        if (CollectionUtils.isEmpty(menuVos)) {
           return Collections.emptyList();
        }
        return menuVos.stream().filter(menuVo -> MenuConstant.DIR_M.equals(menuVo.getType()))
                .peek(menuVo -> recursionForBuild(menuVo, menuVos)).collect(Collectors.toList());
    }
    private static void recursionForBuild(MenuVo par,List<MenuVo> menuList){
        if(par==null||CollectionUtils.isEmpty(menuList)){
            return;
        }
        List<Menu> children = par.getChildren();
        if (CollectionUtils.isEmpty(children)) {
            par.setChildren(new ArrayList<>());
        }
        menuList.forEach(menu -> {
            //子菜单的父菜单id = 父菜单的id
            if (menu.getParentId().equals(par.getId())) {
                //继续查找
                 recursionForBuild(menu,menuList);
                par.getChildren().add(menu);
            }
        });
    }






}
