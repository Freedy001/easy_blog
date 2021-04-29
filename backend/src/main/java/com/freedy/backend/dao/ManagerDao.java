package com.freedy.backend.dao;

import com.freedy.backend.entity.ManagerEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 管理员表
 * 
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
@Mapper
public interface ManagerDao extends BaseMapper<ManagerEntity> {
	
}
