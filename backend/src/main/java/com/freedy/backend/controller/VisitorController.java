package com.freedy.backend.controller;

import java.util.Arrays;
import java.util.Map;

import com.freedy.backend.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.freedy.backend.entity.VisitorEntity;
import com.freedy.backend.service.VisitorService;
import com.freedy.backend.common.utils.PageUtils;


/**
 * 访客记录表
 *
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
@RestController
@RequestMapping("backend/visitor")
public class VisitorController {
    @Autowired
    private VisitorService visitorService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = visitorService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Long id){
		VisitorEntity visitor = visitorService.getById(id);

        return Result.ok().put("visitor", visitor);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public Result save(@RequestBody VisitorEntity visitor){
		visitorService.save(visitor);

        return Result.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public Result update(@RequestBody VisitorEntity visitor){
		visitorService.updateById(visitor);

        return Result.ok();
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    public Result delete(@RequestBody Long[] ids){
		visitorService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
