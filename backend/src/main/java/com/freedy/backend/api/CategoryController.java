package com.freedy.backend.api;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.freedy.backend.utils.AuthorityUtils;
import com.freedy.backend.utils.Local;
import com.freedy.backend.utils.Result;
import com.freedy.backend.exception.NoPermissionsException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.freedy.backend.entity.CategoryEntity;
import com.freedy.backend.service.CategoryService;
import com.freedy.backend.utils.PageUtils;


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


    @ApiOperation("列出所有分类")
    @GetMapping("/list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryService.queryPage(params);

        return Result.ok().put("page", page);
    }

    @ApiOperation("保存分类")
    @PostMapping("/save")
    public Result save(@RequestBody CategoryEntity category){
        category.setCreatorId(Local.MANAGER_LOCAL.get().getId());
		categoryService.save(category);
        return Result.ok();
    }

    @ApiOperation("更新分类")
    @PostMapping("/update")
    public Result update(@RequestBody CategoryEntity category){
        //修改他人且没权限
        if (!category.getCreatorId().equals(Local.MANAGER_LOCAL.get().getId())&&
                !AuthorityUtils.hasAuthority("tag-operation-to-others"))
            throw new NoPermissionsException();
		categoryService.updateById(category);

        return Result.ok();
    }

    @ApiOperation("删除分类")
    @GetMapping("/delete")
    public Result delete(Integer[] ids){
        boolean exception=false;
        for (Integer id : ids) {
            if (!id.equals(Local.MANAGER_LOCAL.get().getId())&&
                    !AuthorityUtils.hasAuthority("tag-operation-to-others")){
                exception=true;
            }else {
                categoryService.removeByIds(Arrays.asList(ids));
            }
        }
        if (exception){
            throw new NoPermissionsException();
        }
        return Result.ok();
    }
}
