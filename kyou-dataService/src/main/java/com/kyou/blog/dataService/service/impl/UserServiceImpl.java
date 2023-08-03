package com.kyou.blog.dataService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kyou.blog.common.constant.StatusConstant;
import com.kyou.blog.dataService.mapper.UserMapper;
import com.kyou.blog.dataService.service.RoleService;
import com.kyou.blog.dataService.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kyou.blog.model.dto.PageUserDto;
import com.kyou.blog.model.entity.Role;
import com.kyou.blog.model.entity.User;
import com.kyou.blog.model.vo.PageVo;
import com.kyou.blog.model.vo.UserVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
@DubboService(interfaceClass = UserService.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @DubboReference(interfaceClass = RoleService.class)
    private RoleService roleService;

    @Override
    public List<User> getExistUsers() {
       return baseMapper.selectList(
               new LambdaQueryWrapper<User>()
                       .eq(User::getDelFlag, StatusConstant.EXISTS)
       );
    }

    @Override
    public List<Long> getRegisterUserId() {
       return baseMapper.getRegisterUserId();
    }

    @Override
    public List<UserVo> getLastRegisterUsers() {
       return baseMapper.getLastRegisterUsers();
    }

    @Override
    public PageVo<UserVo> listWithRoles(PageUserDto query) {
        Page<User> userPage = baseMapper.selectPage(new Page<User>(query.getPageNum(), query.getPageSize())
                , new LambdaQueryWrapper<User>().
          eq(!StringUtils.isEmpty(query.getStatus()),User::getStatus, query.getStatus())
        .eq(!StringUtils.isEmpty(query.getGender()),User::getGender, query.getGender())
                        //模糊用户名
         .like(!StringUtils.isEmpty(query.getKey()), User::getUsername, query.getKey()).
                        //模糊邮箱
          or().like(!StringUtils.isEmpty(query.getKey()), User::getEmail, query.getKey()).
                        //模糊昵称
          or().like(!StringUtils.isEmpty(query.getKey()), User::getNickname, query.getKey()));
        List<UserVo> list = userPage.getRecords().stream().map(user -> {
            UserVo userVo = new UserVo();
            List<Role> roles = roleService.getRolesByUId(user.getId());
            BeanUtils.copyProperties(user, userVo);
            userVo.setRoles(roles);
            return userVo;
        }).collect(Collectors.toList());
        PageVo<UserVo> userVoPageVo = new PageVo<>();
        userVoPageVo.setRecords(list);
        userVoPageVo.setTotal(userPage.getTotal());
        return userVoPageVo;
    }

    @Override
    public UserVo getWithRole(Long userId) {
       return baseMapper.getWithRole(userId);
    }


}
