package com.kyou.blog.background.security;

import cn.hutool.json.JSONUtil;
import com.kyou.blog.background.properties.CCProperties;
import com.kyou.blog.background.webUtil.RedisUtil;
import com.kyou.blog.background.webUtil.WebUtil;
import com.kyou.blog.common.Result;
import com.kyou.blog.common.constant.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author CC
 * time 2023-07-19
 * description
 * 由AuthenticationSuccessHandler调用onAuthenticationSuccess方法
 */
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;
     @Autowired
     private CCProperties ccProperties;
     @Autowired
     private RedisUtil redisUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        SysUser principal = (SysUser) authentication.getPrincipal();
        Map<String,Object> map=new HashMap<>(4);
        Long id = principal.getId();
        map.put("id",id);
        String jwt=WebUtil.createJWT(ccProperties.getTokenSecret(),
                map, ccProperties.getTokenExpire());;
        redisUtil.setVal(RedisConstant.REFLUSH_TOKEN+id,"0",
                Duration.ofMinutes(RedisConstant.REFULSH_TOKEN_TTL));
        response.addHeader("Authorization",jwt);
        PrintWriter writer = response.getWriter();
        writer.write(JSONUtil.toJsonStr(Result.success()));
    }
}
