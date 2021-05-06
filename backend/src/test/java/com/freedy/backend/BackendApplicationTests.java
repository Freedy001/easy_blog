package com.freedy.backend;


import com.freedy.backend.entity.vo.NewUserVo;
import com.freedy.backend.properties.PermissionItemProperties;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@SpringBootTest
class BackendApplicationTests {
    @Autowired
    private PermissionItemProperties permissionItem;
    @Test
    public void test(){
        NewUserVo manager = new NewUserVo();
        manager.setArticlePermission(Arrays.asList("可操作自己","可见他人","可操作他人"));
        manager.setCommentPermission(Arrays.asList("可操作自己","可见他人","可操作他人"));
        manager.setSettingPermission(Arrays.asList("常规设置","评论设置","SMTP设置","附件设置"));


        for (Field field : manager.getClass().getDeclaredFields()) {
            if(field.getType().getSimpleName().equals("List")){
                try {
                    //英文权限列表
                    List<String> engPermission = new ArrayList<>();
                    //中文权限列表
                    field.setAccessible(true);
                    List<String> cnPermission = (List<String>) field.get(manager);
                    Field translateFiled = permissionItem.getClass().getDeclaredField(field.getName());
                    translateFiled.setAccessible(true);
                    Map<String, String> translateMap = (Map<String, String>) translateFiled.get(permissionItem);
                    translateMap.forEach((k,v)->{
                        for (String cName : cnPermission) {
                            if (v.equals(cName)){
                                engPermission.add(k);
                                break;
                            }
                        }
                    });
                    field.set(manager,engPermission);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }


        System.out.println(manager);
    }
}
