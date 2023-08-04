package com.kyou.blog.background.security;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyou.blog.background.webUtil.AuthenticationContextHolder;
import com.kyou.blog.background.webUtil.RedisUtil;
import com.kyou.blog.common.constant.MsgConstant;
import com.kyou.blog.common.constant.RedisConstant;
import com.kyou.blog.model.vo.CodeVo;
import com.kyou.blog.model.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author CC
 * time 2023-08-03
 * description
 */
@Slf4j
public class AuthLoginFilter extends UsernamePasswordAuthenticationFilter {

    public AuthLoginFilter() {

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        failureHandler.onAuthenticationFailure(request,response,failed);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        successHandler.onAuthenticationSuccess(request,response,authResult);
    }
    private RedisUtil redisUtil;
    private AuthenticationSuccessHandlerImpl successHandler;
    private AuthenticationFailureHandlerImpl failureHandler;

    public AuthLoginFilter(RedisUtil redisUtil,
                           AuthenticationManager authenticationManager,
                           AuthenticationSuccessHandlerImpl successHandler,
                           AuthenticationFailureHandlerImpl failureHandler) {
        super(authenticationManager);
        this.redisUtil=redisUtil;
        this.successHandler=successHandler;
        this.failureHandler=failureHandler;
        //不只是允许post请求经过此filter
        this.setPostOnly(false);
        //设置登录的路径和请求方式
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/sys/user/login","POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {
        if (!RequestMethod.POST.name().equals(request.getMethod())) {
            throw new IllegalStateException("无效登录");
        }
        if ("application/json".equals(request.getContentType())) {
            try {
                LoginVo loginParams = new ObjectMapper().readValue(request.getInputStream(), LoginVo.class);
                if (loginParams == null) {
                    throw new RuntimeException("用户不存在");
                }
                if(checkUsername(loginParams.getUsername())&&checkPwd(loginParams.getPassword())){
                   checkCodeAndKey(loginParams.getVerifyCode(),loginParams.getKey());
                    try {
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(
                                        loginParams.getUsername(),
                                loginParams.getPassword());
                       this.setDetails(request,auth);
                        AuthenticationContextHolder.setContext(auth);
                       return this.getAuthenticationManager().authenticate(auth);
                    } catch (AuthenticationException e) {
                        throw new RuntimeException(e);
                    }finally {
                        SecurityContextHolder.clearContext();
                    }
                }else {
                    throw new RuntimeException("用户名或密码错误");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return super.attemptAuthentication(request, response);
    }

    private boolean checkUsername(String username){
        if (!StringUtils.hasText(username)) {
            return false;
        }
        return username.trim().matches("^\\w{4,16}$");
    }
    private boolean checkPwd(String pwd){
        if (!StringUtils.hasText(pwd)) {
            return false;
        }
        return pwd.length() >= 6 || pwd.length() <= 16;
    }
    private void checkCodeAndKey(String code,String key){
        if (!StringUtils.hasText(key)) {
           throw new RuntimeException(MsgConstant.TOURIST_IDENTITY_ERR);
        }
        if (!StringUtils.hasText(code)) {
            throw new RuntimeException(MsgConstant.VERITY_CODE_ERR);
        }
        String o = redisUtil.getVal(RedisConstant.LOGIN_CODE + key);
        if (!StringUtils.hasText(o)) {
            throw new RuntimeException(MsgConstant.VERITY_CODE_EXPIRE);
        }
        CodeVo codeVo = JSONUtil.toBean(o, CodeVo.class);
        if(!code.equalsIgnoreCase(codeVo.getCode())){
            throw new RuntimeException(MsgConstant.VERITY_CODE_ERR);
        }
    }
}
