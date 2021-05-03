package com.freedy.backend;

import com.freedy.backend.middleWare.es.model.ArticleEsModel;
import com.freedy.backend.middleWare.es.dao.ArticleRepository;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Freedy
 * @date 2021/5/1 15:04
 */
@SpringBootTest
public class ESTest {
    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ArticleRepository repository;

    ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void create(){
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

    @Test
    public void query() throws IOException {
    }


}
