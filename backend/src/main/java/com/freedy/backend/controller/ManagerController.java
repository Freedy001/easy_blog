package com.freedy.backend.controller;

import java.util.Arrays;
import java.util.Map;

import com.freedy.backend.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.freedy.backend.entity.ManagerEntity;
import com.freedy.backend.service.ManagerService;
import com.freedy.backend.common.utils.PageUtils;


/**
 * 管理员表
 *
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
@RestController
@RequestMapping("backend/manager")
public class ManagerController {
    @Autowired
    private ManagerService managerService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = managerService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
		ManagerEntity manager = managerService.getById(id);

        return Result.ok().put("manager", manager);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public Result save(@RequestBody ManagerEntity manager){
		managerService.save(manager);

        return Result.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public Result update(@RequestBody ManagerEntity manager){
		managerService.updateById(manager);

        return Result.ok();
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
		managerService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
