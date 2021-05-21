package com.freedy.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.freedy.backend.entity.SettingEntity;
import com.freedy.backend.entity.ShorthandEntity;
import com.freedy.backend.entity.vo.ShorthandItemVo;
import com.freedy.backend.utils.PageUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Freedy
 * @date 2021/5/20 1:56
 */
@Mapper
public interface ShorthandDao extends BaseMapper<ShorthandEntity> {
    /**
     * 查询shorthan列表
     * @param page 分页参数
     */
    List<ShorthandItemVo> getShorthandInfoList(PageUtils page);
}
