package com.kyou.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 文章分类表
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文章id
     */
    private Long aid;

    /**
     * 分类id
     */
    private Long cid;


}
