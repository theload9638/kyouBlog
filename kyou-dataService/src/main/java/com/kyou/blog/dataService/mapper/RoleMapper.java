package com.kyou.blog.dataService.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kyou.blog.model.entity.Role;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> getAssignRole(Long userId);
    List<Role> getRolesByUser(Long id);
    @Insert("insert into user_role (uid,rid) values (#{uid},#{rid})")
    void addUserRole(@Param("uid") Long uid, @Param("rid") int rid);
    @Delete("delete from user_role where uid=#{id}")
    void deleteUserRoleByUid(Long id);
    List<Long> selectIdsByRName(List<String> roles);

    void saveUserRoles(@Param("uid") Long id,@Param("rids") List<Long> rids);


}
