package com.freedy.backend.middleWare.mq.mqCreator;

import com.freedy.backend.constant.RabbitConstant;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.freedy.backend.constant.RabbitConstant.DELAYED_QUEUE_NAME;
import static com.freedy.backend.constant.RabbitConstant.DELAYED_EXCHANGE_NAME;
import static com.freedy.backend.constant.RabbitConstant.DELAYED_ROUTING_KEY;

/**
 * @author Freedy
 * @date 2021/5/1 21:06
 */
@Configuration
public class EsMqCreator {

    @Bean
    public Exchange ESExchange() {
        return new TopicExchange(RabbitConstant.ES_EXCHANGE_NAME,
                true, false);
    }

    @Bean
    public Queue ESQueue() {
        return new Queue(RabbitConstant.ES_QUEUE_NAME,
                true, false, false);
    }

    @Bean
    public Binding ESBinding() {
        return new Binding(RabbitConstant.ES_QUEUE_NAME,
                Binding.DestinationType.QUEUE,
                RabbitConstant.ES_EXCHANGE_NAME,
                RabbitConstant.ES_ROUTE_KEY+".*", null);
    }

    @Bean
    public Queue immediateQueue() {
        return new Queue(DELAYED_QUEUE_NAME);
    }

    @Bean
    public CustomExchange customExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE_NAME,
                "x-delayed-message", true, false, args);
    }

    @Bean
    public Binding bindingNotify(@Qualifier("immediateQueue") Queue queue,
                                 @Qualifier("customExchange") CustomExchange customExchange) {
        return BindingBuilder.bind(queue).to(customExchange).with(DELAYED_ROUTING_KEY).noargs();
    }

}
