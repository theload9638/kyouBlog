package com.kyou.blog.model.vo;

import com.kyou.blog.model.dto.ArticleRankDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author CC
 * time 2023-07-26
 * description
 */
@Data
public class BackInfoVo implements Serializable {
    public static final long serialVersionUID=1L;

    private Integer articlePublished;
    private Integer commentCount;
    private Integer tagNum;
    private List<ArticleRankDto> rankList;
    private Integer joinDays;
    private List<UserVo> users;

}
