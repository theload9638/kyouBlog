package com.kyou.blog.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author CC
 * time 2023-07-31
 * description
 */
@Data
@NotNull(message = "请求参数不能为空")
public class PageLogDto {

    private Integer pageNum=1;
    private Integer pageSize=10;

    private String uri;
    private String type;
    private String status;

}
