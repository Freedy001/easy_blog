package com.freedy.backend.common.utils;

import com.freedy.backend.constant.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

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

    public static boolean hasAuthority(String authority) {
        return hasAuthority(authority,Local.PERMISSION_LOCAL.get());
    }

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
