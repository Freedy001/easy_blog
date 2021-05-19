package com.freedy.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.freedy.backend.entity.SettingEntity;
import com.freedy.backend.entity.ShorthandEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Freedy
 * @date 2021/5/20 1:56
 */
@Mapper
public interface ShorthandDao extends BaseMapper<ShorthandEntity> {
}
