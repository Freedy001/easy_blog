package com.freedy.backend.Listenner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.freedy.backend.SysSetting.LoadSetting;
import com.freedy.backend.constant.EntityConstant;
import com.freedy.backend.constant.RedisConstant;
import com.freedy.backend.entity.SubscriberEntity;
import com.freedy.backend.middleWare.es.model.ArticleEsModel;
import com.freedy.backend.service.ArticleService;
import com.freedy.backend.service.SubscriberService;
import com.freedy.backend.utils.DateUtils;
import com.freedy.backend.utils.EmailSender;
import com.freedy.backend.constant.RabbitConstant;
import com.freedy.backend.entity.CommentEntity;
import com.freedy.backend.service.CommentService;
import com.freedy.backend.utils.EmailTemplate;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
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
 *
 * @author Freedy
 * @date 2021/5/17 16:12
 */
@Slf4j
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

    @Autowired
    private ArticleService articleService;

    @Autowired
    private LoadSetting setting;

    @RabbitHandler
    public void commentEmailSender(CommentEntity commentEntity, Message message, Channel channel) throws IOException {
        try {
            if (setting.getNewCommentNotification()) {
                //新评论通知作者
                String email = articleService.getCreatorEmailByArticleId(commentEntity.getArticleId());
                sender.sendHtml(email, "有人在你的文章下面评论了😀", EmailTemplate.commentArticleTemplate(
                        commentEntity.getUsername(),
                        DateUtils.formatTime(commentEntity.getCreateTime()),
                        commentEntity.getContent(),
                        setting.getWebSiteDomainName() + "/article?id=" + commentEntity.getArticleId()
                ));
                //防止下面return 一直无法消费
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
            if (EntityConstant.COMMENT_EXAMINATION.equals(commentEntity.getCommentStatus())) {
                return;
            }
            commentNotify(commentEntity);
            //手动确定
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("发生异常{}",e.getMessage());
            e.printStackTrace();
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        }
    }


    @RabbitHandler
    public void subscriberSender(SubscriberEntity entity, Message message, Channel channel) throws IOException {
        try {
            String varietyCode = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
            sender.sendHtml(entity.getSubscriberEmail(), "订阅", EmailTemplate.subscribeTemplate(entity.getSubscriberEmail(), varietyCode));
            redisTemplate.opsForValue().set(RedisConstant.SUBSCRIBE_HEADER + entity.getUUID(), varietyCode + "-" + entity.getSubscriberEmail(), 30, TimeUnit.MINUTES);
            //手动确定
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("发生异常{}",e.getMessage());
            e.printStackTrace();
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

    @RabbitHandler
    public void articleNotifySender(ArticleEsModel esModel, Message message, Channel channel) throws IOException {
        try {
            int count = subscriberService.count();
            for (int i = 0; i < count / 100 + 1; i++) {
                Page<SubscriberEntity> page = new Page<>();
                page.setSize(100);
                page.setPages(i + 1);
                List<SubscriberEntity> subscriberList = subscriberService.page(page).getRecords();
                for (SubscriberEntity entity : subscriberList) {
                    sender.sendHtml(
                            entity.getSubscriberEmail(),
                            "您关注的博客更新文章了",
                            EmailTemplate.articleNotifyTemplate(
                                    setting.getWebSiteDomainName() + "/article?id=" + esModel.getId(),
                                    esModel.getTitle())
                    );
                }
            }
            //手动确定
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("发生异常{}",e.getMessage());
            e.printStackTrace();
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

    @RabbitHandler
    public void confirmExaminationsSender(List<Long> asList, Message message, Channel channel) throws IOException {
        try {
            service.listByIds(asList).forEach(this::commentNotify);
            //手动确定
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("发生异常{}",e.getMessage());
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        }
    }


    private void commentNotify(CommentEntity sonComment) {
        //评论回复通知对方
        if (setting.getReplayNotification()&&sonComment.getFatherCommentId()!=null) {
            CommentEntity fatherEntity = service.getById(sonComment.getFatherCommentId());
            sender.sendHtml(fatherEntity.getEmail(), "有人回复你的评论啦😀", EmailTemplate.replayTemplate(
                    fatherEntity.getUsername(),
                    DateUtils.formatTime(sonComment.getCreateTime()),
                    sonComment.getUsername(),
                    fatherEntity.getContent(),
                    sonComment.getContent(),
                    setting.getWebSiteDomainName() + "/article?id=" + fatherEntity.getArticleId()
            ));
        }
    }
}
