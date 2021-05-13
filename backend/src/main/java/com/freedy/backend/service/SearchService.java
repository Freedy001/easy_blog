package com.freedy.backend.service;

import com.freedy.backend.entity.vo.SuccessionVo;

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
     * @return
     */
    List<SuccessionVo> getSuggestions(String queryString) throws IOException;
}
