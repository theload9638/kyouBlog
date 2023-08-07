package com.kyou.blog.background.web;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kyou.blog.common.Result;
import com.kyou.blog.common.constant.RedisConstant;
import com.kyou.blog.common.emuration.OperationType;
import com.kyou.blog.model.dto.PageLogDto;
import com.kyou.blog.model.entity.SysLog;
import com.kyou.blog.model.vo.PageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.Duration;
import java.util.List;

/**
 * @author CC
 * time 2023-07-31
 * description
 */
@Api(value = "系统日志管理")
@RestController
@RequestMapping("/sys/log")
public class SysLogController extends BaseController
{

    @ApiOperation("获取日志分页记录")
    @PreAuthorize("@auth.authenticate()")
    @GetMapping("/list")
    public Result<PageVo<SysLog>> list(@Valid PageLogDto dto){
        String val = redisUtil.getVal(RedisConstant.KYOU_LOG);
        if (StringUtils.hasText(val)) {
            return Result.success(JSONUtil.toBean(val,PageVo.class));
        }
        LambdaQueryWrapper<SysLog> l = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(dto.getUri())) {
           l.like(SysLog::getUri, dto.getUri());
        }
        String i = dto.getType();
        if(StringUtils.hasText(i)){
            if(OperationType.contains(i)){
                l.eq( SysLog::getType, i);
            }
        }
        if(StringUtils.hasText(dto.getStatus())){
           l .like( SysLog::getResult, dto.getStatus());
        }
        Page<SysLog> page = sysLogService.page(
                new Page<SysLog>(dto.getPageNum(), dto.getPageSize()),l);
        PageVo<SysLog> sysLogPageVo = new PageVo<>();
        sysLogPageVo.setTotal(page.getTotal());
        sysLogPageVo.setRecords(page.getRecords());
        redisUtil.setVal(RedisConstant.KYOU_LOG,sysLogPageVo, Duration.ofMinutes(10));
        return Result.success(sysLogPageVo);
    }

    @ApiOperation("删除日志记录")
    @PreAuthorize("@auth.hasPerms('setting:log:remove')")
    @DeleteMapping("/del")
    public Result del(@RequestParam("ids") @NotEmpty List<Long> ids){
        sysLogService.removeByIds(ids);
        clearLogCache();
        return Result.success();
    }
    @ApiOperation("清空日志")
    @PreAuthorize("@auth.hasPerms('setting:log:remove')")
    @DeleteMapping
    public Result clearLogs(){
        sysLogService.clearLogs();
        clearLogCache();
        return Result.success();
    }

    public void clearLogCache(){
        redisUtil.del(RedisConstant.KYOU_LOG);
    }

}
