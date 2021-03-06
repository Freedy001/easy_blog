package com.freedy.backend.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;


/**
 * 自动创建数据库
 * @author Freedy
 * @date 2021/5/29 15:40
 */
@Component
@Slf4j
public class RunSqlScript{
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @PostConstruct
    public void checkDb(){
        try {
            // jdbc 连接信息: 注: 现在版本的JDBC不需要配置driver，因为不需要Class.forName手动加载驱动
            // 建立连接
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement blog_article = conn.prepareStatement("select count(*) from information_schema.TABLES where table_name = 'blog_article'");
            ResultSet resultSet = blog_article.executeQuery();
            resultSet.next();
            if (resultSet.getInt(1)==0){
                log.info("没有发现数据库，开始创建......");
                // 创建ScriptRunner，用于执行SQL脚本
                ScriptRunner runner = new ScriptRunner(conn);
                runner.setErrorLogWriter(null);
                runner.setLogWriter(null);
                // 执行SQL脚本
                Class<? extends RunSqlScript> currentClazz = this.getClass();
                InputStream fis = currentClazz.getClassLoader().getResourceAsStream("init.sql");
                if (fis==null){
                    throw new FileNotFoundException("没有找到初始化sql文件");
                }
                BufferedReader in = new BufferedReader(new InputStreamReader(fis));
                runner.runScript(in);
                // 关闭连接
                conn.close();
                // 若成功，打印提示信息
                log.info("创建成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}