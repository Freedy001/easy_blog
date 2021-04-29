package com.freedy.backend.controller;

import java.util.Arrays;
import java.util.Map;

import com.freedy.backend.common.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import com.freedy.backend.entity.ArticleEntity;
import com.freedy.backend.service.ArticleService;
import com.freedy.backend.common.utils.PageUtils;


/**
 * 文章表
 *
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
@RestController
@RequestMapping("backend/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @Secured("admin")
    @ApiOperation("列出所有文章")
    @GetMapping("/list")
    public Result list(Map<String, Object> params){
        PageUtils page = articleService.queryPage(params);
        return Result.ok().put("page", page);
    }


    @ApiOperation("查出文章详细详细")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Long id){
		ArticleEntity article = articleService.getById(id);
        return Result.ok().put("article", article);
    }

    @ApiOperation("保存文章")
    @PostMapping("/save")
    public Result save(@RequestBody ArticleEntity article){
		articleService.save(article);
        return Result.ok();
    }

    @ApiOperation("修改文章")
    @PostMapping("/update")
    public Result update(@RequestBody ArticleEntity article){
		articleService.updateById(article);
        return Result.ok();
    }

    @ApiOperation("删除文章")
    @GetMapping("/delete")
    public Result delete(@RequestBody Long[] ids){
		articleService.removeByIds(Arrays.asList(ids));
        return Result.ok();
    }

}
