package com.kyou.blog.dataService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kyou.blog.common.constant.MsgConstant;
import com.kyou.blog.common.constant.RedisConstant;
import com.kyou.blog.common.constant.StatusConstant;
import com.kyou.blog.dataService.mapper.RoleMapper;
import com.kyou.blog.dataService.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kyou.blog.model.entity.Role;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
@DubboService(interfaceClass = RoleService.class)
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Override
    public void addUserRole(Long uid, int rid) {
        baseMapper.addUserRole(uid,rid);
    }


    @Override
    public void delRoleByUid(Long id) {
        baseMapper.deleteUserRoleByUid(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserRoleByNameWithUid(Long id, List<String> roles) {
        //先删除
        baseMapper.deleteUserRoleByUid(id);
        if (!CollectionUtils.isEmpty(roles)) {
            //查询所有相关名称的id
            List<Long> rids=baseMapper.selectIdsByRName(roles);
            //后更新
            baseMapper.saveUserRoles(id,rids);
        }

    }


    @Override
    public void saveRole(@Valid Role role) {
        Role r = baseMapper.selectOne(
                new LambdaQueryWrapper<Role>()
                        .eq(Role::getName, role.getName())
                        .or()
                        .eq(Role::getKeyName, role.getKeyName())
        );
        if(r!=null){
            if(r.getKeyName().equals(role.getKeyName())){
                throw new RuntimeException(MsgConstant.ROLE_KEY_REPEATS);
            }
            if(r.getName().equals(role.getName())){
                throw new RuntimeException(MsgConstant.ROLE_NAME_REPEATS);
            }
        }
        baseMapper.insert(role);
    }

    @Override
    public void updateRole(@Valid Role role) {
        //查询角色名是否存在
        Integer c1= baseMapper.selectCount(
                new LambdaQueryWrapper<Role>()
                        .eq(Role::getName, role.getName())
                        .eq(Role::getStatus,StatusConstant.EXISTS)
                        .eq(Role::getDelFlag,StatusConstant.EXISTS)
        );
        //查询键值是否存在
        Integer c2= baseMapper.selectCount(
                new LambdaQueryWrapper<Role>()
                        .eq(Role::getKeyName, role.getKeyName())
                        .eq(Role::getStatus, StatusConstant.EXISTS)
                        .eq(Role::getDelFlag, StatusConstant.EXISTS)
        );
        if(c1>=1&&c2>=1){
            throw new RuntimeException("角色重复");
        }
        Role rr = new Role();
        rr.setId(role.getId());
        rr.setName(role.getName());
        rr.setKeyName(role.getKeyName());
        baseMapper.updateById(rr);
    }
    public List<Role> getRolesByUId(Long id){
        return baseMapper.getRolesByUser(id);
    }

    @Override
    public List<Long> selectIdsByRName(List<String> names) {
       return baseMapper.selectIdsByRName(names);
    }

    @Override
    public List<Role> listRoles(String condition) {
        List<Role> roles = baseMapper.selectList(
                new LambdaQueryWrapper<Role>()
                        .like(!StringUtils.isEmpty(condition), Role::getKeyName, condition)
                        .or()
                        .like(!StringUtils.isEmpty(condition), Role::getName, condition)
                        .orderByAsc(Role::getCreateTime)
        );
        return roles;
    }
}
