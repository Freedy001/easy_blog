package com.freedy.backend.api;

import com.freedy.backend.entity.vo.dashboard.DashBoardVo;
import com.freedy.backend.scheduled.ArticleSynchronize;
import com.freedy.backend.service.*;
import com.freedy.backend.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Freedy
 * @date 2021/5/18 10:17
 */
@RestController
@RequestMapping("backend/sys")
public class SysController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private ThreadPoolExecutor executor;

    @Autowired
    private ArticleSynchronize articleSynchronize;

    @Value("#{loadSetting.setupTime}")
    private String setupTime;


    @GetMapping("getDashboardData")
    public Result getDashboardData() throws Exception {
        DashBoardVo boardVo = new DashBoardVo();
        //设置建立时间
        long timeLong = System.currentTimeMillis() - Long.parseLong(setupTime);
        boardVo.setEstablishmentTime((timeLong/(60*60*24*1000))+"天");
        CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> {
            //设置评论数
            Integer count = commentService.count();
            boardVo.setCommentNum(String.valueOf(count));
        }, executor);
        CompletableFuture<Void> f2 = CompletableFuture.runAsync(() -> {
            //访客统计
            List<Integer> visitorNum=visitorService.visitorNumInPath7Days(System.currentTimeMillis());
            boardVo.setVisitorNum(visitorNum);
        }, executor);
        //设置文章的统计信息
        articleService.articleStatistics(boardVo);
        f1.get();
        f2.get();
        return Result.ok().setData(boardVo);
    }

    @ApiOperation("立即触发定时任务")
    @GetMapping("/synchronizeToEs")
    public Result hand(){
        articleSynchronize.synchronizeArticleToEs();
        return Result.ok();
    }

}
