package com.freedy.backend.middleWare.es;

import com.freedy.backend.constant.RabbitConstant;
import com.freedy.backend.entity.ArticleEntity;
import com.freedy.backend.entity.dto.EsTypeDto;
import com.freedy.backend.entity.vo.ArticleVo;
import lombok.extern.slf4j.Slf4j;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.Arrays;

/**
 * @author Freedy
 * @date 2021/5/1 21:17
 */
@Slf4j
@Component
@RabbitListener(queues = RabbitConstant.ES_QUEUE_NAME)
public class ArticleEs {

    @RabbitHandler
    public void handle(EsTypeDto entity, Message message, Channel channel) throws IOException {
        try {
            log.debug("ArticleEs收到消息{}",entity.getType().name());
            //保存消息到es
            //.......

            //手动确定
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

}
