package com.freedy.backend.dao;

import com.freedy.backend.common.utils.PageUtils;
import com.freedy.backend.entity.ArticleEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.freedy.backend.entity.vo.ArticleInfoVo;
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

    Long getTotalVisit(@Param("authorId") Integer authorId);

    Long getTotalComment(@Param("authorId") Integer authorId);
}
