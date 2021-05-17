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
public class EmailMqCreator {
    @Bean
    public Exchange EmailExchange() {
        return new DirectExchange(RabbitConstant.EMAIL_EXCHANGE_NAME,
                true, false);
    }

    @Bean
    public Queue EmailQueue() {
        return new Queue(RabbitConstant.EMAIL_REPLAY_QUEUE_NAME,
                true, false, false);
    }

    @Bean
    public Binding EmailBinding() {
        return new Binding(RabbitConstant.EMAIL_REPLAY_QUEUE_NAME,
                Binding.DestinationType.QUEUE,
                RabbitConstant.EMAIL_EXCHANGE_NAME,
                RabbitConstant.EMAIL_REPLAY_ROUTING_KEY,
                null);
    }
}
