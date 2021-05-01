package com.freedy.backend.config.security;


import com.alibaba.fastjson.JSON;
import com.freedy.backend.common.utils.JwtTokenUtil;
import com.freedy.backend.constant.RedisConstant;
import com.freedy.backend.dto.UserTokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * 自定义的Token过滤器
 * 这个过滤器的主要作用是为了在用户登录并获取到我们发配的token之后，
 * 在带着token发送请求时，我们要检验token，判断它是否携带着token，token是否过期，
 * token中的用户是否包含在我们的数据库中等等，如果token有效，
 * 则直接让Spring Security形成安全上下文，不再进行验证
 *
 * @author Freedy
 * @date 2021/4/28 13:29
 */
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private JwtProperties jwtProperties;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String authToken = httpServletRequest.getHeader(jwtProperties.getHeader());
        String username = jwtTokenUtil.getUsernameFromToken(authToken);
        log.debug("进入自定义过滤器");
        log.debug("自定义过滤器获得用户名为   " + username);

        //当token中的username不为空时进行验证token是否是有效的token
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //token中username不为空，并且Context中的认证为空，进行token验证
            //从redis得到带有密码的完整user信息
            ValueOperations<String, String> ops = redisTemplate.opsForValue();
            String userToken = ops.get(RedisConstant.USER_TOKEN_HEADER + username);
            UserTokenInfo tokenInfo = JSON.parseObject(userToken, UserTokenInfo.class);
            if (tokenInfo!=null&jwtTokenUtil.validateToken(authToken, tokenInfo)) {
                //如username不为空，并且能够在数据库中查到
                /**
                 * UsernamePasswordAuthenticationToken继承
                 * AbstractAuthenticationToken实现Authentication
                 * 所以当在页面中输入用户名和密码之后首先会进入到
                 * UsernamePasswordAuthenticationToken验证(Authentication)，
                 * 然后生成的Authentication会被交由AuthenticationManager来进行管理
                 * 而AuthenticationManager管理一系列的AuthenticationProvider，
                 * 而每一个Provider都会通UserDetailsService和UserDetail来返回一个
                 * 以UsernamePasswordAuthenticationToken实现的带用户名和密码以及权限的
                 * Authentication
                 */
                User user = tokenInfo.getUser();
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user,
                                null, user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(httpServletRequest));
                //将authentication放入SecurityContextHolder中
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}
