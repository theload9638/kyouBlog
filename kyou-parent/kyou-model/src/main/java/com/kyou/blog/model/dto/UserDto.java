package com.kyou.blog.model.dto;

import com.kyou.blog.model.entity.Role;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author CC
 * time 2023-07-20
 * description
 */
@Data
@NotNull(message = "请求数据不能为空")
public class UserDto implements Serializable {
    public static final long serialVersionUID=1L;
    @NotNull(message = "用户标识不能为空")
    @Min(message = "用户标识异常",value = 0)
    private Long id;
    @NotBlank(message = "邮箱不能为空")
    @Email
    private String email;
    @Length(min = 4,max = 16,message = "用户名长度在6到16位")
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "性别异常")
    private String gender;
    @Length(min = 4,max = 10,message = "昵称长度在4到10位")
    private String nickname;
    private String headImage;
    private String remark;
    private String delFlag;
    @Min(value = 0,message = "账号异常")
    @Max(value = 1,message = "账号异常")
    private Integer status;
    private List<String> roles;




}
