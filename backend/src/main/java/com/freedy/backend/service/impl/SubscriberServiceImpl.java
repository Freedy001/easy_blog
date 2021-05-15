package com.freedy.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedy.backend.dao.SubscriberDao;
import com.freedy.backend.entity.SubscriberEntity;
import com.freedy.backend.service.SubscriberService;
import org.springframework.stereotype.Service;

/**
 * @author Freedy
 * @date 2021/5/15 20:33
 */
@Service
public class SubscriberServiceImpl extends ServiceImpl<SubscriberDao, SubscriberEntity> implements SubscriberService {
}
