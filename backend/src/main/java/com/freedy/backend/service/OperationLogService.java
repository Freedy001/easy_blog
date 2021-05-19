package com.freedy.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.freedy.backend.entity.OperationLogEntity;
import com.freedy.backend.utils.PageUtils;

import java.util.Map;

/**
 * @author Freedy
 * @date 2021/5/18 11:47
 */
public interface OperationLogService extends IService<OperationLogEntity> {
    /**
     * 分页查询
     */
    PageUtils queryPage(Map<String, Object> param);
}
