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
    private static StringRedisTemplate redisTemplate;

    @Autowired
    public void init(StringRedisTemplate redisTemplate){
        AuthorityUtils.redisTemplate =redisTemplate;
    }

    /**
     * 判断是否拥有给出的权限
     * @param authority 权限
     */
    public static boolean hasAuthority(String authority) {
        return hasAuthority(authority,Local.PERMISSION_LOCAL.get());
    }

    /**
     * 判断是否在permissionString里面 具有authority权限
     *
     * 使用此方法主要为在使用了异步时，无法获取到ThreadLocal
     * 则可以提前将permissionString字符串存储起来 然后通过此方法传入
     * 判断是否具有权限
     * @param authority 指定的权限
     * @param permissionString 权限字符串
     */
    public static boolean hasAuthority(String authority,String permissionString) {
        if (permissionString==null) return false;
        for (String grantedAuthority : permissionString.split(",")) {
            if (grantedAuthority.equals(authority)) {
                return true;
            }
        }
        return false;
    }

    public static boolean logout(){
        return logout(Local.MANAGER_LOCAL.get().getUsername());
    }

    public static boolean logout(String username){
        Boolean status = redisTemplate.delete(RedisConstant.USER_TOKEN_HEADER + username);
        return status != null && status;
    }
}
