package com.freedy.backend.dto;

import lombok.Data;
import org.springframework.security.core.userdetails.User;


/**
 * @author Freedy
 * @date 2021/4/29 8:53
 */
@Data
public class UserTokenInfo{
    private User user;
    private String token;
}
