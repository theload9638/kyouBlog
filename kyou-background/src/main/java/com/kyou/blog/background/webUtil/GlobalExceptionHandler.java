package com.kyou.blog.background.webUtil;

import com.kyou.blog.common.Result;
import com.kyou.blog.common.exception.QueryParamException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * @author CC
 * time 2023-05-26
 * description
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler   {
    @ExceptionHandler({Exception.class})
    public Result handler(Exception ex,HttpServletRequest req){
        log.error("请求路径-->{}",req.getRequestURI());
        log.error("未知异常-->{}",ex.getMessage());
        ex.printStackTrace();
        return Result.serverError(ex.getMessage());
    }
    @ExceptionHandler({QueryParamException.class})
    public Result handler(QueryParamException ex){
        log.error("请求参数异常---》{}",ex.getCause());
        return Result.serverError(ex.getMessage());
    }
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result constraintViolationExceptionHandler( ConstraintViolationException ex,HttpServletRequest req) {
        log.error("请求路径-->{}",req.getRequestURI());
        log.error("[constraintViolationExceptionHandler]--->{}", ex);
        // 拼接错误
        StringBuilder detailMessage = new StringBuilder();
        for (ConstraintViolation<?> constraintViolation : ex.getConstraintViolations()) {
            // 使用 ; 分隔多个错误
            if (detailMessage.length() > 0) {
                detailMessage.append(";");
            }
            // 拼接内容到其中
            detailMessage.append(constraintViolation.getMessage());
        }
        // 包装 CommonResult 结果
        return Result.serverError(detailMessage.toString());
    }
    @ExceptionHandler(value = BindException.class)
    public Result bindExceptionHandler(HttpServletRequest req, BindException ex) {
        log.error("请求路径-->{}",req.getRequestURI());
        log.error("[bindExceptionHandler]", ex);
        // 拼接错误
        StringBuilder detailMessage = new StringBuilder();
        for (ObjectError objectError : ex.getAllErrors()) {
            // 使用 ; 分隔多个错误
            if (detailMessage.length() > 0) {
                detailMessage.append(";");
            }
            // 拼接内容到其中
            detailMessage.append(objectError.getDefaultMessage());
        }
        // 包装 CommonResult 结果
        return Result.serverError(detailMessage.toString());
    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result methodArgumentNotValidExceptionHandler(HttpServletRequest req, MethodArgumentNotValidException ex) {
        log.error("请求路径-->{}",req.getRequestURI());
        log.error("[MethodArgumentNotValidException]", ex);
        // 拼接错误
        StringBuilder detailMessage = new StringBuilder();
        for (ObjectError objectError : ex.getBindingResult().getAllErrors()) {
            // 使用 ; 分隔多个错误
            if (detailMessage.length() > 0) {
                detailMessage.append(";");
            }
            // 拼接内容到其中
            detailMessage.append(objectError.getDefaultMessage());
        }
        // 包装 CommonResult 结果
        return Result.serverError( detailMessage.toString());
    }
    @ExceptionHandler(value = AccessDeniedException.class)
    public Result accessDeniedExceptionHandler(AccessDeniedException ex,HttpServletRequest req){
        log.error("请求路径-->{}",req.getRequestURI());
        log.error("[AccessDeniedException]", ex);
        return Result.clientError(ex.getMessage());
    }
    @ExceptionHandler(value = ExpiredJwtException.class)
    public Result expiredJwtExceptionHandler(ExpiredJwtException ex,HttpServletRequest req){
        log.error("请求路径-->{}",req.getRequestURI());
        log.error("[ExpiredJwtException]", ex);
        return Result.serverError("登录凭证失效，请重新登录");
    }




}
