package com.kyou.blog.model.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author CC
 * time 2023-07-26
 * description
 */
@Data
@NotNull(message = "请求参数不能为空")
public class PageArticleDto implements Serializable {

    public static final long serialVersionUID=1L;
    @NotNull(message = "页码不能为空")
    @Min(value = 1,message = "页码最小必须为整数1")
    private Integer pageNum;
    @NotNull(message = "页量不能为空")
    @Min(value = 1,message = "页量最小不能低于整数1")
    private Integer pageSize;
    private String kw;
    private String type;
    private String order;
    @NotNull(message = "博客文章状态不能为空")
    private String status;



}
