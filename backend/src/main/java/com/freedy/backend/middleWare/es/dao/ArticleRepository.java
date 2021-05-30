package com.freedy.backend.middleWare.es.dao;

import com.freedy.backend.middleWare.es.model.ArticleEsModel;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Freedy
 * @date 2021/5/1 15:01
 */
@Repository
public interface ArticleRepository extends ElasticsearchRepository<ArticleEsModel,Long> {
    @Query("{\"match\": {\"title\": {\"query\": \"?0\"}}}")
    ArticleEsModel findByTitle(String title);
}
