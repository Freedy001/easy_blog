package com.freedy.backend.api;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.freedy.backend.aspect.annotation.RecordLog;
import com.freedy.backend.entity.ManagerEntity;
import com.freedy.backend.enumerate.RecordEnum;
import com.freedy.backend.utils.AuthorityUtils;
import com.freedy.backend.utils.Local;
import com.freedy.backend.utils.Result;
import com.freedy.backend.exception.NoPermissionsException;
import com.freedy.backend.valid.Update;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.freedy.backend.entity.CategoryEntity;
import com.freedy.backend.service.CategoryService;
import com.freedy.backend.utils.PageUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 * 分类表
 *
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
@Validated
@RestController
@RequestMapping("backend/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    @ApiOperation("列出所有分类")
    @GetMapping("/list")
    public Result list(@RequestParam Map<String, Object> params) {
        PageUtils page = categoryService.queryPage(params);
        return Result.ok().put("page", page);
    }

    @RecordLog(type = RecordEnum.CATEGORY)
    @ApiOperation("保存分类")
    @PostMapping("/save")
    public Result saveCategory(@Validated @RequestBody CategoryEntity category) {
        category.setCreatorId(Local.MANAGER_LOCAL.get().getId());
        categoryService.save(category);
        return Result.ok();
    }

    @RecordLog(type = RecordEnum.CATEGORY)
    @ApiOperation("更新分类")
    @PostMapping("/update")
    public Result updateCategory(@Validated(Update.class) @RequestBody CategoryEntity category) {
        //修改他人且没权限
        ManagerEntity entity = Local.MANAGER_LOCAL.get();
        if (!category.getCreatorId().equals(entity.getId())&&entity.getStatus()!=1)
            throw new NoPermissionsException();
        categoryService.updateById(category);

        return Result.ok();
    }

    @RecordLog(type = RecordEnum.CATEGORY)
    @ApiOperation("删除分类")
    @GetMapping("/delete")
    public Result deleteCategory(@NotNull Integer[] ids) {
        categoryService.deleteCategories(Arrays.asList(ids));
        return Result.ok();
    }
}
