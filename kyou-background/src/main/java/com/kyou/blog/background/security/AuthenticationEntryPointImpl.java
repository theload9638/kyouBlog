package com.kyou.blog.background.security;

import cn.hutool.json.JSONUtil;
import com.kyou.blog.common.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author CC
 * time 2023-07-19
 * description
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = -8970718410437077606L;
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse res,
                         AuthenticationException e)
            throws IOException, ServletException {
        Result result = Result.clientError("请先登录认证").code(401);
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().println(JSONUtil.toJsonStr(result));
    }
}
