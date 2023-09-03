package com.kyou.blog.common.meta;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.kyou.blog.common.util.SysContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author CC
 * time 2023-07-13
 * description
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 执行插入操作时，自动调用此方法进行填充
     *@param  metaObject 被执行更新的对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("createBy", String.valueOf(SysContext.getUserId()));
        metaObject.setValue("updateBy",String.valueOf(SysContext.getUserId()));
    }
    /**
     *   执行更新操作时，自动调用此方法进行填充
     *@param  metaObject 被执行更新的对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateBy",String.valueOf(SysContext.getUserId()));
    }
}
