package com.freedy.backend.config;

import com.alibaba.fastjson.support.spring.messaging.MappingFastJsonMessageConverter;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author Freedy
 * @date 2021/5/2 22:01
 */
@Configuration
public class rabbitConfig {
    @Autowired
    RabbitTemplate template;

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @PostConstruct//RabbitConfig对象创建完成后,执行这个方法
    public void initRabbitTemplate() {
        //消息队列发送成功确认机制
        template.setConfirmCallback((data, ack, err) -> {
            System.out.println("confirm");
            System.out.println(data);
            System.out.println(ack);
            System.out.println(err);
        });
    }

}
