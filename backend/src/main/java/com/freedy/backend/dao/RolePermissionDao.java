package com.freedy.backend.dao;

import com.freedy.backend.entity.RolePermissionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色权限表
 * 
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
@Mapper
public interface RolePermissionDao extends BaseMapper<RolePermissionEntity> {
	
}
