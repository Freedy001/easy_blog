package com.freedy.backend.middleWare.mq.mqCreator;

import com.freedy.backend.constant.RabbitConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Freedy
 * @date 2021/5/1 21:06
 */
@Configuration
public class EsMqCreator {

    @Bean
    public Exchange orderEventExchange() {
        return new TopicExchange(RabbitConstant.ES_EXCHANGE_NAME,
                true, false);
    }

    @Bean
    public Queue OrderReleaseQueue() {
        return new Queue(RabbitConstant.ES_QUEUE_NAME,
                true, false, false);
    }

    @Bean
    public Binding orderCreateOrderBinding() {
        return new Binding(RabbitConstant.ES_QUEUE_NAME,
                Binding.DestinationType.QUEUE,
                RabbitConstant.ES_EXCHANGE_NAME,
                RabbitConstant.ES_ROUTE_KEY+".*", null);
    }

}
