package com.freedy.backend.controller;

import java.util.Arrays;
import java.util.Map;

import com.freedy.backend.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.freedy.backend.entity.RoleEntity;
import com.freedy.backend.service.RoleService;
import com.freedy.backend.common.utils.PageUtils;


/**
 * 角色表
 *
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
@RestController
@RequestMapping("backend/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = roleService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
		RoleEntity role = roleService.getById(id);

        return Result.ok().put("role", role);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public Result save(@RequestBody RoleEntity role){
		roleService.save(role);

        return Result.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public Result update(@RequestBody RoleEntity role){
		roleService.updateById(role);

        return Result.ok();
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
		roleService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
