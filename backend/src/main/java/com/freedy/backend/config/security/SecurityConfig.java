package com.freedy.backend.config.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * @author Freedy
 * @date 2021/4/27 20:31
 */
@Slf4j
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    private MyAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private MyAuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private MyLogoutHandler myLogoutHandler;

    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 自定义的Jwt Token过滤器
    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                //自定义认证成功处理器
                .successHandler(authenticationSuccessHandler)
                // 自定义失败拦截器
                .failureHandler(authenticationFailureHandler)
                // 自定义登录拦截URI
                .loginProcessingUrl("/backend/login")
                .and()
                //token的验证方式不需要开启csrf的防护
                .csrf().disable()
                // 自定义认证失败类
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                // 自定义权限不足处理类
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                //设置无状态的连接,即不创建session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/backend/login").permitAll()
                .antMatchers("/v2/api-docs",
                        "/swagger-resources/configuration/ui",
                        "/swagger-resources",
                        "/swagger-resources/configuration/security",
                        "/swagger-ui.html").permitAll()
                //允许根路径url的访问
                .antMatchers("/").permitAll()
                .antMatchers("/webjars/**").permitAll()
                //允许swagger-ui.html访问
                .antMatchers("/swagger-ui.html").permitAll()
                //允许静态文件访问
                .antMatchers("/image/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/resource/**").permitAll()
                //允许前台页面访问
                .antMatchers("/frontend/**").permitAll()
                .antMatchers("/admin/**").permitAll()
                .antMatchers("/assets/**").permitAll()
                //配置允许匿名访问的路径
                .anyRequest().authenticated();
        // 解决跨域问题（重要）  只有在前端请求接口时才发现需要这个
        http.cors().and().csrf().disable();

        //配置自己的jwt验证过滤器
        http.addFilterBefore(authenticationTokenFilterBean(),
                UsernamePasswordAuthenticationFilter.class);

        http.logout().logoutUrl("/backend/logout")
                .addLogoutHandler(myLogoutHandler)
                .logoutSuccessHandler(myLogoutSuccessHandler);

        // disable page caching
        http.headers().cacheControl();

    }

    //http.authorizeRequests().requestMatchers(CorsUtils::isCorsRequest).permitAll();
}
