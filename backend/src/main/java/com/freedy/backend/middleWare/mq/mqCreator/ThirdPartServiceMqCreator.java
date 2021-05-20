package com.freedy.backend.middleWare.mq.mqCreator;

import com.freedy.backend.constant.RabbitConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Freedy
 * @date 2021/5/4 22:40
 */
@Configuration
public class ThirdPartServiceMqCreator {
    @Bean
    public Exchange emailExchange() {
        return new DirectExchange(RabbitConstant.THIRD_PART_EXCHANGE_NAME,
                true, false);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(RabbitConstant.EMAIL_REPLAY_QUEUE_NAME,
                true, false, false);
    }

    @Bean
    public Binding emailBinding() {
        return new Binding(RabbitConstant.EMAIL_REPLAY_QUEUE_NAME,
                Binding.DestinationType.QUEUE,
                RabbitConstant.THIRD_PART_EXCHANGE_NAME,
                RabbitConstant.EMAIL_REPLAY_ROUTING_KEY,
                null);
    }

    @Bean
    public Queue ipQueue() {
        return new Queue(RabbitConstant.IP_REGION_QUEUE_NAME,
                true, false, false);
    }

    @Bean
    public Binding ipBinding() {
        return new Binding(RabbitConstant.IP_REGION_QUEUE_NAME,
                Binding.DestinationType.QUEUE,
                RabbitConstant.THIRD_PART_EXCHANGE_NAME,
                RabbitConstant.IP_REGION_ROUTING_KEY,
                null);
    }

    @Bean
    public Queue articleLikeQueue() {
        return new Queue(RabbitConstant.ARTICLE_LIKE_QUEUE_NAME,
                true, false, false);
    }

    @Bean
    public Binding articleLikeBinding() {
        return new Binding(RabbitConstant.ARTICLE_LIKE_QUEUE_NAME,
                Binding.DestinationType.QUEUE,
                RabbitConstant.THIRD_PART_EXCHANGE_NAME,
                RabbitConstant.ARTICLE_LIKE_ROUTING_KEY,
                null);
    }
}
