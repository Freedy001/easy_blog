package com.freedy.backend.controller;

import java.util.Arrays;
import java.util.Map;

import com.freedy.backend.common.utils.Local;
import com.freedy.backend.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.freedy.backend.entity.TagEntity;
import com.freedy.backend.service.TagService;
import com.freedy.backend.common.utils.PageUtils;


/**
 * 文章标签
 *
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
@RestController
@RequestMapping("backend/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = tagService.queryPage(params);
        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @PreAuthorize("hasAuthority('tag-self')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
		TagEntity tag = tagService.getById(id);
        return Result.ok().put("tag", tag);
    }

    /**
     * 保存
     */
    @PreAuthorize("hasAuthority('tag-self')")
    @PostMapping("/save")
    public Result save(@RequestBody TagEntity tag){
        tag.setCreatorId(Local.MANAGER_LOCAL.get().getId());
		tagService.save(tag);
        return Result.ok();
    }

    /**
     * 修改
     */
    @PreAuthorize("hasAuthority('tag-self')")
    @PostMapping("/update")
    public Result update(@RequestBody TagEntity tag){
		tagService.updateById(tag);
        return Result.ok();
    }

    /**
     * 删除
     */
    @PreAuthorize("hasAuthority('tag-self')")
    @GetMapping("/delete")
    public Result delete(Integer[] ids){
		tagService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
