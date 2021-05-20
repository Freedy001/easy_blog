package com.freedy.backend.service.impl;

import com.alibaba.fastjson.JSON;
import com.freedy.backend.utils.DateUtils;
import com.freedy.backend.entity.vo.search.SearchResult;
import com.freedy.backend.entity.vo.search.SuggestionVo;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

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
    public List<SuggestionVo> getSuggestions(String queryString) throws IOException {
        SearchRequest articleRequest = new SearchRequest("article");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.should(QueryBuilders.matchQuery(TITLE, queryString).boost(2));
        boolQuery.should(QueryBuilders.termQuery(CATEGORY, queryString).boost(1));
        boolQuery.should(QueryBuilders.termQuery(TAG, queryString).boost(1));
        builder.query(boolQuery);
        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field(TITLE).field(CATEGORY)
                .field(TAG).preTags(PRE_TAG).postTags(POST_TAG);
        builder.highlighter(highlightBuilder);
        builder.size(10);
        //构建需要返回的字段
        String[] queryFields = Arrays.stream(SuggestionVo.class.getDeclaredFields()).map(Field::getName).toArray(String[]::new);
        builder.fetchSource(queryFields,null);
        articleRequest.source(builder);
        SearchResponse response = highLevelClient.search(articleRequest, RequestOptions.DEFAULT);
        ArrayList<SuggestionVo> vos = new ArrayList<SuggestionVo>();
        for (SearchHit hit : response.getHits().getHits()) {
            SuggestionVo vo = new SuggestionVo();
            Map<String, HighlightField> fields = hit.getHighlightFields();
            //越往上优先级越大
            if (fields.containsKey(TITLE)) {
                vo.setField(TITLE);
                vo.setContent("<p>" + fields.get(TITLE).getFragments()[0].toString() + "</p>");
            } else if (fields.containsKey(CATEGORY)) {
                vo.setField(CATEGORY);
                vo.setContent("<p>" + fields.get(CATEGORY).getFragments()[0].toString() + "</p>");
            } else if (fields.containsKey(TAG)) {
                vo.setField(TAG);
                vo.setContent("<p>" + fields.get(TAG).getFragments()[0].toString() + "</p>");
            }
            ArticleEsModel esModel = JSON.parseObject(hit.getSourceAsString(), ArticleEsModel.class);
            vo.setTitle(esModel.getTitle());
            vo.setId(esModel.getId().toString());
            vos.add(vo);
        }
        return vos;
    }

    @Override
    public List<SearchResult> doSearch(String searchString, Integer page) throws IOException {
        SearchRequest articleRequest = new SearchRequest("article");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.should(QueryBuilders.matchQuery(TITLE, searchString).boost(1000));
        boolQuery.should(QueryBuilders.termQuery(CATEGORY, searchString).boost(1000));
        boolQuery.should(QueryBuilders.termQuery(TAG, searchString).boost(1000));
        boolQuery.should(QueryBuilders.matchQuery(DESC, searchString).boost(100));
        boolQuery.should(QueryBuilders.matchQuery(CONTENT, searchString).boost(1));
        builder.query(boolQuery);
        //构建高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field(TITLE).field(DESC).field(CATEGORY)
                .field(TAG).field(CONTENT).preTags(PRE_TAG).postTags(POST_TAG);
        builder.highlighter(highlightBuilder);
        //分页
        builder.size(10);
        builder.from((page - 1) * 10);
        //查询指定字段
        String[] queryFields = Arrays.stream(SearchResult.class.getDeclaredFields()).map(Field::getName).filter(item -> !"hitItem".equals(item)).toArray(String[]::new);
        builder.fetchSource(queryFields, null);
        articleRequest.source(builder);
        SearchResponse response = highLevelClient.search(articleRequest, RequestOptions.DEFAULT);
        List<SearchResult> searchResults = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            //构建SearchResult
            SearchResult vo = new SearchResult();
            ArticleEsModel esModel = JSON.parseObject(hit.getSourceAsString(), ArticleEsModel.class);
            BeanUtils.copyProperties(esModel, vo);
            vo.setId(esModel.getId().toString());
            vo.setPublishTime(DateUtils.formatTime(new Date(esModel.getPublishTime())));
            //构建hitItem
            List<SearchResult.HitItem> hitItems = new ArrayList<>();
            hit.getHighlightFields().forEach((k, v) -> {
                SearchResult.HitItem item = new SearchResult.HitItem();
                String hitContent = v.getFragments()[0].toString();
                switch (k) {
                    case TITLE:
                        //这时不需要将title字段放入命中字段中 直接与标题合并就行了
                        vo.setTitle(hitContent);
                        break;
                    case CATEGORY:
                        vo.setArticleCategory(hitContent);
                        break;
                    case TAG:
                        //将命中的tag替换为带标签的tag
                        String hitTag = hitContent.replace("<mark>", "").replace("</mark>", "");
                        vo.getArticleTags().replaceAll(tag -> tag.equals(hitTag) ? hitContent : tag);
                        break;
                    default:
                        item.setField(k);
                        item.setContent(hitContent);
                        hitItems.add(item);
                        break;
                }
            });
            vo.setHitItem(hitItems);
            searchResults.add(vo);
        }
        return searchResults;
    }

    /*
    DSL语句
     GET /article/_search
      {
         "query": {
           "bool": {
             "should": [
               {
                 "match": {
                   "title": ""
                 }
               },
                {
                 "term": {
                   "articleCategory": {
                     "value": ""
                   }
                 }
               },
                {
                 "term": {
                   "articleTags": {
                     "value": ""
                   }
                 }
               },
               {
                 "match": {
                   "articleDesc": ""
                 }
               }
             ]
           }
         },
        "_source": ["title","articleCategory","articleTags","articleDesc"],
        "highlight": {
          "fields": {
            "title": {
              "pre_tags": "<mark>",
              "post_tags": "</mark>"
            },
            "articleCategory": {
              "pre_tags": "<mark>",
              "post_tags": "</mark>"
            },
            "articleTags": {
              "pre_tags": "<mark>",
              "post_tags": "</mark>"
            },
            "articleDesc": {
              "pre_tags": "<mark>",
              "post_tags": "</mark>"
            }
          }
        }
      }
     */
}
