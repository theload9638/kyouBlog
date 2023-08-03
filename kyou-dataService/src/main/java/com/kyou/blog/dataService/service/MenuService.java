package com.kyou.blog.dataService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kyou.blog.model.entity.Menu;
import com.kyou.blog.model.entity.User;
import com.kyou.blog.model.vo.MenuVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */

public interface MenuService extends IService<Menu> {
    /**
     * 获取树形菜单
     * @return
     */

    void delMenu(Long id);

    Menu getMenuInfo(Long id);

    MenuVo getInfo(Menu menu);

    List<String> getMenuPermission(User user);

    List<Menu> getMenuByRids(List<Long> rids);

    void assign(Long rid, List<Long> mids);

}

