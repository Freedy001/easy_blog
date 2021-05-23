package com.freedy.backend.Listenner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.freedy.backend.constant.RedisConstant;
import com.freedy.backend.entity.SubscriberEntity;
import com.freedy.backend.middleWare.es.model.ArticleEsModel;
import com.freedy.backend.service.SubscriberService;
import com.freedy.backend.utils.DateUtils;
import com.freedy.backend.utils.EmailSender;
import com.freedy.backend.constant.RabbitConstant;
import com.freedy.backend.entity.CommentEntity;
import com.freedy.backend.service.CommentService;
import com.freedy.backend.utils.EmailTemplate;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 使用异步方式发送邮件
 * @author Freedy
 * @date 2021/5/17 16:12
 */
@Component
@RabbitListener(queues = RabbitConstant.EMAIL_REPLAY_QUEUE_NAME)
public class EmailListener {
    @Autowired
    private EmailSender sender;

    @Autowired
    private CommentService service;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SubscriberService subscriberService;

    @Value("#{loadSetting.replayNotification}")
    private Boolean replayNotification;

    @Value("#{loadSetting.webSiteDomainName}")
    private String webSiteDomainName;


    @RabbitHandler
    public void commentEmailSender(CommentEntity commentEntity, Message message, Channel channel) throws IOException {
        try {
            if (replayNotification){
                CommentEntity fatherEntity = service.getById(commentEntity.getFatherCommentId());
                sender.sendHtml(fatherEntity.getEmail(), "有人回复你的评论啦😀", EmailTemplate.replayTemplate(
                        fatherEntity.getUsername(),
                        DateUtils.formatTime(commentEntity.getCreateTime()),
                        commentEntity.getUsername(),
                        fatherEntity.getContent(),
                        commentEntity.getContent(),
                        webSiteDomainName+"/article?id=" + fatherEntity.getArticleId()
                ));
                //手动确定
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }catch (Exception e){
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

    @RabbitHandler
    public void subscriberSender(SubscriberEntity entity , Message message, Channel channel) throws IOException {
        try {
            String varietyCode = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
            sender.sendHtml(entity.getSubscriberEmail(),"订阅",EmailTemplate.subscribeTemplate(entity.getSubscriberEmail(),varietyCode));
            redisTemplate.opsForValue().set(RedisConstant.SUBSCRIBE_HEADER+entity.getUUID(),varietyCode+"-"+entity.getSubscriberEmail(),30, TimeUnit.MINUTES);
            //手动确定
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }catch (Exception e){
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

    @RabbitHandler
    public void articleNotifySender(ArticleEsModel esModel , Message message, Channel channel) throws IOException {
        try {
            int count = subscriberService.count();
            for (int i = 0; i < count / 100 + 1; i++) {
                Page<SubscriberEntity> page = new Page<>();
                page.setSize(100);
                page.setPages(i+1);
                List<SubscriberEntity> subscriberList = subscriberService.page(page).getRecords();
                for (SubscriberEntity entity : subscriberList) {
                    sender.sendHtml(
                            entity.getSubscriberEmail(),
                            "您关注的博客更新文章了",
                            EmailTemplate.articleNotifyTemplate(
                                    webSiteDomainName+"/article?id=" + esModel.getId(),
                                    esModel.getTitle())
                    );
                }
            }
            //手动确定
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }catch (Exception e){
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}
