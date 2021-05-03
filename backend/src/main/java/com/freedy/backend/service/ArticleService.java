package com.freedy.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.freedy.backend.common.utils.PageUtils;
import com.freedy.backend.entity.ArticleEntity;
import com.freedy.backend.entity.vo.ArticleDraftVo;
import com.freedy.backend.entity.vo.ArticleVo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 文章表
 *
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
public interface ArticleService extends IService<ArticleEntity> {

    /**
     * 查询文章详细列表
     */
    PageUtils queryPage(Map<String, Object> params) throws ExecutionException, InterruptedException;

    /**
     * 发布文章 或修改文章
     */
    void saveArticle(ArticleVo article) throws ExecutionException, InterruptedException;


    /**
     * 查询发布的文章详情
     */
    ArticleVo getArticle(Long id);

    /**
     * 保存文章为草稿
     */
    void saveDraft(ArticleDraftVo draftVo);

    /**
     * 根绝id批量删除
     */
    void deleteArticle(List<Long> singletonList);
}

