package com.kyou.blog.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author CC
 * time 2023-08-02
 * description
 */
@Data
public class UserSignVo implements Serializable {
    public static final long serialVersionUID=1L;
    /**
     * 用户id
     */
    private Long id;
    /**
     * 本月签到次数
     */
    private Integer signMonthNum;
    /**
     * 本月签到的天数
     */
    private int[] signDays;
    /**
     * 本月的天数
     */
    private Integer daysMonth;
    /**
     * 最大连续签到次数
     */
    private Integer maxCoiledNum;

}
