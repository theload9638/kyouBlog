package com.kyou.blog.background.web;


import cn.hutool.json.JSONUtil;

import com.kyou.blog.background.webUtil.WebUtil;
import com.kyou.blog.common.Result;
import com.kyou.blog.common.annotation.Log;
import com.kyou.blog.common.constant.RedisConstant;

import com.kyou.blog.common.emuration.OperationType;
import com.kyou.blog.common.util.SysContext;
import com.kyou.blog.model.entity.Role;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.Duration;
import java.util.List;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
@Api(tags = "角色相关接口")
@RestController
@RequestMapping("/sys/role")
public class RoleController extends BaseController{

    @ApiOperation("展示角色列表")
    @GetMapping("/list")
    @PreAuthorize("@auth.authenticate()")
    public Result<List<Role>> list(String condition){
        List<Role> list = roleService.listRoles(condition);
        return Result.success(list);
    }
    @DeleteMapping("/{id}")
    @ApiOperation("删除角色")
    @Log(value = OperationType.DELETE,title = "角色模块")
    @PreAuthorize("@auth.hasPerms('setting:role:remove')")
    public Result del(@PathVariable @Min(value = 0,message = "编号不能为负数") Long id){
        if(id==1){
            return Result.serverError("无法删除超级管理员");
        }
        roleService.removeById(id);
        clearRoleCache();
        return Result.success();
    }
    @PutMapping
    @ApiOperation("更新角色")
    @Log(value = OperationType.UPDATE,title = "角色模块")
    @PreAuthorize("@auth.hasPerms('setting:role:edit')")
    public Result updRole(@RequestBody @Valid Role role){
        roleService.updateRole(role);
        clearRoleCache();
        return Result.success();
    }
    @PostMapping("/add")
    @ApiOperation("新增角色")
    @Log(value = OperationType.INSERT,title = "角色模块")
    @PreAuthorize("@auth.hasPerms('setting:role:add')")
    public Result addRole(@RequestBody @Valid Role role){
        roleService.saveRole(role);
        clearRoleCache();
        return Result.success();
    }

    @ApiOperation("是否是审核员或者admin")
    @GetMapping("/isAudit")
    @PreAuthorize("@auth.authenticate()")
    public Result isAudit()
    {
        List<Role> roles = roleService.getRolesByUId(SysContext.getUserId());
        boolean b = roles.stream().
                anyMatch(role -> role.getKeyName().equals("admin")
                        || role.getKeyName().equals("checker"));
        return Result.success(b);
    }

    public void clearRoleCache(){
    }

}

