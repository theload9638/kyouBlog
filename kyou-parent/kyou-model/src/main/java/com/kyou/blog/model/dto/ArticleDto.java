package com.kyou.blog.model.dto;

import com.kyou.blog.model.entity.Tag;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author CC
 * time 2023-07-25
 * description
 */
@Data
@NotNull(message ="数据不能为空")
public class ArticleDto implements Serializable {
    public static final long serialVersionUID=1L;

    private Long id;
    @NotBlank(message = "标题不能为空")
    private String title;
    @NotBlank(message = "内容不能为空")
    private String content;
    @NotNull(message = "文章状态不能为空")
    @Min(value = 0,message = "文章状态不能为负数")
    @Max(value = 4,message = "没有此文章状态")
    private Integer status;
    @NotNull(message = "评论开关不能为空")
    private Boolean isComment;
    private String thumbnail;
    private String summary;
    private List<Long> categoryIds;
    private List<String> tags;
    private Integer viewCount;
    /**
     * 评论数
     */
    private Integer commentCount;
    /**
     * 点赞数
     */
    private Integer likeCount;
    /**
     * 当前用户是否点赞
     */
    private Boolean likeStatus;




}
