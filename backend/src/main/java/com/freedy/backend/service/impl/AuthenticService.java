package com.freedy.backend.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.freedy.backend.aspect.annotation.RecordLog;
import com.freedy.backend.constant.CacheConstant;
import com.freedy.backend.constant.RedisConstant;
import com.freedy.backend.dao.ManagerDao;
import com.freedy.backend.entity.ManagerEntity;
import com.freedy.backend.entity.OperationLogEntity;
import com.freedy.backend.entity.dto.UserTokenInfo;
import com.freedy.backend.enumerate.RecordEnum;
import com.freedy.backend.enumerate.ResultCode;
import com.freedy.backend.service.RolePermissionService;
import com.freedy.backend.utils.Local;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Freedy
 * @date 2021/5/4 16:06
 */
@Slf4j
@Service
public class AuthenticService implements UserDetailsService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ManagerDao managerDao;
    @Autowired
    private RolePermissionService permissionService;

    /**
     * springSecurity 权限认证
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ManagerEntity userEntity = managerDao.selectOne(new QueryWrapper<ManagerEntity>().eq("username", username));
        if (userEntity==null){
            throw new UsernameNotFoundException(ResultCode.USER_ACCOUNT_NOT_EXIST.getStrCode());
        }
        String permission=permissionService.getPermissionsByManagerId(userEntity.getId());
        List<GrantedAuthority> role = AuthorityUtils.
                commaSeparatedStringToAuthorityList(permission);
        User user = new User(userEntity.getUsername(), userEntity.getPassword(), role);
        //存储完整user 因为验证的时候需要
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        UserTokenInfo info = new UserTokenInfo();
        info.setPermission(permission);
        info.setManager(userEntity);
        ops.set(RedisConstant.USER_TOKEN_HEADER+user.getUsername(),
                JSON.toJSONString(info));
        for (GrantedAuthority authority : user.getAuthorities()) {
            log.debug("用户拥有{}权限",authority.getAuthority());
        }
        return user;
    }
}
