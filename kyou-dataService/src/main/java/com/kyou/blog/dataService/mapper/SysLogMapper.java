package com.kyou.blog.dataService.mapper;

import com.kyou.blog.model.entity.SysLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.DeleteMapping;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cc
 * @since 2023-07-31
 */
public interface SysLogMapper extends BaseMapper<SysLog> {
    @Delete("delete from sys_log")
    void clearLogs();
}
