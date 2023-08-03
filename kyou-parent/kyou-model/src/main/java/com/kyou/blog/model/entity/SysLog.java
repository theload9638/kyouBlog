package com.kyou.blog.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author cc
 * @since 2023-07-31
 */
@Data
public class SysLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标识
     */
    private Long id;
    /**
     * 操作模块
     */
    private String title;

    /**
     *  操作人
     */
    private String operator;

    /**
     * 请求路径
     */
    private String uri;

    /**
     * 操作类型
     */
    private String type;

    /**
     * 请求ip
     */
    private String ip;

    /**
     * 执行时间(秒)
     */
    private Integer runTime;

    /**
     * 操作的方法
     */
    private String method;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 响应结果
     */
    private String result;

    private LocalDateTime createTime;
}
