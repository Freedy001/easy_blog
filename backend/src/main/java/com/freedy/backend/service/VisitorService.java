package com.freedy.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.freedy.backend.common.utils.PageUtils;
import com.freedy.backend.entity.VisitorEntity;

import java.util.Map;

/**
 * 访客记录表
 *
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
public interface VisitorService extends IService<VisitorEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

