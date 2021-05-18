package com.freedy.backend.constant;

/**
 * @author Freedy
 * @date 2021/5/1 21:21
 */
public class RabbitConstant {
    //与es相关的队列与交换机
    public static final String ES_EXCHANGE_NAME="es.exchange";
    public static final String ES_QUEUE_NAME="es.queue";
    public static final String ES_ROUTE_KEY="es";
    //延迟队列
    public static final String DELAYED_QUEUE_NAME = "delay.article.delay.queue";
    public static final String DELAYED_EXCHANGE_NAME = "delay.article.delay.exchange";
    public static final String DELAYED_ROUTING_KEY = "delay.article.delay.routing.key";
    //与邮件系统相关的队列与交换机
    public static final String THIRD_PART_EXCHANGE_NAME="third.part.exchange";
    public static final String EMAIL_REPLAY_QUEUE_NAME="email.replay.sender.queue";
    public static final String EMAIL_REPLAY_ROUTING_KEY="email.replay.sender.routing.key";

    public static final String IP_REGION_QUEUE_NAME="email.replay.sender.queue";
    public static final String IP_REGION_ROUTING_KEY="email.replay.sender.routing.key";
}
