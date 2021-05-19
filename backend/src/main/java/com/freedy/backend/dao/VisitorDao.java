package com.freedy.backend.dao;

import com.freedy.backend.entity.VisitorEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 访客记录表
 * 
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
@Mapper
public interface VisitorDao extends BaseMapper<VisitorEntity> {
    /**
     * 查询过去七天每天的访问量
     * @param time 当前时间
     */
    List<Integer> visitorNumInPath7Days(@Param("time") long time);
}
