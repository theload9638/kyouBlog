package com.kyou.blog.dataService.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kyou.blog.dataService.mapper.SysLogMapper;
import com.kyou.blog.dataService.service.SysLogService;
import com.kyou.blog.model.entity.SysLog;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cc
 * @since 2023-07-31
 */
@DubboService(interfaceClass = SysLogService.class)
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

    @Override
    public void clearLogs() {
        baseMapper.clearLogs();
    }
}
