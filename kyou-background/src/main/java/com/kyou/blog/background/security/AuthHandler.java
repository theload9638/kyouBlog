package com.kyou.blog.background.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


/**
 * @author CC
 * time 2023-07-19
 * description
 */
@Slf4j
@Component("auth")
public class AuthHandler {

    /** 所有权限标识 */
    private static final String ALL_PERMISSION = "*:*:*";
    /** 管理员角色权限标识 */
    private static final String SUPER_ADMIN = "admin";

    public boolean hasPerms(String perms){
        log.info("校验权限");
        if (!StringUtils.hasText(perms)) {
            log.info("权限不足，无法访问路径");
            return false;
        }
        SysUser principal = AuthHandler.getLoginUser();
        if (principal.getPerms().contains(ALL_PERMISSION)) {
            return true;
        }
        if (principal.getPerms().contains(perms)) {
           return true;
        }
        log.info("权限不足，无法访问路径");
        return false;
    }
    public boolean isAdmin(){
        log.info("校验角色");
        SysUser loginUser = AuthHandler.getLoginUser();
        if ("admin".equals(loginUser.getUsername())) {
            return true;
        }
        return false;
    }
    public boolean hasAnyPerms(String perms){
        log.info("校验权限");
        if (!StringUtils.hasText(perms)) {
            log.info("权限不足，无法访问路径");
            return false;
        }
        SysUser principal = AuthHandler.getLoginUser();
        if (principal.getPerms().contains(ALL_PERMISSION)) {
            return true;
        }
        String[] split = perms.split(",");
        for (String s : split) {
            if(principal.getPerms().contains(s)){
               return true;
            }
        }
        log.info("权限不足，无法访问路径");
        return false;
    }
    public boolean authenticate(){
        SysUser loginUser = getLoginUser();
        return loginUser!=null;
    }

    public static SysUser getLoginUser(){
        try {
            return ((SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }








}
