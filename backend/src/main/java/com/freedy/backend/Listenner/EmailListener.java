package com.freedy.backend.Listenner;

import com.freedy.backend.utils.DateUtils;
import com.freedy.backend.utils.EmailSender;
import com.freedy.backend.constant.RabbitConstant;
import com.freedy.backend.entity.CommentEntity;
import com.freedy.backend.service.CommentService;
import com.freedy.backend.utils.EmailTemplate;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 使用异步方式发送邮件
 * @author Freedy
 * @date 2021/5/17 16:12
 */
@Component
public class EmailListener {
    @Autowired
    private EmailSender sender;

    @Autowired
    private CommentService service;

    @Value("#{loadSetting.replayNotification}")
    private Boolean replayNotification;

    @RabbitListener(queues = RabbitConstant.EMAIL_REPLAY_QUEUE_NAME)
    public void EmailSender(CommentEntity commentEntity, Message message, Channel channel) throws IOException {
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


}
