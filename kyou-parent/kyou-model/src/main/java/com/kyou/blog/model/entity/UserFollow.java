package com.kyou.blog.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author cc
 * @since 2023-07-31
 */
@Data
@NotNull(message = "请求参数不能为空")
public class UserFollow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId("cur_id")
    @NotNull(message = "关注人id不能为空")
    @Min(value = 1,message = "关注异常")
    private Long curId;

    /**
     * 粉丝id
     */
    @NotNull(message = "粉丝id不能为空")
    @TableField("fans_id")
    @Min(value = 1,message = "关注异常")
    private Long fansId;


}
