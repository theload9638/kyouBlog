package com.kyou.blog.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author CC
 * time 2023-07-31
 * description
 */
@Data
public class UserHomeVo implements Serializable {

    public static final long serialVersionUID=1L;

    private Long id;
    private String username;
    private String nickname;
    private Integer articleNum;
    private Integer commentNum;
    private String headImage;
    private Integer level;
    private Integer experience;
    private String remark;
    private List<Long> followIds;
    private Integer followNum;
    private List<Long> fansIds;
    private Integer fansNum;

}
