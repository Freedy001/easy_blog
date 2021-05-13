package com.freedy.backend.service.impl;

import com.alibaba.fastjson.JSON;
import com.freedy.backend.entity.vo.SuccessionVo;
import com.freedy.backend.middleWare.es.model.ArticleEsModel;
import com.freedy.backend.service.SearchService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.slice.SliceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.freedy.backend.constant.EsConstant.*;

/**
 * @author Freedy
 * @date 2021/5/13 19:31
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private RestHighLevelClient highLevelClient;


    @Override
    public List<SuccessionVo> getSuggestions(String queryString) throws IOException {
        SearchRequest articleRequest = new SearchRequest("article");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.should(QueryBuilders.matchQuery(TITLE, queryString));
        boolQuery.should(QueryBuilders.matchQuery(DESC, queryString));
        boolQuery.should(QueryBuilders.termQuery(CATEGORY, queryString));
        boolQuery.should(QueryBuilders.termQuery(TAG, queryString));
        boolQuery.should(QueryBuilders.matchQuery(CONTENT, queryString));
        builder.query(boolQuery);
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field(TITLE).field(DESC).field(CATEGORY)
                .field(TAG).field(CONTENT).preTags(PRE_TAG).postTags(POST_TAG);
        builder.highlighter(highlightBuilder);
        builder.size(10);
        articleRequest.source(builder);
        SearchResponse response = highLevelClient.search(articleRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        ArrayList<SuccessionVo> vos = new ArrayList<SuccessionVo>();
        for (SearchHit hit : hits) {
            SuccessionVo vo = new SuccessionVo();
            Map<String, HighlightField> fields = hit.getHighlightFields();
            for (Map.Entry<String, HighlightField> entry : fields.entrySet()) {
                vo.setField(entry.getKey());
                vo.setContent("<p>"+entry.getValue().getFragments()[0].toString()+"</p>");
                //因为搜索建议,只需要一个字段
                break;
            }
            ArticleEsModel esModel = JSON.parseObject(hit.getSourceAsString(), ArticleEsModel.class);
            vo.setTitle(esModel.getTitle());
            vos.add(vo);
        }
        return vos;
    }

    /**
     * GET /article/_search
     * {
     *    "query": {
     *      "bool": {
     *        "should": [
     *          {
     *            "match": {
     *              "title": ""
     *            }
     *          },
     *           {
     *            "term": {
     *              "articleCategory": {
     *                "value": ""
     *              }
     *            }
     *          },
     *           {
     *            "term": {
     *              "articleTags": {
     *                "value": ""
     *              }
     *            }
     *          },
     *          {
     *            "match": {
     *              "articleDesc": ""
     *            }
     *          }
     *        ]
     *      }
     *    },
     *   "_source": ["title","articleCategory","articleTags","articleDesc"],
     *   "highlight": {
     *     "fields": {
     *       "title": {
     *         "pre_tags": "<mark>",
     *         "post_tags": "</mark>"
     *       },
     *       "articleCategory": {
     *         "pre_tags": "<mark>",
     *         "post_tags": "</mark>"
     *       },
     *       "articleTags": {
     *         "pre_tags": "<mark>",
     *         "post_tags": "</mark>"
     *       },
     *       "articleDesc": {
     *         "pre_tags": "<mark>",
     *         "post_tags": "</mark>"
     *       }
     *     }
     *   }
     * }
     */
}
