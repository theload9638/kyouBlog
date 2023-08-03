package com.kyou.blog.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
@Data
@NotNull
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @Length(min = 4,max = 16,message = "用户名长度在4到16位")
    @Pattern(regexp = "^\\w{4,16}$",message = "账号格式为数字以及字母下划线")
    private String username;

    /**
     * 密码
     */
    @Length(min = 6,max = 16,message = "密码长度在6到16位")
    private String password;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 性别
     */
    private String gender;

    /**
     * 用户昵称
     */
    @Length(min = 4,max = 10,message = "昵称长度在4到10位")
    private String nickname;

    /**
     * 头像
     */
    private String headImage;

    /**
     * 个人说明
     */
    private String remark;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
//    @TableLogic(value = "0",delval = "1")
    private String delFlag;

    /**
     * 账户状态(0启用，1禁用)
     */
    private Integer status;

    /**
     * 用户最后登录ip
     */
    private String lastIp;

    /**
     * 注册时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 更新者
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
