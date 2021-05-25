package com.freedy.backend.scheduled;

import com.freedy.backend.constant.RedisConstant;
import com.freedy.backend.middleWare.es.dao.ArticleRepository;
import com.freedy.backend.middleWare.es.model.ArticleEsModel;
import com.freedy.backend.service.ArticleService;
import com.freedy.backend.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Freedy
 * @date 2021/5/9 0:22
 */
@Slf4j
@RestController
@EnableScheduling // 2.开启定时任务
public class ArticleSynchronize {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RestHighLevelClient highLevelClient;

    /**
     * 每天凌晨3点检查数据库与es 保证数据最终一致性
     */
    //todo 修改corn表达式
//    @Scheduled(cron = "0 0,10,20,30,40,50 * * * ?")
//    @Scheduled(cron = "0 * * * * ?")
    @GetMapping("/frontend/es")
    public String synchronizeArticleToEs() {
        log.info("开始检查ES与数据库中文章的数据!");
        int count = articleService.count();
        for (int i = 0; i < count / 100 + 1; i++) {
            List<ArticleEsModel> ids = articleService.getEsArticleList(i + 1, 100);
            ids.forEach(item -> {
                if (item.getId() != 1 && item.getArticleStatus() >= 3) {
                    Optional<ArticleEsModel> optional = articleRepository.findById(item.getId());
                    if (optional.isPresent()) {
                        if (!optional.get().equals(item)) {
                            log.info("数据不一致，开始同步 id:{}", item.getId());
                            articleRepository.save(item);
                        }
                    } else {
                        //数据不存在,判断是否到了发布时间 没到不管他
                        if (item.getPublishTime() < System.currentTimeMillis()) {
                            //过了发布时间 需要同步到es中
                            log.info("开始上架文章 id:{}", item.getId());
                            articleRepository.save(item);
                        }
                    }
                }
            });
        }
        return "ok";
    }

    //每十分钟统计一次
//    @Scheduled(cron = "0 0,10,20,30,40,50 * * * ?")
    //@Scheduled(cron = "0,20,40 * * * * ?")
    @Transactional(rollbackFor = Throwable.class)
    @GetMapping("/frontend/db")
    public String synchronizeArticleParameter() throws IOException {
        log.debug("开始统计数据");
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        {
            Set<String> keys = redisTemplate.keys(RedisConstant.ARTICLE_LIKE_HEADER + "*");
            Map<String, String> map = new HashMap<>(8);
            for (String key : Objects.requireNonNull(keys)) {
                String articleId = key.split(":", 2)[1];
                String redisLikeNum = ops.get(key);
                map.put(articleId, redisLikeNum);

                GetRequest getRequest = new GetRequest();
                getRequest.index("article");
                getRequest.id(articleId);
                getRequest.fetchSourceContext(new FetchSourceContext(true, new String[]{"likeNum"}, null));
                //查询出文章点赞数
                Map<String, Object> likeNumMap = highLevelClient.get(getRequest, RequestOptions.DEFAULT).getSourceAsMap();
                Integer likeNum = (Integer) likeNumMap.get("likeNum");
                likeNumMap.put("likeNum", likeNum + Integer.parseInt(redisLikeNum));
                UpdateRequest request = new UpdateRequest();
                request.index("article");
                request.id(articleId);
                request.doc(likeNumMap);
                // 更新文章点赞数
                highLevelClient.update(request, RequestOptions.DEFAULT);
            }
            articleService.addArticleParameter("like_num", map);
            redisTemplate.delete(keys);
        }
        {
            Set<String> keys = redisTemplate.keys(RedisConstant.ARTICLE_VISIT_HEADER + "*");
            Map<String, String> map = new HashMap<>(8);
            for (String key : Objects.requireNonNull(keys)) {
                String articleId = key.split(":", 2)[1];
                String redisVisitNum = ops.get(key);
                map.put(articleId, redisVisitNum);

                GetRequest getRequest = new GetRequest();
                getRequest.index("article");
                getRequest.id(articleId);
                getRequest.fetchSourceContext(new FetchSourceContext(true, new String[]{"visitNum"}, null));
                //查询出文章点赞数
                Map<String, Object> likeNumMap = highLevelClient.get(getRequest, RequestOptions.DEFAULT).getSourceAsMap();
                Integer visitNum = (Integer) likeNumMap.get("visitNum");
                likeNumMap.put("visitNum", visitNum + Integer.parseInt(redisVisitNum));
                UpdateRequest request = new UpdateRequest();
                request.index("article");
                request.id(articleId);
                request.doc(likeNumMap);
                // 更新文章点赞数
                highLevelClient.update(request, RequestOptions.DEFAULT);
            }
            articleService.addArticleParameter("visit_num", map);
            redisTemplate.delete(keys);
        }
        articleService.updateComment(commentService.getArticleCommentNum());
        return "ok";
    }


}
