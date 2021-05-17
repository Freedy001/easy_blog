package com.freedy.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.freedy.backend.utils.PageUtils;
import com.freedy.backend.entity.RolePermissionEntity;

import java.util.List;
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

    /**
     * 根据管理员id查询权限 返回结果以逗号分割
     */
    String getPermissionsByManagerId(Integer id);

    /**
     * 根据用户id删除与该用户绑定的权限
     */
    void deletePermissionByUserIds(List<Integer> ids);

}

