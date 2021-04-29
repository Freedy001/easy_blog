package com.freedy.backend.controller;

import java.util.Arrays;
import java.util.Map;

import com.freedy.backend.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.freedy.backend.entity.ArticleTagRelationEntity;
import com.freedy.backend.service.ArticleTagRelationService;
import com.freedy.backend.common.utils.PageUtils;


/**
 * 标签文章关联表
 *
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
@RestController
@RequestMapping("backend/articleTagRelation")
public class ArticleTagRelationController {
    @Autowired
    private ArticleTagRelationService articleTagRelationService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = articleTagRelationService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
		ArticleTagRelationEntity articleTagRelation = articleTagRelationService.getById(id);

        return Result.ok().put("articleTagRelation", articleTagRelation);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public Result save(@RequestBody ArticleTagRelationEntity articleTagRelation){
		articleTagRelationService.save(articleTagRelation);

        return Result.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public Result update(@RequestBody ArticleTagRelationEntity articleTagRelation){
		articleTagRelationService.updateById(articleTagRelation);

        return Result.ok();
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
		articleTagRelationService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
