package com.freedy.backend.apiFront;

import com.freedy.backend.constant.CacheConstant;
import com.freedy.backend.constant.RedisConstant;
import com.freedy.backend.entity.ArticleEntity;
import com.freedy.backend.utils.*;
import com.freedy.backend.exception.NoArticleException;
import com.freedy.backend.middleWare.es.dao.ArticleRepository;
import com.freedy.backend.middleWare.es.model.ArticleEsModel;
import com.freedy.backend.service.ArticleService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @author Freedy
 * @date 2021/5/8 20:31
 */
@Validated
@Slf4j
@RestController
@RequestMapping("/frontend/article")
public class FrontArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository repository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @ApiOperation("列出前台所有文章")
    @GetMapping("/list")
    public Result getArticleList(@RequestParam Map<String, Object> params) throws Exception {
        PageUtils page=articleService.getFrontArticleList(params);
        return Result.ok().setData(page);
    }

    @ApiOperation("获取文章详情")
    @GetMapping("/get")
    public Result getArticle(@NotNull @RequestParam Long id){
        if (id==1){
            ArticleEntity about = articleService.getAbout();
            about.setContent(MarkDown.render(about.getContent()));
            return Result.ok().setData(about);
        }
        Optional<ArticleEsModel> optional = repository.findById(id);
        if (optional.isEmpty()){
            throw new NoArticleException();
        }
        return buildResult(optional.get());
    }

    @ApiOperation("获取文章详情")
    @GetMapping("/getByTitle")
    public Result getArticleByArticleTitle(@NotEmpty String title){
        ArticleEsModel optional = repository.findByTitle(title);
        if (optional==null){
            throw new NoArticleException();
        }
        return buildResult(optional);
    }

    /**
     * 构建文章实体类
     */
    private Result buildResult(ArticleEsModel esModel) {
        //增加访问数
        redisTemplate.opsForValue().increment(RedisConstant.ARTICLE_VISIT_HEADER+esModel.getId());
        //解析markdown
        String html = MarkDown.render(esModel.getContent());
        esModel.setContent(html);
        //同步访客数
        String redisVisit = redisTemplate.opsForValue().get(RedisConstant.ARTICLE_VISIT_HEADER + esModel.getId());
        if (StringUtils.hasText(redisVisit)) {
            esModel.setVisitNum(esModel.getVisitNum() + Integer.parseInt(redisVisit));
        }
        //同步点赞数
        String redisLike = redisTemplate.opsForValue().get(RedisConstant.ARTICLE_LIKE_HEADER + esModel.getId());
        if (StringUtils.hasText(redisLike)) {
            esModel.setLikeNum(esModel.getLikeNum() + Integer.parseInt(redisLike));
        }
        //设置默认图片
        if (esModel.getArticlePoster() == null) esModel.setArticlePoster("/resource/pexels-johannes-plenio-3421812.jpg");
        HashMap<Object, Object> model = Arrays.stream(BeanUtils.getPropertyDescriptors(esModel.getClass()))
                .filter(itm -> !"class".equals(itm.getName()))
                .collect(HashMap::new,
                        (map, pd) -> map.put(pd.getName(), ReflectionUtils.invokeMethod(pd.getReadMethod(), esModel)),
                        HashMap::putAll);
        model.put("publishTime", DateUtils.formatChineseDate(esModel.getPublishTime()));
        return Result.ok().setData(model);
    }


    @ApiOperation("给文章点赞")
    @GetMapping("/likeArticle")
    public Result likeArticle(@NotNull @RequestParam Long id){
        redisTemplate.opsForValue().increment(RedisConstant.ARTICLE_LIKE_HEADER+id);
        return Result.ok();
    }

    @CacheEvict(cacheNames = CacheConstant.ARTICLE_CACHE_NAME,allEntries = true)
    @GetMapping("/refreshTheArticleAndClearTheCache")
    public Result refresh(){
        return Result.ok();
    }
}
