package com.freedy.backend.api;

import java.util.Arrays;
import java.util.Map;

import com.freedy.backend.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.freedy.backend.entity.CommentEntity;
import com.freedy.backend.service.CommentService;
import com.freedy.backend.common.utils.PageUtils;


/**
 * 评论表
 *
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
@RestController
@RequestMapping("backend/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = commentService.queryPage(params);

        return Result.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Long id){
		CommentEntity comment = commentService.getById(id);

        return Result.ok().put("comment", comment);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public Result save(@RequestBody CommentEntity comment){
		commentService.save(comment);

        return Result.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public Result update(@RequestBody CommentEntity comment){
		commentService.updateById(comment);

        return Result.ok();
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    public Result delete(@RequestBody Long[] ids){
		commentService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
