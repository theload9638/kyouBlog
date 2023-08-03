package com.kyou.blog.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
@Data
@TableName(value = "role")
@NotNull
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色名称
     */
    @TableField(value = "name")
    @NotBlank
    private String name;

    /**
     * 角色权限字符串
     */
    @NotBlank
    private String keyName;

    /**
     * 角色状态（0正常 1停用）
     */
    @TableField(value = "status")
    private String status;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableField(value = "del_flag")
    @TableLogic(value = "0",delval = "1")
    private String delFlag;

    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT,value = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT,value ="create_time" )
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE,value ="update_by" )
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE,value = "update_time")
    private LocalDateTime updateTime;


}
