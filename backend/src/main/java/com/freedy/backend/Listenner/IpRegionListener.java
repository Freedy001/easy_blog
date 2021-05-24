package com.freedy.backend.Listenner;

import com.alibaba.fastjson.JSON;
import com.freedy.backend.constant.RabbitConstant;
import com.freedy.backend.entity.CommentEntity;
import com.freedy.backend.entity.dto.IpRegionDto;
import com.freedy.backend.entity.vo.comment.CommentVo;
import com.freedy.backend.enumerate.EsType;
import com.freedy.backend.service.ArticleService;
import com.freedy.backend.service.CommentService;
import com.freedy.backend.utils.HttpUtil;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Freedy
 * @date 2021/5/19 1:08
 */
@Component
public class IpRegionListener {

    @Autowired
    private CommentService service;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitConstant.IP_REGION_QUEUE_NAME)
    public void handle(CommentEntity comment,Message message, Channel channel) throws IOException {
        try {
            String ipAddr = comment.getIp();
            String json = HttpUtil.get("https://ip.taobao.com/outGetIpInfo?ip=" + ipAddr);
            IpRegionDto ipRegionDto = JSON.parseObject(json, IpRegionDto.class);
            comment.setRegion(ipRegionDto.getCountry()+"-"+ipRegionDto.getRegion()+"-"+ipRegionDto.getCity());
            articleService.addCommentNum(comment.getArticleId());
            service.updateById(comment);
            //手动确定
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

}
