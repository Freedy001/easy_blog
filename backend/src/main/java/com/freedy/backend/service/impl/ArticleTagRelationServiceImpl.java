package com.freedy.backend.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedy.backend.utils.PageUtils;
import com.freedy.backend.utils.Query;

import com.freedy.backend.dao.ArticleTagRelationDao;
import com.freedy.backend.entity.ArticleTagRelationEntity;
import com.freedy.backend.service.ArticleTagRelationService;


@Service("articleTagRelationService")
public class ArticleTagRelationServiceImpl extends ServiceImpl<ArticleTagRelationDao, ArticleTagRelationEntity> implements ArticleTagRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ArticleTagRelationEntity> page = this.page(
                new Query<ArticleTagRelationEntity>().getPage(params),
                new QueryWrapper<ArticleTagRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void removeRelationByArticleIds(List<Long> articleId) {
        baseMapper.removeRelationByArticleIds(articleId);
    }

}