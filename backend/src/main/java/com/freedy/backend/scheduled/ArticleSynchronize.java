package com.freedy.backend.scheduled;

import com.freedy.backend.middleWare.es.dao.ArticleRepository;
import com.freedy.backend.middleWare.es.model.ArticleEsModel;
import com.freedy.backend.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Freedy
 * @date 2021/5/9 0:22
 */
@Slf4j
@Component
@EnableScheduling // 2.开启定时任务
public class ArticleSynchronize {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleService articleService;

    /**
     * 每天凌晨3点检查数据库与es 保证数据一致性
     */
    //todo 修改corn表达式
    //@Scheduled(cron = "0 0,10,20,30,40,50 * * * ?")
    //@Scheduled(cron = "0 * * * * ?")
    public void synchronizeArticleEveryThreeClock(){
        log.info("开始检查ES与数据库中文章的数据!");
        int count = articleService.count();
        for (int i = 0; i < count/10+1; i++) {
            List<ArticleEsModel> ids=articleService.getEsArticleList(i+1,10);
            ids.forEach(item->{
                Optional<ArticleEsModel> optional = articleRepository.findById(item.getId());
                if (optional.isPresent()){
                    if (!optional.get().equals(item)){
                        log.info("数据不一致，开始同步 id:{}",item.getId());
                        articleRepository.save(item);
                    }
                }else {
                    //数据不存在,判断是否到了发布时间 没到不管他
                    if (item.getPublishTime()<new Date().getTime()){
                        //过了发布时间 需要同步到es中
                        log.info("开始上架文章 id:{}",item.getId());
                        articleRepository.save(item);
                    }
                }
            });
        }
    }


}
