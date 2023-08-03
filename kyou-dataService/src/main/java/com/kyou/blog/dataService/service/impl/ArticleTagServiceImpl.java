package com.kyou.blog.dataService.service.impl;

import com.kyou.blog.dataService.mapper.ArticleTagMapper;
import com.kyou.blog.dataService.service.ArticleTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kyou.blog.model.entity.ArticleTag;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * <p>
 * 文章标签表 服务实现类
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
@DubboService(interfaceClass =ArticleTagService.class)
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
