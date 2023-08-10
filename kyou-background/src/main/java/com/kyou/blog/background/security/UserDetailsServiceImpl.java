package com.kyou.blog.background.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kyou.blog.background.webUtil.AuthenticationContextHolder;
import com.kyou.blog.background.webUtil.RedisUtil;
import com.kyou.blog.common.constant.MsgConstant;
import com.kyou.blog.common.constant.RedisConstant;
import com.kyou.blog.common.constant.StatusConstant;
import com.kyou.blog.dataService.service.MenuService;
import com.kyou.blog.dataService.service.UserService;
import com.kyou.blog.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;


/**
 * @author CC
 * time 2023-07-14
 * description  给安全框架注册用户信息
 */
@Slf4j
@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
    @DubboReference(interfaceClass = UserService.class)
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private int maxRetry=3;
    private int lockTime=30;
    @DubboReference(interfaceClass = MenuService.class)
    private MenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       log.info("进入[UserDetailsService]的loadUserByUsername方法");
        User user = userService.getOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, username)
                        .or()
                        .eq(User::getEmail, username)
        );
        validate(user,username);
        return createUser(user);
    }

    public void validate(User user,String inputName) {
        if (user==null) {
            log.error("登录用户：{} 不存在.", inputName);
            throw new UsernameNotFoundException("登录用户：" + inputName + " 不存在");
        }else if(StatusConstant.DISABLE.equals(user.getStatus())){
            log.info("登录用户：{} 已被停用.", inputName);
            throw new RuntimeException("对不起，您的账号：" + inputName + " 已停用");
        }else if(StatusConstant.NOT_EXISTS.equals(user.getDelFlag())){
            log.info("登录用户：{} 已被删除.", inputName);
            throw new RuntimeException("对不起，您的账号：" + inputName + " 已被删除");
        }
        Authentication usernamePasswordAuthenticationToken = AuthenticationContextHolder.getContext();
        String password = usernamePasswordAuthenticationToken.getCredentials().toString();
        AuthenticationContextHolder.clearContext();
        String val = redisUtil.getVal(RedisConstant.RETRY_LOGIN + user.getId());
        Integer retryCount;
        if(val==null||"{}".equals(val)){
            retryCount = 0;
        }else {
            retryCount=Integer.parseInt(val);
        }
        //超过重试次数
        if (retryCount >= maxRetry) {
            log.info(MsgConstant.RETRY_MAX_EXCEED, maxRetry,lockTime);
            redisUtil.setVal(RedisConstant.RETRY_LOGIN+user.getId(),retryCount, Duration.ofMinutes(lockTime));
            throw new RuntimeException("账号已锁定，锁定时间--》"+lockTime+"分钟");
        }
        //密码比对
        if (!matches(user, password)) {
            log.info("user.password.retry.limit.count", retryCount);
            redisUtil.val().increment(RedisConstant.RETRY_LOGIN+user.getId());
            throw new RuntimeException("密码输入错误!");
        }
        redisUtil.del(RedisConstant.RETRY_LOGIN + user.getId());
    }
    public UserDetails createUser(User user){
        return new SysUser(user,menuService.getMenuPermission(user));
    }
    public boolean matches(User user, String password) {
        return passwordEncoder.matches( password,user.getPassword());
    }


}
