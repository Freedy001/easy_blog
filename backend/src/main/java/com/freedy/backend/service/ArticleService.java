package com.freedy.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.freedy.backend.entity.vo.dashboard.DashBoardVo;
import com.freedy.backend.utils.PageUtils;
import com.freedy.backend.entity.ArticleEntity;
import com.freedy.backend.entity.vo.article.ArticleDraftVo;
import com.freedy.backend.entity.vo.article.ArticleVo;
import com.freedy.backend.entity.vo.comment.CommentAdminVo;
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
    void deleteArticle(List<Long> singletonList) throws ExecutionException, InterruptedException;

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
    PageUtils getFrontArticleList(Map<String, Object> params) throws Exception;

    /**
     * 获取要保存到es里面的model
     */
    ArticleEsModel getEsArticle(Long id);

    /**
     * 获取es model 列表
     */
    List<ArticleEsModel> getEsArticleList(Integer page,Integer limit);

    /**
     * 获取文章title
     */
    List<CommentAdminVo.Article> getArticleTitles(List<Long> articleIDs);

    /**
     * 统计所有文章信息
     */
    void articleStatistics(DashBoardVo dashBoardVo) throws ExecutionException, InterruptedException;

    /**
     * 增加评论文章的数量
     */
    void addCommentNum(Long articleId);

    /**
     * 修改文章状态
     * @param id 文章id
     * @param articleStatus 修改后的状态
     */
    void updateArticleStatus(Long id, Integer articleStatus);

    /**
     * 给指定文章点赞
     * @param id 文章id
     */
    void likeArticle(Long id);

    /**
     * 获取关于页面
     */
    ArticleEntity getAbout();

    /**
     * 获取指定文章的作者邮箱
     */
    String getCreatorEmailByArticleId(Long articleId);


    /**
     * 增加文章的各个参数
     * @param field 要更新的字段
     * @param map 键为文章id 值为增加的数目
     */
    void addArticleParameter(String field, Map<String, String> map);

    /**
     * 更新文章评论数
     * @param map 键为文章id 值为数目
     */
    void updateComment(Map<String, String> map);

    /**
     * 创建第一篇文章与关于页面
     */
    void initArticle() throws Exception;

}

