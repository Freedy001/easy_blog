package com.freedy.backend.service;

import com.freedy.backend.entity.vo.search.SearchResult;
import com.freedy.backend.entity.vo.search.SuggestionVo;

import java.io.IOException;
import java.util.List;

/**
 * @author Freedy
 * @date 2021/5/13 19:31
 */
public interface SearchService {
    /**
     * 获取搜索建议
     * @param queryString 搜索字符串
     */
    List<SuggestionVo> getSuggestions(String queryString) throws IOException;

    /**
     * 搜索文章
     * @param searchString 搜索字符串
     */
    List<SearchResult> doSearch(String searchString,Integer page) throws IOException;
}
