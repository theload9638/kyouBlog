package com.kyou.blog.model.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author CC
 * time 2023-07-14
 * description
 */
@Data
@NotNull
public class LoginVo implements Serializable {
    public static final long serialVersionUID=1L;
    @NotBlank
    @Length(min = 4,max = 16,message = "用户名不能为空")
    @Pattern(regexp = "^\\w{4,16}$",message = "账号格式为数字以及字母下划线")
    private String username;
    @NotBlank
    @Length(min = 6,max = 16,message = "密码不能为空")
    private String password;
    @Length(min = 4,message = "验证码不能为空")
    private String verifyCode;
    private Boolean remember;
    /**
     * 缓存key
     */
    private String key;
    @Email(message = "邮箱格式不正确")
    private String email;
}
