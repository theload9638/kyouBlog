package com.kyou.blog.dataService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kyou.blog.model.entity.Role;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
public interface RoleService extends IService<Role> {
    /**
     * 查询id集合通过角色名称
     * @param names
     * @return
     */
    List<Long> selectIdsByRName(List<String> names);

    List<Role> listRoles(String condition);
    public List<Role> getRolesByUId(Long id);

    void updateRole(Role role);

    void saveRole(Role role);

    void addUserRole(Long uid, int rid);


    void updateUserRoleByNameWithUid(Long id, List<String> roles);


    void delRoleByUid(Long id);

}

