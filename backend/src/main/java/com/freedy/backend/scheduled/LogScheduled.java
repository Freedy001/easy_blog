package com.freedy.backend.scheduled;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.freedy.backend.entity.OperationLogEntity;
import com.freedy.backend.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Freedy
 * @date 2021/5/28 0:16
 */
@Slf4j
@RestController
@EnableScheduling // 2.开启定时任务
public class LogScheduled {

    @Autowired
    private OperationLogService service;

    /**
     * 删除一个月前的日志
     */
    @Scheduled(cron = "30 30 3 * * ?")
    public void deleteLog(){
        service.remove(new QueryWrapper<OperationLogEntity>()
                .lambda().lt(OperationLogEntity::getCreatTime,
                        System.currentTimeMillis()- 1000L *60*60*25*30));
    }


}
