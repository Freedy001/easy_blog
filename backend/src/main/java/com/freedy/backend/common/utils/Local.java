package com.freedy.backend.common.utils;

import com.freedy.backend.entity.ManagerEntity;

/**
 * @author Freedy
 * @date 2021/5/4 17:19
 */
public class Local {
    public static final ThreadLocal<ManagerEntity> MANAGER_LOCAL=new ThreadLocal<>();
    public static final ThreadLocal<String> PERMISSION_LOCAL=new ThreadLocal<>();
}
