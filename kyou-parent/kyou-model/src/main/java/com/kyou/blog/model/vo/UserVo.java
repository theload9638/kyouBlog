package com.kyou.blog.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.kyou.blog.model.entity.Role;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author CC
 * time 2023-07-14
 * description
 */
@Data
public class UserVo implements Serializable {
    public static final long serialVersionUID=1L;
    private Long id;
    @Length(min = 4,max = 16,message = "用户名长度在6到16位")
    private String username;
    @Email(message = "邮箱格式不正确")
    private String email;
    private String gender;
    @Length(min = 4,max = 10,message = "昵称长度在4到10位")
    private String nickname;
    private String headImage;
    private String remark;
    private String delFlag;
    private Integer status;
    private String lastIp;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<Role> roles;

}
