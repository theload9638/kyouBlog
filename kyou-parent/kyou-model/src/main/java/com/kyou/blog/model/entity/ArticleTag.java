package com.kyou.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 文章标签表
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleTag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文章id
     */
    private Long aid;

    /**
     * 标签id
     */
    private Long tid;


}
