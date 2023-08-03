package com.kyou.blog.model.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author CC
 * time 2023-07-20
 * description
 */
@Data
@NotNull(message = "分页参数异常")
public class PageUserDto implements Serializable {
    public static final long serialVersionUID=1L;
    @NotNull(message = "页码不能为空")
    @Min(value = 1,message = "页码不能小于1")
    private Integer pageNum=1;
    @NotNull(message = "页量不能为空")
    @Min(value = 1,message = "页量不能小于1")
    private Integer pageSize=10;
    private String status;
    private String gender;
    private String key;
}
