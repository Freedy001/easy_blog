package com.freedy.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.freedy.backend.common.utils.PageUtils;
import com.freedy.backend.entity.ManagerEntity;

import java.util.Map;

/**
 * 管理员表
 *
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
public interface ManagerService extends IService<ManagerEntity> {

    PageUtils queryPage(Map<String, Object> params);

    public boolean checkLogin(String username, String password);
}

