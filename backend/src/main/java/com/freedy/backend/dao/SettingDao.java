package com.freedy.backend.dao;

import com.freedy.backend.entity.SettingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统设置表
 * 
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
@Mapper
public interface SettingDao extends BaseMapper<SettingEntity> {

    /**
     * 批量更新
     */
    void updateBatch(List<SettingEntity> entities);
}
