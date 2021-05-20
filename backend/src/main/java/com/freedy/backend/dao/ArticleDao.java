package com.freedy.backend.dao;

import com.freedy.backend.utils.PageUtils;
import com.freedy.backend.entity.ArticleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.freedy.backend.entity.vo.article.ArticleInfoVo;
import com.freedy.backend.entity.vo.comment.CommentAdminVo;
import com.freedy.backend.middleWare.es.model.ArticleEsModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章表
 * 
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
@Mapper
public interface ArticleDao extends BaseMapper<ArticleEntity> {

    /**
     * 文章列表
     */
    List<ArticleInfoVo> queryArticleList(PageUtils page);

    /**
     * 获取数据库条目
     */
    Long getCount();

    /**
     * 获取指定用户的访问量
     */
    Long getTotalVisit(@Param("authorId") Integer authorId);

    /**
     * 获取总共的访问量
     */
    Long getTotalVisitNum();

    /**
     * 获取指定用户的评论量
     */
    Long getTotalComment(@Param("authorId") Integer authorId);

    /**
     * 获取ArticleEsModel
     */
    ArticleEsModel getEsModel(Long id);

    /**
     * 获取ArticleEsModel 列表
     */
    List<ArticleEsModel> getEsArticleList(@Param("page") Integer page, @Param("limit") Integer limit);

    /**
     * 获取文章标题
     */
    List<CommentAdminVo.Article> getTitles(List<Long> articleIds);


    /**
     * 获取所有文章的标题与分类id
     */
    List<ArticleEntity> getAllArticleTitleAndCategoryId();

    /**
     * 增加评论数量
     */
    void addCommentNum(@Param("articleId") Long articleId);

    /**
     * 修改状态
     */
    void updateArticleStatus(@Param("id") Long id, @Param("articleStatus") Integer articleStatus);

    /**
     * 给指定文章点赞
     * @param id 文章id
     */
    void likeArticle(Long id);
}
