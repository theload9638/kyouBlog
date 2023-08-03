package com.kyou.blog.model.vo;

import com.kyou.blog.model.entity.UserFollow;
import lombok.Data;

import java.io.Serializable;

/**
 * @author CC
 * time 2023-08-01
 * description
 */
@Data
public class UserFollowVo extends UserFollow implements Serializable {
    public static final long serialVersionUID=1L;

    private String nickname;

    private String username;
    private String headImage;

}
