package com.freedy.backend.entity.dto;

import com.freedy.backend.entity.ManagerEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.security.core.userdetails.User;


/**
 * @author Freedy
 * @date 2021/4/29 8:53
 */
@Data
public class UserTokenInfo{
    //用于springSecurity的验证
    private User user;
    //用户token消息
    private String token;
}
