package com.freedy.backend.Listenner;

import com.freedy.backend.constant.CacheConstant;
import com.freedy.backend.constant.EntityConstant;
import com.freedy.backend.constant.RabbitConstant;
import com.freedy.backend.constant.RedisConstant;
import com.freedy.backend.entity.dto.EsTypeDto;
import com.freedy.backend.enumerate.EsType;
import com.freedy.backend.enumerate.ResultCode;
import com.freedy.backend.middleWare.es.dao.ArticleRepository;
import com.freedy.backend.middleWare.es.model.ArticleEsModel;
import com.freedy.backend.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import com.rabbitmq.client.Channel;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


import static com.freedy.backend.constant.RabbitConstant.*;

/**
 * @author Freedy
 * @date 2021/5/1 21:17
 */
@Slf4j
@Component
public class ArticleEsListener {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RestHighLevelClient highLevelClient;

    @RabbitListener(queues = RabbitConstant.ES_QUEUE_NAME)
    public void handle(EsTypeDto entity, Message message, Channel channel) throws IOException {
        try {
            //保存消息到es
            if (entity.getType() == EsType.SAVE) preparePublish(entity);
            if (entity.getType() == EsType.UPDATE) {
                if (entity.getId() == 1) return;
                log.debug("修改文章{}", entity.getId());
                articleRepository.deleteById(entity.getId());
                preparePublish(entity);
                //通知前端下架页面
                redisTemplate.opsForValue().set(RedisConstant.NOTIFY_HEADER + UUID.randomUUID(),
                        ResultCode.NOTIFY_ARTICLE_UPDATE.name(), 5, TimeUnit.SECONDS);
            }
            if (entity.getType() == EsType.DELETE) {
                log.debug("删除文章{}", entity.getId());
                //通知前端下架页面
                redisTemplate.opsForValue().set(RedisConstant.NOTIFY_HEADER + UUID.randomUUID(),
                        ResultCode.NOTIFY_ARTICLE_UPDATE.name(), 5, TimeUnit.SECONDS);
                articleRepository.deleteById(entity.getId());
            }
            //手动确定
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

    /**
     * 发送消息到延迟队列
     * 在发布时间前一分钟 上架文章
     */
    private void preparePublish(EsTypeDto entity) {
        Date currentTime = new Date();
        long delayTime = entity.getPublishTime() - currentTime.getTime() - 1000 * 60;
        if (delayTime < 0) {
            delayTime = 0;
        }
        log.debug("当前时间：{},向延迟队列发送消息{}。延迟时间{}", new Date(), entity, delayTime);
        long finalDelayTime = delayTime;
        rabbitTemplate.convertAndSend(DELAYED_EXCHANGE_NAME, DELAYED_ROUTING_KEY, entity, item -> {
            item.getMessageProperties().setDelay(Math.toIntExact(finalDelayTime));
            return item;
        });
    }

    @CacheEvict(cacheNames = CacheConstant.ARTICLE_CACHE_NAME, allEntries = true)
    @RabbitListener(queues = DELAYED_QUEUE_NAME)
    public void receiveD(EsTypeDto entity, Message message, Channel channel) throws IOException {
        try {
            log.debug("当前时间：{},延时队列收到上架{}文章的消息", new Date(), entity.getId());
            Long id = entity.getId();
            ArticleEsModel esModel = articleService.getEsArticle(id);
            if (esModel != null && (esModel.getPublishTime() - 1000 * 61 < System.currentTimeMillis())) {
                if (entity.isOverHead()) {
                    //设置发布状态
                    articleService.updateArticleStatus(entity.getId(), EntityConstant.ARTICLE_OVERHEAD);
                    esModel.setArticleStatus(EntityConstant.ARTICLE_OVERHEAD);
                }else {
                    //设置发布状态
                    articleService.updateArticleStatus(entity.getId(), EntityConstant.ARTICLE_PUBLISHED);
                    esModel.setArticleStatus(EntityConstant.ARTICLE_PUBLISHED);
                }
                articleRepository.save(esModel);
                //邮件通知
                esModel.setContent(null);
                esModel.setArticleDesc(null);
                //邮件通知
                rabbitTemplate.convertAndSend(THIRD_PART_EXCHANGE_NAME, EMAIL_REPLAY_ROUTING_KEY, esModel);
                //通知前台
                redisTemplate.opsForValue().set(RedisConstant.NOTIFY_HEADER + UUID.randomUUID(),
                        ResultCode.NOTIFY_ARTICLE_UPDATE.name(), 5, TimeUnit.SECONDS);
                log.debug("文章{}上架成功", id);
            }
            //最终确认
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            e.printStackTrace();
        }
    }



}
