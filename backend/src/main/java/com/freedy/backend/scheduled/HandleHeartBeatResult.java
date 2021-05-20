package com.freedy.backend.scheduled;

import com.alibaba.fastjson.JSON;
import com.freedy.backend.constant.RedisConstant;
import com.freedy.backend.entity.VisitorEntity;
import com.freedy.backend.entity.dto.IpRegionDto;
import com.freedy.backend.service.VisitorService;
import com.freedy.backend.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

/**
 * @author Freedy
 * @date 2021/5/18 23:47
 */
@Slf4j
@Component
public class HandleHeartBeatResult {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private VisitorService service;

    /**
     * 每10分钟统计一次信息
     */
    @Scheduled(cron = "0 0,10,20,30,40,50 * * * ?")
    private void Handle() {
        Set<String> keys = redisTemplate.keys(RedisConstant.USER_IP_HEADER + "*");
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        if (keys != null && keys.size() != 0) {
            for (String key : keys) {
                String[] split = Objects.requireNonNull(ops.get(key)).split("-");
                long startTime = Long.parseLong(split[0]);
                long endTime = Long.parseLong(split[1]);
                if (endTime < System.currentTimeMillis() - 5 * 60 * 1000) {
                    //5分钟误差
                    //用户下线 在数据库中添加数据
                    VisitorEntity entity = new VisitorEntity();
                    String ipAddr = key.replace(RedisConstant.USER_IP_HEADER + ":", "");
                    entity.setIpAddress(ipAddr);
                    entity.setVisitTime(startTime);
                    entity.setVisitDuration(endTime - startTime);
                    String json = HttpUtil.get("https://ip.taobao.com/outGetIpInfo?ip=" + ipAddr);
                    IpRegionDto ipRegionDto = JSON.parseObject(json, IpRegionDto.class);
                    entity.setRegion(ipRegionDto.getCountry()+"-"+ipRegionDto.getRegion()+"-"+ipRegionDto.getCity());
                    service.save(entity);
                    redisTemplate.delete(key);
                }
            }
        }
    }

}
