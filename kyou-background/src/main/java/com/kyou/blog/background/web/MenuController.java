package com.kyou.blog.background.web;

import com.kyou.blog.background.security.AuthHandler;
import com.kyou.blog.background.security.SysUser;
import com.kyou.blog.background.webUtil.RedisUtil;
import com.kyou.blog.common.Result;
import com.kyou.blog.common.annotation.Log;
import com.kyou.blog.common.constant.MsgConstant;
import com.kyou.blog.common.emuration.OperationType;
import com.kyou.blog.common.util.MenuHelper;
import com.kyou.blog.dataService.service.MenuService;
import com.kyou.blog.dataService.service.RoleService;
import com.kyou.blog.model.entity.Menu;
import com.kyou.blog.model.entity.Role;
import com.kyou.blog.model.vo.MenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
@Api(tags = "菜单相关接口")
@RestController
@RequestMapping("/sys/menu")
public class MenuController {
    @DubboReference(interfaceClass = MenuService.class)
    private MenuService menuService;
    @DubboReference(interfaceClass = RoleService.class)
    private RoleService roleService;
    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation("获取所有树形菜单")
    @GetMapping("/getMenus")
    @PreAuthorize("@auth.authenticate()")
    public Result<List<MenuVo>> getMenus()
    {
        SysUser loginUser = AuthHandler.getLoginUser();
        Long id = loginUser.getId();
        List<Menu> menus;
        if (loginUser.getUsername().equals("admin") || id==1) {
            menus = menuService.list(null);
        }else{
            List<Role> roles = roleService.getRolesByUId(id);
            List<Long> rids = roles.stream().map(Role::getId).
                    collect(Collectors.toList());
            menus=menuService.getMenuByRids(rids);
        }
        List<MenuVo> menuVos = menus.stream().map(menu -> {
            MenuVo menuVo = new MenuVo();
            BeanUtils.copyProperties(menu, menuVo);
            return menuVo;
        }).collect(Collectors.toList());
        List<MenuVo> menuTree = MenuHelper.buildTree(menuVos);
        return Result.success(menuTree);
    }
    @ApiOperation("删除单个菜单")
    @DeleteMapping("/{id}")
    @Log(value = OperationType.DELETE,title = "菜单模块")
    @PreAuthorize("@auth.hasPerms('setting:menu:remove')")
    public Result delMenu(@PathVariable Long id){
        menuService.delMenu(id);
        return Result.success(MsgConstant.DEL_MENU_SUC);
    }
    @ApiOperation("新增菜单")
    @PutMapping
    @Log(value = OperationType.INSERT,title = "菜单模块")
    @PreAuthorize("@auth.hasPerms('setting:menu:add')")
    public Result addMenu(@RequestBody Menu menu){
        menuService.save(menu);
        return Result.success(MsgConstant.ADD_MENU_SUC);
    }
    /**
     * 没有子菜单的菜单信息
     * @param id
     * @return
     */
    @ApiOperation("获取菜单基本信息")
    @GetMapping("/{id}")
    @PreAuthorize("@auth.hasPerms('setting:menu:list')")
    public Result<Menu> getMenuInfo(@PathVariable Long id){
        Menu menu=menuService.getMenuInfo(id);
        return Result.success(menu);
    }
    @ApiOperation("修改菜单")
    @PostMapping
    @Log(value = OperationType.UPDATE,title = "菜单模块")
    @PreAuthorize("@auth.hasPerms('setting:menu:edit')")
    public Result updateMenu(@RequestBody Menu menu){
         menuService.updateById(menu);
        return Result.success(MsgConstant.UPDATE_MENU_SUC);
    }

    /**
     * 具有子菜单的菜单信息
     * @param menu
     * @return
     */
    @ApiOperation("获取菜单详细信息")
    @GetMapping("/info")
    @PreAuthorize("@auth.hasPerms('setting:menu:list')")
    public Result<MenuVo>  getInfo(Menu menu){
        MenuVo menuVo=menuService.getInfo(menu);
        return Result.success(menuVo);
    }
    @ApiOperation("根据角色id获取菜单权限")
    @GetMapping("/getPerms")
    @PreAuthorize("@auth.hasPerms('setting:menu:edit')")
    public Result getPerms(@NotNull(message = "角色id不能为空")
                               @Min(value = 1,message = "请求数据异常，标识不能为负数") Long id){
        List<Menu> perms = menuService.getMenuByRids(Arrays.asList(id));
        return Result.success(perms);
    }
    @ApiOperation("为角色分配菜单权限")
    @PostMapping("/assignPerms/{rid}")
    @Log(value = OperationType.UPDATE,title = "菜单模块")
    @PreAuthorize("@auth.isAdmin()")
    public Result assign(@PathVariable
                             @NotNull(message ="分配权限失败")
                             @Min(value = 1,message = "分配权限失败") Long rid,
                         @RequestBody @NotNull List<Long> mids){
        menuService.assign(rid,mids);
        return Result.success();
    }


}

