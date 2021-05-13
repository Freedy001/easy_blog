package com.freedy.backend.apiFront;

import com.freedy.backend.common.utils.IPUtil;
import com.freedy.backend.common.utils.PageUtils;
import com.freedy.backend.common.utils.Result;
import com.freedy.backend.entity.CommentEntity;
import com.freedy.backend.entity.vo.CommentVo;
import com.freedy.backend.service.CommentService;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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

    @ApiOperation("发布评论")
    @PostMapping("/publish")
    public Result publishComment(@RequestBody CommentEntity comment, HttpServletRequest httpRequest){
        comment.setIp(IPUtil.getRemoteIpAddr(httpRequest));
        commentService.publishComment(comment);
        return Result.ok();
    }

    @ApiOperation("获取评论列表")
    @GetMapping("/getList")
    public Result getComments(@RequestParam Map<String, Object> params){
        PageUtils page = commentService.queryPage(params);
        return Result.ok().setData(page);
    }

}
