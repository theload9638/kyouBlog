package com.kyou.blog.background.web;


import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kyou.blog.background.webUtil.RedisUtil;
import com.kyou.blog.common.Result;
import com.kyou.blog.common.constant.RedisConstant;
import com.kyou.blog.dataService.service.LinksService;
import com.kyou.blog.model.entity.Links;
import com.kyou.blog.model.vo.PageVo;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author cc
 * @since 2023-07-21
 */
@RestController
@RequestMapping("/sys/links")
public class LinksController {

    @DubboReference(interfaceClass = LinksService.class)
    private LinksService linksService;
    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation("获取所有链接")
    @GetMapping("/list")
    public Result<PageVo<Links>> list(Integer pageNum,Integer pageSize){
        String val = redisUtil.getVal(RedisConstant.KYOU_LINKS);
        if (StringUtils.hasText(val)) {
            return Result.success(JSONUtil.toBean(val,PageVo.class));
        }
        Page<Links> page = linksService.page(
                new Page<Links>(pageNum == null ? 1 : pageNum, pageSize == null ? 5 : pageSize), null
        );
        PageVo<Links> linksPageVo = new PageVo<>();
        linksPageVo.setRecords(page.getRecords());
        linksPageVo.setTotal(page.getTotal());
        redisUtil.setVal(RedisConstant.KYOU_LINKS,linksPageVo, Duration.ofHours(1));
        return Result.success(linksPageVo);
    }





}

