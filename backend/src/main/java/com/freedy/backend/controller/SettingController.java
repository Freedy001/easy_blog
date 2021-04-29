package com.freedy.backend.controller;

import java.util.Arrays;
import java.util.Map;

import com.freedy.backend.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.freedy.backend.entity.SettingEntity;
import com.freedy.backend.service.SettingService;
import com.freedy.backend.common.utils.PageUtils;


/**
 * 系统设置表
 *
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
@RestController
@RequestMapping("backend/setting")
public class SettingController {
    @Autowired
    private SettingService settingService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = settingService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
		SettingEntity setting = settingService.getById(id);

        return Result.ok().put("setting", setting);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public Result save(@RequestBody SettingEntity setting){
		settingService.save(setting);

        return Result.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public Result update(@RequestBody SettingEntity setting){
		settingService.updateById(setting);

        return Result.ok();
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
		settingService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
