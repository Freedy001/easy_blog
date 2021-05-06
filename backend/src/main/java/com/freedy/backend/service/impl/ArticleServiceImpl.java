package com.freedy.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.freedy.backend.common.utils.*;
import com.freedy.backend.constant.EntityConstant;
import com.freedy.backend.constant.RabbitConstant;
import com.freedy.backend.entity.ArticleTagRelationEntity;
import com.freedy.backend.entity.ManagerEntity;
import com.freedy.backend.entity.TagEntity;
import com.freedy.backend.entity.dto.EsTypeDto;
import com.freedy.backend.entity.vo.ArticleDraftVo;
import com.freedy.backend.entity.vo.ArticleVo;
import com.freedy.backend.entity.vo.ArticleInfoVo;
import com.freedy.backend.exception.NoPermissionsException;
import com.freedy.backend.service.ArticleTagRelationService;
import com.freedy.backend.service.TagService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.freedy.backend.dao.ArticleDao;
import com.freedy.backend.entity.ArticleEntity;
import com.freedy.backend.service.ArticleService;
import org.springframework.transaction.annotation.Transactional;


@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleDao, ArticleEntity> implements ArticleService {
    @Autowired
    private ThreadPoolExecutor executor;
    @Autowired
    private TagService tagService;
    @Autowired
    private ArticleTagRelationService relationService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) throws ExecutionException, InterruptedException {
        PageUtils page = new PageUtils(params);
        String permission = Local.PERMISSION_LOCAL.get();
        ManagerEntity entity = Local.MANAGER_LOCAL.get();
        CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> {
            List<ArticleInfoVo> articleList;
            if (AuthorityUtils.hasAuthority("article-operation-to-others",permission)){
                articleList = baseMapper.queryArticleList(page);
            }else {
                articleList = baseMapper.queryArticleListByAuthorId(page,entity.getId());
            }
            articleList.forEach(item -> {
                //设置日期
                Date date = new Date();
                if (!item.getPublishTime().equals("0")){
                    date.setTime(Long.parseLong(item.getPublishTime()));
                    item.setPublishTime(DateUtils.format(date));
                }
                date.setTime(Long.parseLong(item.getUpdateTime()));
                item.setUpdateTime(DateUtils.format(date));
            });
            page.setList(articleList);
        }, executor);
        Long count = baseMapper.getCount();
        page.setTotalCount(Math.toIntExact(count));
        f1.get();
        return page;
    }



    @Override
    @Transactional
    public void saveArticle(ArticleVo article) throws ExecutionException, InterruptedException {
        if (article.getId()!=null&&
                !article.getAuthorId().equals(Local.MANAGER_LOCAL.get().getId())&&
                !AuthorityUtils.hasAuthority("article-operation-to-others")
        ) throw new NoPermissionsException("没有权限修改");

        log.debug(article.toString());
        Integer authorId = Local.MANAGER_LOCAL.get().getId();
        ArticleEntity entity = new ArticleEntity();
        BeanUtils.copyProperties(article, entity);
        //获取作者消息
        if (article.getId() == null) {
            entity.setAuthorId(authorId);
        }
        entity.setArticleStatus(article.getIsOverhead() ?
                EntityConstant.ARTICLE_Overhead : EntityConstant.ARTICLE_PUBLISHED);
        entity.setArticleComment(article.getIsComment() ?
                EntityConstant.ARTICLE_CAN_COMMENT : EntityConstant.ARTICLE_CAN_NOT_COMMENT);
        if (article.getId() == null) {
            entity.setCreateTime(new Date().getTime());
        }
        entity.setUpdateTime(new Date().getTime());
        //保存文章
        CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> {
            if (article.getId() == null) {
                baseMapper.insert(entity);
                article.setId(entity.getId());
            } else {
                baseMapper.updateById(entity);
            }
            //将文章保存到es
            EsTypeDto dto = new EsTypeDto();
            dto.setType(article.getId() == null ? EsTypeDto.EsType.SAVE : EsTypeDto.EsType.UPDATE);
            dto.setId(entity.getId());
            dto.setPublishTime(entity.getPublishTime());
            rabbitTemplate.convertAndSend(RabbitConstant.ES_EXCHANGE_NAME,
                    RabbitConstant.ES_ROUTE_KEY + ".saveOrUpdate",
                    dto);
        }, executor);
        //创建用户新建的标签
        List<TagEntity> newTags = new ArrayList<>();
        CompletableFuture<Void> f2 = CompletableFuture.runAsync(() -> {
            List<String> notExistedTag = article.getNotExistedTag();
            if (notExistedTag.size() > 0) {
                notExistedTag.forEach(item -> {
                    TagEntity tagEntity = new TagEntity();
                    tagEntity.setTagName(item);
                    tagEntity.setPriority(EntityConstant.PRIORITY_NORMAL);
                    tagEntity.setCreatorId(authorId);
                    newTags.add(tagEntity);
                });
                tagService.createTagsByName(newTags);
            }
        }, executor);
        //创建标签与文章的关系
        CompletableFuture<Void> f3 = CompletableFuture.allOf(f1, f2).thenRun(() -> {
            List<ArticleTagRelationEntity> relationEntityList = new ArrayList<>();
            newTags.forEach(item -> {
                ArticleTagRelationEntity relationEntity = new ArticleTagRelationEntity();
                relationEntity.setTagId(item.getId());
                relationEntity.setArticleId(entity.getId());
                relationEntityList.add(relationEntity);
            });
            article.getExistedTags().forEach(item -> {
                ArticleTagRelationEntity relationEntity = new ArticleTagRelationEntity();
                relationEntity.setTagId(item);
                relationEntity.setArticleId(entity.getId());
                relationEntityList.add(relationEntity);
            });
            relationService.remove(new QueryWrapper<ArticleTagRelationEntity>()
                            .eq("article_id",entity.getId()));
            relationService.saveBatch(relationEntityList);
        });
        f3.get();
    }

    @Override
    public ArticleVo getArticle(Long id) {
        ArticleEntity entity = baseMapper.selectById(id);
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(entity, articleVo);
        //设置发布状态
        articleVo.setIsOverhead(entity.getArticleStatus().equals(EntityConstant.ARTICLE_Overhead));
        articleVo.setIsComment(entity.getArticleComment().equals(EntityConstant.ARTICLE_CAN_COMMENT));
        List<ArticleTagRelationEntity> relationList = relationService.list(new QueryWrapper<ArticleTagRelationEntity>()
                .eq("article_id", entity.getId()));
        List<Integer> list = relationList.stream().map(ArticleTagRelationEntity::getTagId).collect(Collectors.toList());
        articleVo.setExistedTags(list);
        return articleVo;
    }

    @Override
    public void saveDraft(ArticleDraftVo draftVo) {
        ArticleEntity entity = new ArticleEntity();
        BeanUtils.copyProperties(draftVo,entity);
        if(entity.getId()!=null){
            //有id表示文章已经存在 这时要将文章状态转换为草稿状态 即未发布状态
            entity.setArticleStatus(EntityConstant.ARTICLE_UNPUBLISHED);
            entity.setUpdateTime(new Date().getTime());
            baseMapper.updateById(entity);
        }else {
            //文章不存在创建文章
            entity.setArticleStatus(EntityConstant.ARTICLE_UNPUBLISHED);
            entity.setCreateTime(new Date().getTime());
            entity.setUpdateTime(new Date().getTime());
            //获取作者名称
            entity.setAuthorId(Local.MANAGER_LOCAL.get().getId());
            baseMapper.insert(entity);
        }
    }

    @Override
    public void deleteArticle(List<Long> articleId) {
        relationService.removeRelationByArticleIds(articleId);
        baseMapper.deleteBatchIds(articleId);
    }

    @Override
    public Long getTotalVisit() {
        return baseMapper.getTotalVisit(Local.MANAGER_LOCAL.get().getId());
    }

    @Override
    public Long getTotalComment() {
        return baseMapper.getTotalComment(Local.MANAGER_LOCAL.get().getId());
    }

}