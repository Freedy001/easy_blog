package com.freedy.backend;

import com.freedy.backend.middleWare.es.dao.ArticleRepository;
import com.freedy.backend.service.ArticleService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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
