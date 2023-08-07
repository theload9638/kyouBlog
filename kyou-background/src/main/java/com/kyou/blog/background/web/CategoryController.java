package com.kyou.blog.background.web;


import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kyou.blog.background.webUtil.WebUtil;
import com.kyou.blog.common.Result;
import com.kyou.blog.common.annotation.Log;
import com.kyou.blog.common.constant.MsgConstant;
import com.kyou.blog.common.constant.RedisConstant;
import com.kyou.blog.common.emuration.OperationType;
import com.kyou.blog.model.entity.Category;
import com.kyou.blog.model.vo.ArticleCategoryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 分类表 前端控制器
 * </p>
 *
 * @author cc
 * @since 2023-07-11
 */
@Api(tags = "分类相关接口")
@RestController
@Slf4j
@RequestMapping("/sys/category")
public class CategoryController extends BaseController{
    @ApiOperation("获取展示的分类列表")
    @GetMapping("/list")
    @PreAuthorize("@auth.authenticate()")
    public Result<List<ArticleCategoryVo>> list()
    {
        String val = redisUtil.getVal(RedisConstant.CATEGORY_LIST);
        if (StringUtils.hasText(val)) {
            return Result.success(JSONUtil.toList(val, ArticleCategoryVo.class));
        }
        List<ArticleCategoryVo> list=categoryService.listCategroies();
        //20分钟
        redisUtil.setVal(RedisConstant.CATEGORY_LIST,list,Duration.ofMinutes( RedisConstant.CATEGORY_LIST_TTL));
        return Result.success(list);
    }
    @ApiOperation("删除分类")
    @DeleteMapping("/{id}")
    @Log(value = OperationType.DELETE,title = "分类模块")
    @PreAuthorize("@auth.hasPerms('os:category:remove')")
    public Result del(@PathVariable Long id){
        categoryService.removeById(id);
        clearCategoryCache();
        return Result.success();
    }
    @ApiOperation("获取下拉框的分类列表")
    @GetMapping("/selectList")
    @PreAuthorize("@auth.authenticate()")
    public Result< List<Category>> selectList()
    {
        String val = redisUtil.getVal(RedisConstant.CATEGORY_SELECT);
        if (StringUtils.hasText(val)) {
            return Result.success(JSONUtil.toList(val,Category.class));
        }
        List<Category> list = categoryService.list();
        redisUtil.setVal(RedisConstant.CATEGORY_SELECT,list,Duration.ofMinutes(20));
        return Result.success(list);
    }
    @ApiOperation("获取单个分类")
    @GetMapping("/{id}")
    @PreAuthorize("@auth.hasPerms('os:category:select')")
    public Result getOne(@PathVariable
                             @NotNull(message = "分类id不能为空")
                             @Min(value = 0,message = "分类id必须为正整数") Long id){
        Category data = categoryService.getById(id);
        return Result.success(data);
    }
    @ApiOperation("新增分类")
    @PutMapping
    @Log(value = OperationType.INSERT,title = "分类模块")
    @PreAuthorize("@auth.hasPerms('os:category:add')")
    public Result add(@RequestBody @Valid Category category){
         categoryService.save(category);
        clearCategoryCache();
        return Result.success();
    }
    @ApiOperation("修改分类")
    @PostMapping
    @Log(value = OperationType.UPDATE,title = "分类模块")
    @PreAuthorize("@auth.hasPerms('os:category:edit')")
    public Result edit(@RequestBody @Valid Category category){
        if (!WebUtil.notNullNumber(category.getId())) {
            return Result.clientError(MsgConstant.QUERY_PARAM_ERR);
        }
        //父子id重复
        if(category.getId().equals(category.getPid())){
            return Result.serverError(MsgConstant.CATEGORY_LOOP_ERR);
        }
        //父子节点id重复
        List<Category> children = categoryService.list(
                new LambdaQueryWrapper<Category>()
                        .eq(Category::getPid, category.getId())
        );
        List<Long> cids = children.stream().
                map(Category::getId).collect(Collectors.toList());
        if(cids.contains(category.getPid())){
            return Result.serverError(MsgConstant.CATEGORY_LOOP_ERR);
        }
        categoryService.updateById(category);
        clearCategoryCache();
        return Result.success();
    }
    public void clearCategoryCache(){
        redisUtil.del(RedisConstant.CATEGORY_LIST);
        redisUtil.del(RedisConstant.CATEGORY_SELECT);
        redisUtil.del(RedisConstant.KYOU_FRONT);
        redisUtil.del(RedisConstant.KYOU_FRONT);
    }

}

