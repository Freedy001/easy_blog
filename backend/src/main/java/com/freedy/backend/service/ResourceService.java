package com.freedy.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.freedy.backend.common.utils.PageUtils;
import com.freedy.backend.entity.ResourceEntity;

import java.util.Map;

/**
 * @author Freedy
 * @date 2021/5/7 13:00
 */
public interface ResourceService extends IService<ResourceEntity> {
    /**
     * 分页查询
     */
    PageUtils queryPage(Map<String, Object> params);
}
