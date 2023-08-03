package com.kyou.blog.dataService.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kyou.blog.dataService.mapper.LinksMapper;
import com.kyou.blog.dataService.service.LinksService;
import com.kyou.blog.model.entity.Links;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cc
 * @since 2023-07-21
 */
@DubboService(interfaceClass = LinksService.class)
public class LinksServiceImpl extends ServiceImpl<LinksMapper, Links> implements LinksService {

}
