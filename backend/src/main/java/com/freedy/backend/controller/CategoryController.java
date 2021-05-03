package com.freedy.backend.controller;

import java.util.Arrays;
import java.util.Map;

import com.freedy.backend.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.freedy.backend.entity.CategoryEntity;
import com.freedy.backend.service.CategoryService;
import com.freedy.backend.common.utils.PageUtils;


/**
 * 分类表
 *
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
@RestController
@RequestMapping("backend/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
		CategoryEntity category = categoryService.getById(id);

        return Result.ok().put("category", category);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public Result save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return Result.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public Result update(@RequestBody CategoryEntity category){
		categoryService.updateById(category);

        return Result.ok();
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    public Result delete(Integer[] ids){
		categoryService.removeByIds(Arrays.asList(ids));
        return Result.ok();
    }

}
