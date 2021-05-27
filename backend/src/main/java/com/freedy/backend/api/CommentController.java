package com.freedy.backend.api;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.freedy.backend.aspect.annotation.RecordLog;
import com.freedy.backend.constant.CacheConstant;
import com.freedy.backend.enumerate.RecordEnum;
import com.freedy.backend.utils.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import com.freedy.backend.entity.CommentEntity;
import com.freedy.backend.service.CommentService;

import javax.servlet.http.HttpServletRequest;


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

    @Cacheable(cacheNames = CacheConstant.COMMENT_CACHE_NAME, sync = true,
            key = "(T(com.freedy.backend.utils.AuthorityUtils).hasAuthority('comment-operation-to-others')?'admin':T(com.freedy.backend.utils.Local).MANAGER_LOCAL.get().id)+'-'+#root.methodName+'-'+#root.args[0]")
    @ApiOperation("列出评论")
    @GetMapping("/list")
    public Result list(@RequestParam Map<String, Object> params) throws ExecutionException, InterruptedException {
        PageUtils page = commentService.queryAdminPage(params);

        return Result.ok().setData(page);
    }

    @GetMapping("/getNotReadNum")
    public Result notReadNum() {
        int count;
        if (AuthorityUtils.hasAuthority("comment-operation-to-others")) {
            count = commentService.count(new QueryWrapper<CommentEntity>()
                    .lambda().eq(CommentEntity::getHasRead, 0));
        } else {
            count = commentService.countNotRead(Local.MANAGER_LOCAL.get().getId());
        }
        return Result.ok().setData(count);

    }

    @RecordLog(type = RecordEnum.COMMENT)
    @CacheEvict(cacheNames = CacheConstant.COMMENT_CACHE_NAME, allEntries = true)
    @ApiOperation("回复评论")
    @PostMapping("/replay")
    public Result replayComment(@RequestBody CommentEntity commentEntity, HttpServletRequest request) {
        commentEntity.setIp(IPUtil.getRemoteIpAddr(request));
        commentService.replay(commentEntity);
        return Result.ok();
    }

    @RecordLog(type = RecordEnum.COMMENT)
    @CacheEvict(cacheNames = CacheConstant.COMMENT_CACHE_NAME, allEntries = true)
    @ApiOperation("审核通过")
    @GetMapping("/confirmExaminations")
    public Result confirmComment(Long[] ids) {
        commentService.confirmExaminations(Arrays.asList(ids));
        return Result.ok();
    }

    @RecordLog(type = RecordEnum.COMMENT)
    @ApiOperation("删除")
    @CacheEvict(cacheNames = CacheConstant.COMMENT_CACHE_NAME, allEntries = true)
    @GetMapping("/delete")
    public Result deleteComment(Long[] ids) {
        commentService.deleteComment(Arrays.asList(ids));
        return Result.ok();
    }

}
