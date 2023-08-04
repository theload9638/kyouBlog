package com.kyou.blog.background.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyou.blog.background.properties.CCProperties;
import com.kyou.blog.background.webUtil.RedisUtil;
import com.kyou.blog.background.webUtil.WebUtil;
import com.kyou.blog.dataService.service.MenuService;
import com.kyou.blog.dataService.service.UserService;
import com.kyou.blog.model.entity.User;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CC
 * time 2023-07-19
 * description
 */
@Slf4j
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter implements Serializable {
    @Autowired
    private CCProperties ccProperties;
    @DubboReference(interfaceClass = UserService.class)
    private UserService userService;
    @DubboReference(interfaceClass = MenuService.class)
    private MenuService menuService;
    @Autowired
    private RedisUtil redisUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        log.info("进入[TokenAuthenticationFilter]过滤链-->本次请求--》{}",uri);
        try {
            String authorization = request.getHeader("Authorization");
            if(!StringUtils.isEmpty(authorization)){
                Claims claims = WebUtil.parseJWT(ccProperties.getTokenSecret(),
                        authorization);
                Long id = claims.get("id", Long.class);

                SysUser loginUser = getLoginUser(id);
                //用于进行用户名密码认证并设置授权信息的
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(loginUser,
                                null, loginUser.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
           log.info("[TokenAuthenticationFilter]中本次请求token校验失败-->{}",uri);
           log.error("异常信息：{}",e.getMessage());
        }
        filterChain.doFilter(request,response);
    }

    private SysUser getLoginUser(Long id){
        User user = userService.getById(id);
        List<String> permission = menuService.getMenuPermission(user);
        SysUser loginUser = new SysUser(user,permission);
        return loginUser;
    }

}
