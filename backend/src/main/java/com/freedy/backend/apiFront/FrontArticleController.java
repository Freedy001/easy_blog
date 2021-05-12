package com.freedy.backend.apiFront;

import com.alibaba.fastjson.TypeReference;
import com.freedy.backend.common.utils.MarkDown;
import com.freedy.backend.common.utils.PageUtils;
import com.freedy.backend.common.utils.Result;
import com.freedy.backend.entity.vo.ArticleInfoVo;
import com.freedy.backend.exception.NoArticleException;
import com.freedy.backend.middleWare.es.dao.ArticleRepository;
import com.freedy.backend.middleWare.es.model.ArticleEsModel;
import com.freedy.backend.service.ArticleService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.freedy.backend.constant.RabbitConstant.DELAYED_EXCHANGE_NAME;
import static com.freedy.backend.constant.RabbitConstant.DELAYED_ROUTING_KEY;

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

    @ApiOperation("列出前台所有文章")
    @GetMapping("/list")
    @Cacheable(cacheNames = "article",sync = true)
    public Result getArticleList(@RequestParam Map<String, Object> params) throws ExecutionException, InterruptedException {
        PageUtils page=articleService.getFrontArticleList(params);
        return Result.ok().setData(page);
    }

    @GetMapping("/get")
    public Result getArticle(@RequestParam Long id){
        Optional<ArticleEsModel> optional = repository.findById(id);
        if (optional.isEmpty()){
            throw new NoArticleException();
        }
        ArticleEsModel esModel = optional.get();
        String html = MarkDown.render(esModel.getContent());
        esModel.setContent(html);
        return  Result.ok().setData(esModel);
    }

}
