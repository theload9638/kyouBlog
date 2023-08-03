package com.kyou.blog.background.webUtil;

import cn.hutool.json.JSONUtil;
import com.kyou.blog.common.annotation.Log;
import com.kyou.blog.model.entity.SysLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author CC
 * time 2023-07-27
 * description
 */
@Aspect
@Component
@Slf4j
public class LogAop {
    private long start=0;
    private long end=0;
    @Autowired
    private LogQueue queue;
    @Pointcut("execution(* com.kyou.blog.background.web..*(..))&&@annotation(com.kyou.blog.common.annotation.Log)")
    private void log(){}

    @Around("log()&&@annotation(l)")
    public Object around(ProceedingJoinPoint joinPoint,Log l) throws Throwable {
        start= System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        end=System.currentTimeMillis();
        recordLog(joinPoint,end-start,WebUtil.getReq(),l,proceed);
        return proceed;
    }
    private void recordLog(JoinPoint joinPoint, long exeTime,
                           HttpServletRequest request,Log l,Object result) {
        Object[] args = joinPoint.getArgs();
        SysLog sysLog = new SysLog();
        sysLog.setType(l.value().toString());
        sysLog.setTitle(l.title());
        if (l.isSaveRequestData()) {
            sysLog.setParams(Arrays.deepToString(args));
        }
        if (l.isSaveResponseData()) {
            sysLog.setResult(JSONUtil.toJsonStr(result));
        }
        sysLog.setMethod(joinPoint.getSignature().toString());
        sysLog.setUri(request.getRequestURI());
        sysLog.setOperator(request.getRemoteUser());
        sysLog.setIp(request.getRemoteAddr());
        sysLog.setCreateTime(LocalDateTime.now());
        sysLog.setRunTime(Integer.valueOf(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(exeTime))));
        queue.add(sysLog);
    }

}
