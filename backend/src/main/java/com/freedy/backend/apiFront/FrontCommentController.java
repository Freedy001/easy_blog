package com.freedy.backend.apiFront;

import com.freedy.backend.SysSetting.LoadSetting;
import com.freedy.backend.constant.CacheConstant;
import com.freedy.backend.utils.IPUtil;
import com.freedy.backend.utils.PageUtils;
import com.freedy.backend.utils.Result;
import com.freedy.backend.entity.CommentEntity;
import com.freedy.backend.service.CommentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Freedy
 * @date 2021/5/11 22:59
 */
@RestController
@RequestMapping("/frontend/comment")
public class FrontCommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private LoadSetting setting;

    @CacheEvict(cacheNames = CacheConstant.COMMENT_CACHE_NAME,allEntries = true)
    @ApiOperation("发布评论")
    @PostMapping("/publish")
    public Result publishComment(@RequestBody CommentEntity comment, HttpServletRequest httpRequest){
        comment.setIp(IPUtil.getRemoteIpAddr(httpRequest));
        comment.setCommentStatus(setting.getExamination() ? 0 : 1);
        commentService.publishComment(comment);
        return Result.ok();
    }

    @ApiOperation("获取评论列表")
    @GetMapping("/getList")
    @Cacheable(cacheNames = CacheConstant.COMMENT_CACHE_NAME,sync = true)
    public Result getComments(@RequestParam Map<String, Object> params){
        PageUtils page = commentService.queryPage(params);
        return Result.ok().setData(page);
    }


}
