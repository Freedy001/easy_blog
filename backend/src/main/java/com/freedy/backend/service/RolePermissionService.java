package com.freedy.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.freedy.backend.common.utils.PageUtils;
import com.freedy.backend.entity.RolePermissionEntity;

import java.util.Map;

/**
 * 角色权限表
 *
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
public interface RolePermissionService extends IService<RolePermissionEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

