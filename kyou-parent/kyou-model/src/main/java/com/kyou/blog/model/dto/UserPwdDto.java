package com.kyou.blog.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author CC
 * time 2023-07-16
 * description
 */
@Data
@NotNull
public class UserPwdDto implements Serializable {
    public static final long serialVersionUID=1L;
    @NotNull
    @Length(min = 6,max = 16,message = "密码长度在6到16位")
    private String curPwd;
    @NotNull
    @Length(min = 6,max = 16,message = "密码长度在6到16位")
    private String nextPwd;
    @NotNull
    @Length(min = 6,max = 16,message = "密码长度在6到16位")
    private String checkPwd;

}
