package com.freedy.backend;

import com.alibaba.fastjson.JSON;
import com.freedy.backend.dao.ArticleDao;
import com.freedy.backend.entity.vo.ArticleVo;
import com.freedy.backend.middleWare.es.model.ArticleEsModel;
import com.freedy.backend.middleWare.es.dao.ArticleRepository;
import com.freedy.backend.service.ArticleService;
import com.google.common.cache.Cache;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Freedy
 * @date 2021/5/1 15:04
 */
@SpringBootTest
@RestController
public class ESTest {
    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ArticleRepository repository;

    @Autowired
    private ArticleService service;

    @Autowired
    private SqlSessionFactory factory;

    @Test
    public void create() {
        System.out.println("创建成功");
    }

    @Test
    public void createIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("article");
        client.indices().create(request, RequestOptions.DEFAULT);
    }

    @Test
    public void addDocument() throws IOException {
//        ArrayList<String> list = new ArrayList<>();
//        list.add("juc");
//        list.add("并发");
//        list.add("jvm");
//        list.add("jmm");
//        ArticleEsModel model = new ArticleEsModel(
//                1L,"jvm","深入理解jvm",
//                "freedy","null",
//                "深入理解jvm深入理解jvm深入理解jvm深入理解jvm"
//                ,100000L,100000L,100000L,
//                "java",list,1231312312L
//        );
//        repository.save(model);
    }


}
