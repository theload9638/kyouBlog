package com.kyou.blog.background.interceptor;

import com.kyou.blog.background.properties.CCProperties;
import com.kyou.blog.background.webUtil.RedisUtil;
import com.kyou.blog.background.webUtil.WebUtil;
import com.kyou.blog.common.constant.RedisConstant;
import com.kyou.blog.common.util.SysContext;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


/**
 * @author CC
 * time 2023-07-19
 * description 只要用户在线，每三十分钟刷新一次
 */
@Slf4j
@Component
public class TokenFlushInterceptor implements HandlerInterceptor {
    @Autowired
    private CCProperties ccProperties;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("销毁本地线程--本次方法结束--->{}",request.getRequestURI());
        SysContext.remove();
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception
    {
        String uri = request.getRequestURI();
        log.info("进入[TokenFlushInterceptor]拦截器--》{}",uri);
        String auth = request.getHeader("Authorization");
        if(StringUtils.hasText(auth)){
            Claims claims = null;
            try {
                claims = WebUtil.parseJWT(ccProperties.getTokenSecret(), auth);
            } catch (Exception e) {
               log.error("解析token失败",e);
               return false;
            }
            Long id = claims.get("id", Long.class);
            //查询缓存
            String val = redisUtil.getVal(RedisConstant.REFLUSH_TOKEN + id);
            SysContext.setUserId(id);
            if (!StringUtils.hasText(val)) {
                log.info("刷新token");
               //说明已经过了30分钟
                Map<String,Object> map=new HashMap<>(4);
                map.put("id",id);
                String jwt = WebUtil.createJWT(ccProperties.getTokenSecret(),
                        map, ccProperties.getTokenExpire());
                //刷新token
                response.setHeader("Authorization",jwt);
                //刷新缓存
                redisUtil.setVal(RedisConstant.REFLUSH_TOKEN+id,"0",
                        Duration.ofMinutes(RedisConstant.REFULSH_TOKEN_TTL));
            }
        }
        return true;
    }
}
