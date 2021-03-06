package com.freedy.backend;


import com.freedy.backend.entity.ArticleEntity;
import com.freedy.backend.service.ArticleService;
import com.freedy.backend.utils.EmailSender;
import com.freedy.backend.entity.vo.manager.NewUserVo;
import com.freedy.backend.properties.PermissionItemProperties;
import com.freedy.backend.utils.MarkDown;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;


@SpringBootTest
class BackendApplicationTests {
    @Autowired
    private PermissionItemProperties permissionItem;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private EmailSender sender;
    @Autowired
    private ArticleService service;
    @Autowired
    private RestHighLevelClient highLevelClient;

    @Test
    public void test() {
        NewUserVo manager = new NewUserVo();
        manager.setArticlePermission(Arrays.asList("可操作自己", "可见他人", "可操作他人"));
        manager.setCommentPermission(Arrays.asList("可操作自己", "可见他人", "可操作他人"));
        manager.setSettingPermission(Arrays.asList("常规设置", "评论设置", "SMTP设置", "附件设置"));


        for (Field field : manager.getClass().getDeclaredFields()) {
            if (field.getType().getSimpleName().equals("List")) {
                try {
                    //英文权限列表
                    List<String> engPermission = new ArrayList<>();
                    //中文权限列表
                    field.setAccessible(true);
                    List<String> cnPermission = (List<String>) field.get(manager);
                    Field translateFiled = permissionItem.getClass().getDeclaredField(field.getName());
                    translateFiled.setAccessible(true);
                    Map<String, String> translateMap = (Map<String, String>) translateFiled.get(permissionItem);
                    translateMap.forEach((k, v) -> {
                        for (String cName : cnPermission) {
                            if (v.equals(cName)) {
                                engPermission.add(k);
                                break;
                            }
                        }
                    });
                    field.set(manager, engPermission);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }


        System.out.println(manager);
    }

    @Test
    public void test1() {
        Set<String> keys = redisTemplate.keys("EASY*");
        redisTemplate.delete(keys);
    }

    @Test
    public void test2() {
        sender.sendHtml("985948228@qq.com", "test", "<h1>love you haha</h1>");
    }

    @Test
    public void test3() throws IOException {
        GetRequest getRequest = new GetRequest();
        getRequest.index("article");
        getRequest.id("1388865816372539418");
        getRequest.fetchSourceContext(new FetchSourceContext(true, new String[]{"likeNum"}, null));
        System.out.println(highLevelClient.get(getRequest, RequestOptions.DEFAULT).getSourceAsString());
    }

}
