package com.kyou.blog.dataService.service;

import com.kyou.blog.model.entity.SysLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cc
 * @since 2023-07-31
 */
public interface SysLogService extends IService<SysLog> {

    void clearLogs();
}
