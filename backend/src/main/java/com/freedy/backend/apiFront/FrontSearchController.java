package com.freedy.backend.apiFront;

import com.freedy.backend.utils.Result;
import com.freedy.backend.entity.vo.search.SearchResult;
import com.freedy.backend.entity.vo.search.SuggestionVo;
import com.freedy.backend.service.SearchService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author Freedy
 * @date 2021/5/13 19:25
 */
@Slf4j
@RestController
@RequestMapping("/frontend/search")
public class FrontSearchController {

    @Autowired
    private SearchService service;

    @ApiOperation("获取搜索建议")
    @GetMapping("/getSuggestions")
    public Result getSuggestions(@RequestParam String queryString) throws IOException {
        List<SuggestionVo> suggestions = service.getSuggestions(queryString);
        return Result.ok().setData(suggestions);
    }

    @ApiOperation("搜索")
    @GetMapping("/doSearch")
    public Result search(@RequestParam(name = "searchString") String searchString ,
            @RequestParam(name = "page") Integer page) throws IOException {
        List<SearchResult> results=service.doSearch(searchString,page);
        return Result.ok().setData(results);
    }


}
