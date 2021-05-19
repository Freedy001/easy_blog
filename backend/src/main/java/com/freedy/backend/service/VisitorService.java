package com.freedy.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.freedy.backend.utils.PageUtils;
import com.freedy.backend.entity.VisitorEntity;

import java.util.List;
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

    /**
     * 查询过去七天每天的访问量
     */
    List<Integer> visitorNumInPath7Days(long time);
}

