package com.freedy.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

/**
 * @author Freedy
 * @date 2021/4/26 21:13
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket docket(Environment environment){
        //设置要显示的Swagger环境
        Profiles profiles = Profiles.of("dev");//可以填多个值 用逗号分割或者是|分割
        //通过environment.acceptsProfiles判断是否处在自己设定的环境当中
        boolean flag = environment.acceptsProfiles(profiles);
        return new Docket(DocumentationType.SWAGGER_2)
                //配置Swagger信息=apiInfo
                .apiInfo(new ApiInfo("ease blog SwaggerAPI文档",
                        "好好学习，天天向上",
                        "1.0",
                        "http://localhost:8081/hello",
                        new Contact("Freedy", "暂时还没有", "985948228@qq.com"),
                        "Apache 2.0",
                        "http://www.apache.org/licenses/LICENSE-2.0",
                        new ArrayList<>()))
                .enable(flag)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.freedy.backend.controller"))
                .build();
    }

}
