package com.kyou.blog.dataService.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kyou.blog.model.entity.Menu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
public interface MenuMapper extends BaseMapper<Menu> {

    Set<String> selectMenuPersByRid(Long id);

    List<Menu> getMenuByRids(List<Long> rids);

    void assign(@Param("rid") Long rid,
                @Param("mids") List<Long> mids);
    @Delete("delete from role_menu where rid=#{rid}")
    void delPerms(Long rid);
}
