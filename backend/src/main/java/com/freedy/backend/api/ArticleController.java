package com.freedy.backend.api;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.freedy.backend.common.utils.Result;
import com.freedy.backend.entity.vo.ArticleDraftVo;
import com.freedy.backend.entity.vo.ArticleVo;
import com.freedy.backend.middleWare.mq.annotation.ESEvict;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("列出所有文章")
    @GetMapping("/list")
    public Result list(@RequestParam Map<String, Object> params) throws ExecutionException, InterruptedException {
        PageUtils page = articleService.getBackArticleList(params);
        return Result.ok().setData(page);
    }

    @ApiOperation("查出文章详细详细")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Long id){
        return Result.ok().setData(articleService.getArticle(id));
    }

    @CacheEvict("article")
    @ApiOperation("保存或修改文章")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody ArticleVo article) throws ExecutionException, InterruptedException {
        //当传入的ArticleVo没有id时表示创建新的文章
        //当传入的ArticleVo有id时表示修改文章
        articleService.saveArticle(article);
        return Result.ok();
    }

    @ApiOperation("保存文章为草稿")
    @PostMapping("/saveDraft")
    public Result saveDraft(@RequestBody ArticleDraftVo draftVo){
        articleService.saveDraft(draftVo);
        return Result.ok();
    }

    @ESEvict
    @CacheEvict("article")
    @ApiOperation("删除文章")
    @GetMapping("/delete")
    public Result delete(@RequestParam Long[] ids){
		articleService.deleteArticle(Arrays.asList(ids));
        return Result.ok();
    }

}
