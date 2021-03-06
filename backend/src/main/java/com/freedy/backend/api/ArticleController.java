package com.freedy.backend.api;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.freedy.backend.aspect.annotation.RecordLog;
import com.freedy.backend.constant.CacheConstant;
import com.freedy.backend.enumerate.RecordEnum;
import com.freedy.backend.exception.NoPermissionsException;
import com.freedy.backend.utils.AuthorityUtils;
import com.freedy.backend.utils.Result;
import com.freedy.backend.entity.vo.article.ArticleDraftVo;
import com.freedy.backend.entity.vo.article.ArticleVo;
import com.freedy.backend.aspect.annotation.ESEvict;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.freedy.backend.service.ArticleService;
import com.freedy.backend.utils.PageUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 * 文章表
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
@Validated
@RestController
@RequestMapping("backend/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @Cacheable(cacheNames = CacheConstant.ARTICLE_CACHE_NAME,sync = true,
            key = "(T(com.freedy.backend.utils.AuthorityUtils).hasAuthority('article-operation-to-others')?'admin':T(com.freedy.backend.utils.Local).MANAGER_LOCAL.get().id)+'-'+#root.methodName+'-'+#root.args[0]")
    @ApiOperation("列出所有文章")
    @GetMapping("/list")
    public Result list(@RequestParam Map<String, Object> params) throws ExecutionException, InterruptedException {
        PageUtils page = articleService.getBackArticleList(params);
        return Result.ok().setData(page);
    }

    @ApiOperation("查出文章详细详细")
    @GetMapping("/info/{id}")
    public Result info(@NotNull @PathVariable("id") Long id){
        if (id.equals(1L) && !AuthorityUtils.hasAuthority("about-setting"))
            throw new NoPermissionsException();
        return Result.ok().setData(articleService.getArticle(id));
    }

    @CacheEvict(cacheNames = CacheConstant.ARTICLE_CACHE_NAME, allEntries = true)
    @RecordLog(type = RecordEnum.ARTICLE)
    @ApiOperation("保存或修改文章")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdateArticle(@Validated @RequestBody ArticleVo article) throws ExecutionException, InterruptedException {
        //当传入的ArticleVo没有id时表示创建新的文章
        //当传入的ArticleVo有id时表示修改文章
        articleService.saveArticle(article);
        return Result.ok();
    }

    @RecordLog(type = RecordEnum.DRAFT)
    @CacheEvict(cacheNames = CacheConstant.ARTICLE_CACHE_NAME,allEntries = true)
    @ApiOperation("保存文章为草稿")
    @PostMapping("/saveDraft")
    public Result saveDraftArticle(@NotNull @RequestBody ArticleDraftVo draftVo){
        articleService.saveDraft(draftVo);
        return Result.ok();
    }

    @ESEvict
    @RecordLog(type = RecordEnum.ARTICLE)
    @CacheEvict(cacheNames = CacheConstant.ARTICLE_CACHE_NAME,allEntries = true)
    @ApiOperation("删除文章")
    @GetMapping("/delete")
    public Result deleteArticle(@NotNull @RequestParam Long[] ids) throws ExecutionException, InterruptedException {
		articleService.deleteArticle(Arrays.asList(ids));
        return Result.ok();
    }
}
