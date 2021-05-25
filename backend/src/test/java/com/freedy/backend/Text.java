package com.freedy.backend;

import com.freedy.backend.entity.vo.manager.NewUserVo;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * @author Freedy
 * @date 2021/4/29 13:11
 */
public class Text {
    //模拟100万条评论大小
    public static void main(String[] args) throws Exception {
        NewUserVo userVo = new NewUserVo();
        userVo.setPassword("11111");
        userVo.setEmail("11111");
        userVo.setPassword("123sda123sda123sda123sda123sda123sda123sda123sda123sda123sda123sda123sda123sda123sda123sda123sda123sda123sda123sda123sda123sda123sda123sda123sda");
        userVo.setPassword("");
        System.out.println(userVo);
//        for (PropertyDescriptor descriptor : BeanUtils.getPropertyDescriptors(userVo.getClass())) {
//            System.out.println(descriptor.getPropertyType().getSimpleName());
////            System.out.println(descriptor.getReadMethod().invoke(userVo));
//        }
    }
}

