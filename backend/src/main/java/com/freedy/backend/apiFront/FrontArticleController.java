package com.freedy.backend.apiFront;

import com.freedy.backend.constant.RabbitConstant;
import com.freedy.backend.utils.DateUtils;
import com.freedy.backend.utils.MarkDown;
import com.freedy.backend.utils.PageUtils;
import com.freedy.backend.utils.Result;
import com.freedy.backend.exception.NoArticleException;
import com.freedy.backend.middleWare.es.dao.ArticleRepository;
import com.freedy.backend.middleWare.es.model.ArticleEsModel;
import com.freedy.backend.service.ArticleService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @author Freedy
 * @date 2021/5/8 20:31
 */
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

    @ApiOperation("列出前台所有文章")
    @GetMapping("/list")
    public Result getArticleList(@RequestParam Map<String, Object> params) throws Exception {
        PageUtils page=articleService.getFrontArticleList(params);
        return Result.ok().setData(page);
    }

    @ApiOperation("获取文章详情")
    @GetMapping("/get")
    public Result getArticle(@RequestParam Long id){
        Optional<ArticleEsModel> optional = repository.findById(id);
        if (optional.isEmpty()){
            throw new NoArticleException();
        }
        ArticleEsModel esModel = optional.get();
        String html = MarkDown.render(esModel.getContent());
        esModel.setContent(html);
        HashMap<Object, Object> model = Arrays.stream(BeanUtils.getPropertyDescriptors(esModel.getClass()))
                .filter(itm -> !"class".equals(itm.getName()))
                .collect(HashMap::new,
                        (map, pd) -> map.put(pd.getName(), ReflectionUtils.invokeMethod(pd.getReadMethod(), esModel)),
                        HashMap::putAll);
        model.put("publishTime",DateUtils.formatChineseDate(esModel.getPublishTime()));
        return  Result.ok().setData(model);
    }

    @ApiOperation("给文章点赞")
    @GetMapping("/likeArticle")
    public Result likeArticle(@RequestParam Long id){
        rabbitTemplate.convertAndSend(RabbitConstant.THIRD_PART_EXCHANGE_NAME,RabbitConstant.ARTICLE_LIKE_ROUTING_KEY, id);
        return Result.ok();
    }

}
