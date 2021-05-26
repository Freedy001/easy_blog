package com.freedy.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.freedy.backend.utils.PageUtils;
import com.freedy.backend.entity.ManagerEntity;
import com.freedy.backend.entity.vo.manager.NewUserVo;
import com.freedy.backend.entity.vo.manager.UserInfoVo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 管理员表
 *
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
public interface ManagerService extends IService<ManagerEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取用户详细消息
     */
    UserInfoVo getUserInfo() throws ExecutionException, InterruptedException;

    /**
     * 创建用户
     */
    void createOrUpdateManager(NewUserVo manager) throws ExecutionException, InterruptedException;

    /**
     * 根据id删除用户
     */
    void deleteUserByIds(List<Integer> ids);

    /**
     * 获取用户 重要的消息 如账号邮箱权限等等
     */
    NewUserVo getUserImportantInfo(Integer id) throws Exception;

    /**
     *  创建根用户
     */
    void createRootUser();
}

