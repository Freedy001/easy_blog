package com.freedy.backend.entity.dto;

import com.freedy.backend.entity.ManagerEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * @author Freedy
 * @date 2021/4/29 8:53
 */
@Data
public class UserTokenInfo {
    //用户token消息
    private String token;
    //用户实体类
    private ManagerEntity manager;
    //用户权限
    private String permission;
}
