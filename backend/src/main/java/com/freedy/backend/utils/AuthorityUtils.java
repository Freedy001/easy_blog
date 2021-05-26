package com.freedy.backend.utils;

import com.freedy.backend.constant.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Freedy
 * @date 2021/5/5 23:10
 */
@Component
public class AuthorityUtils {
    public static final Integer ROOT_ADMIN = 1;
    private static StringRedisTemplate redisTemplate;

    @Autowired
    public void init(StringRedisTemplate redisTemplate) {
        AuthorityUtils.redisTemplate = redisTemplate;
    }

    /**
     * 判断是否拥有给出的权限
     *
     * @param authority 权限
     */
    public static boolean hasAuthority(String authority) {
        return hasAnyOfAuthority(Local.PERMISSION_LOCAL.get(),authority);
    }

    /**
     * 判断是否在permissionString里面 具有authority权限
     * <p>
     * 使用此方法主要为在使用了异步时，无法获取到ThreadLocal
     * 则可以提前将permissionString字符串存储起来 然后通过此方法传入
     * 判断是否具有权限
     *
     * @param authority        指定的权限
     * @param permissionString 权限字符串
     */
    public static boolean hasAuthority(String permissionString,String authority) {
        return hasAnyOfAuthority(permissionString,authority);
    }


    public static boolean hasAnyAuthority(String... authority) {
        return hasAnyOfAuthority(Local.PERMISSION_LOCAL.get(),authority);
    }


    public static boolean hasAnyOfAuthority(String permissionString, String... authorities) {
        if (permissionString == null) return false;
        for (String grantedAuthority : permissionString.split(",")) {
            for (String authority : authorities) {
                if (grantedAuthority.equals(authority)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断所给的id是否是当前操作的用户
     */
    public static boolean isUser(Integer userId) {
        return Local.MANAGER_LOCAL.get().getId().equals(userId);
    }

    /**
     * 判断是否是根管理员
     */
    public static boolean isRoot() {
        return isUser(ROOT_ADMIN);
    }

    public static boolean logout() {
        return logout(Local.MANAGER_LOCAL.get().getUsername());
    }

    public static boolean logout(String username) {
        Boolean status = redisTemplate.delete(RedisConstant.USER_TOKEN_HEADER + username);
        return status != null && status;
    }
}
