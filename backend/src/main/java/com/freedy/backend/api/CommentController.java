package com.freedy.backend.api;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.freedy.backend.common.utils.Result;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation("列出评论")
    @GetMapping("/list")
    public Result list(@RequestParam Map<String, Object> params) throws ExecutionException, InterruptedException {
        PageUtils page = commentService.queryAdminPage(params);
        return Result.ok().setData(page);
    }


    @ApiOperation("删除")
    @GetMapping("/delete")
    public Result delete(@RequestBody Long[] ids){
		commentService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
