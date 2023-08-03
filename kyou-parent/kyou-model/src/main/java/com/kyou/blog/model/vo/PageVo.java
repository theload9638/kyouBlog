package com.kyou.blog.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author CC
 * time 2023-07-20
 * description
 */
@Data
public class PageVo<T> implements Serializable {
    public static final long serialVersionUID=1L;
    private List<T> records;
    private long total;
}
