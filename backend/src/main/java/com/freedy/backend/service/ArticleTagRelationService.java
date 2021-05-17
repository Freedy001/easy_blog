package com.freedy.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.freedy.backend.utils.PageUtils;
import com.freedy.backend.entity.ArticleTagRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 标签文章关联表
 *
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
public interface ArticleTagRelationService extends IService<ArticleTagRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根究文章id删除关联 数据
     */
    void removeRelationByArticleIds(List<Long> articleId);

}

