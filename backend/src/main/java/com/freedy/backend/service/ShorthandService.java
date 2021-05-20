package com.freedy.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.freedy.backend.entity.ShorthandEntity;
import com.freedy.backend.utils.PageUtils;

import java.util.Map;

/**
 * @author Freedy
 * @date 2021/5/20 1:57
 */
public interface ShorthandService extends IService<ShorthandEntity> {
    /**
     * 分页查询
     */
    PageUtils queryPage( Map<String, Object> param);

}
