package com.kyou.blog.model.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author cc
 * @since 2023-07-27
 */
@Data
public class UserComment implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 用户id
     */
    private Long id;
    /**
     * 评论人等级(1-6)
     */
    private Integer level;
    /**
     * 经验
     */
    private Integer experience;

    /**
     * 用户主页
     */
    private String homeLink;


}
