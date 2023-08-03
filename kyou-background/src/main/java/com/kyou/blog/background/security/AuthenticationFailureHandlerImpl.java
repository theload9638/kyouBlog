package com.kyou.blog.background.security;

import cn.hutool.json.JSONUtil;
import com.kyou.blog.common.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
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
@Component("failureHandler")
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler, Serializable {
    private static final long serialVersionUID = -8970718410437077606L;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException e)
            throws IOException, ServletException {
        Result r = Result.clientError(e.getMessage()).code(401);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(JSONUtil.toJsonStr(r));
    }
}
