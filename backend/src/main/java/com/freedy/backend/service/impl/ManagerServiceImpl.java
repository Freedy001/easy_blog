package com.freedy.backend.service.impl;

import com.alibaba.fastjson.JSON;
import com.freedy.backend.constant.RedisConstant;
import com.freedy.backend.entity.dto.UserTokenInfo;
import com.freedy.backend.enumerate.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedy.backend.common.utils.PageUtils;
import com.freedy.backend.common.utils.Query;

import com.freedy.backend.dao.ManagerDao;
import com.freedy.backend.entity.ManagerEntity;
import com.freedy.backend.service.ManagerService;


@Service("managerService")
public class ManagerServiceImpl extends ServiceImpl<ManagerDao, ManagerEntity> implements ManagerService, UserDetailsService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ManagerEntity> page = this.page(
                new Query<ManagerEntity>().getPage(params),
                new QueryWrapper<ManagerEntity>()
        );

        return new PageUtils(page);
    }

    public boolean checkLogin(String username, String rawPassword){
        ManagerEntity user = baseMapper.selectOne(new QueryWrapper<ManagerEntity>().eq("username", username));
        if (user==null){
            throw new RuntimeException("账号不存在!");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(rawPassword, user.getPassword())){
            throw new RuntimeException("密码不存在!");
        }
        return true;
    }

    /**
     * springSecurity 权限认证
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> role = AuthorityUtils.
                commaSeparatedStringToAuthorityList("role");
        ManagerEntity userEntity = baseMapper.selectOne(new QueryWrapper<ManagerEntity>().eq("username", username));
        if (userEntity==null){
           throw new UsernameNotFoundException(ResultCode.USER_ACCOUNT_NOT_EXIST.getStrCode());
        }
        User user = new User(userEntity.getUsername(), userEntity.getPassword(), role);
        //存储完整user 因为验证的时候需要
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        UserTokenInfo info = new UserTokenInfo();
        info.setUser(user);
        ops.set(RedisConstant.USER_TOKEN_HEADER+user.getUsername(),
                JSON.toJSONString(info));
        return user;
    }
}