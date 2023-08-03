package com.kyou.blog.common.annotation;


import com.kyou.blog.common.emuration.OperationType;

import java.lang.annotation.*;

/**
 * @author CC
 * time 2023-07-07
 * description 自定义操作日志注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(value = {ElementType.METHOD})
public @interface Log {
    /**
     * 方法操作类型
     * @return
     */
    public OperationType value();

    /**
     * 模块
     */
    public String title() default "";

    /**
     * 是否保存请求的参数
     */
    public boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    public boolean isSaveResponseData() default true;


}
