package com.freedy.backend.Listenner;

import com.freedy.backend.constant.RedisConstant;
import com.freedy.backend.entity.SubscriberEntity;
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

    @Value("#{loadSetting.replayNotification}")
    private Boolean replayNotification;

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
                        "/article?id=" + fatherEntity.getArticleId()
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
}
