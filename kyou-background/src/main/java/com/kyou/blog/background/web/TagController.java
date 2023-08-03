package com.kyou.blog.background.web;


import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kyou.blog.background.webUtil.RedisUtil;
import com.kyou.blog.background.webUtil.WebUtil;
import com.kyou.blog.common.Result;
import com.kyou.blog.common.annotation.Log;
import com.kyou.blog.common.constant.MsgConstant;
import com.kyou.blog.common.constant.RedisConstant;
import com.kyou.blog.common.emuration.OperationType;
import com.kyou.blog.common.util.SysContext;

import com.kyou.blog.dataService.service.TagService;
import com.kyou.blog.model.entity.Tag;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.util.List;

/**
 * <p>
 * 标签表 前端控制器
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
@Api(tags = "文章标签相关接口")
@RestController
@RequestMapping("/sys/tag")
public class TagController {
    @DubboReference(interfaceClass = TagService.class)
    private TagService tagService;
    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation("获取标签列表")
    @PreAuthorize("@auth.hasPerms('os:tags:select')")
    @GetMapping("/list")
    public Result<List<Tag>> listTags(){
        String val = redisUtil.getVal(RedisConstant.KYOU_TAGS);
        if(StringUtils.hasText(val)){
            return Result.success(JSONUtil.toList(val,Tag.class));
        }
        List<Tag> list = tagService.list();
        redisUtil.setVal(RedisConstant.KYOU_TAGS,list, Duration.ofMinutes(20));
        return Result.success(list);
    }
    @ApiOperation("新增标签")
    @PreAuthorize("@auth.hasPerms('os:tags:add')")
    @PostMapping
    @Log(value = OperationType.INSERT,title = "标签模块")
    public Result addTag(@RequestParam("name")@NotBlank String name){
        int count = tagService.count(
                new LambdaQueryWrapper<Tag>()
                        .eq(Tag::getName, name)
        );
        if (count>0) {
            return Result.serverError(MsgConstant.TAG_REPEAT);
        }
        Long userId = SysContext.getUserId();
        String time = WebUtil.formatTime(WebUtil.DATE_PATH_LINUX);
        String key=RedisConstant.TAG_LIMIT+time+":"+userId;
        String val = redisUtil.getVal(key);
        if (StringUtils.hasText(val)) {
            if (Integer.parseInt(val)>=3) {
                return Result.clientError("每天只能添加3个标签");
            }
        }else{
            redisUtil.setVal(key,1);
        }
        Tag tag = new Tag();
        tag.setName(name);
        tagService.save(tag);
        redisUtil.val().increment(key);
        clearTagCache();
        return Result.success();
    }
    public void clearTagCache(){
        redisUtil.del(RedisConstant.KYOU_TAGS);
        redisUtil.del(RedisConstant.KYOU_FRONT);
        redisUtil.del(RedisConstant.KYOU_FRONT);
    }



}

