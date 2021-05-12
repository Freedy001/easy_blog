package com.freedy.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.freedy.backend.common.utils.PageUtils;
import com.freedy.backend.entity.ArticleEntity;
import com.freedy.backend.entity.vo.ArticleDraftVo;
import com.freedy.backend.entity.vo.ArticleVo;
import com.freedy.backend.middleWare.es.model.ArticleEsModel;

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
    PageUtils getBackArticleList(Map<String, Object> params) throws ExecutionException, InterruptedException;

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

    /**
     * 获取该用户的总访问量
     */
    Long getTotalVisit();

    /**
     * 获取该用户的文章的总评论数
     */
    Long getTotalComment();

    /**
     * 获取前台文章列表
     */
    PageUtils getFrontArticleList(Map<String, Object> params) throws ExecutionException, InterruptedException;

    /**
     * 获取要保存到es里面的model
     */
    ArticleEsModel getEsArticle(Long id);

    /**
     * 获取es model 列表
     */
    List<ArticleEsModel> getEsArticleList(Integer page,Integer limit);
}

