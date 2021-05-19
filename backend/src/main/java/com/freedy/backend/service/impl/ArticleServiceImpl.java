package com.freedy.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.freedy.backend.entity.*;
import com.freedy.backend.entity.vo.dashboard.DashBoardVo;
import com.freedy.backend.service.*;
import com.freedy.backend.utils.AuthorityUtils;
import com.freedy.backend.utils.DateUtils;
import com.freedy.backend.utils.Local;
import com.freedy.backend.utils.PageUtils;
import com.freedy.backend.constant.EntityConstant;
import com.freedy.backend.constant.RabbitConstant;
import com.freedy.backend.entity.dto.EsTypeDto;
import com.freedy.backend.entity.vo.article.ArticleDraftVo;
import com.freedy.backend.entity.vo.article.ArticleVo;
import com.freedy.backend.entity.vo.article.ArticleInfoVo;
import com.freedy.backend.entity.vo.comment.CommentAdminVo;
import com.freedy.backend.enumerate.EsType;
import com.freedy.backend.exception.NoPermissionsException;
import com.freedy.backend.middleWare.es.dao.ArticleRepository;
import com.freedy.backend.middleWare.es.model.ArticleEsModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.freedy.backend.dao.ArticleDao;
import org.springframework.transaction.annotation.Transactional;


@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleDao, ArticleEntity> implements ArticleService {
    private final ThreadPoolExecutor executor;
    private final TagService tagService;
    private final ArticleTagRelationService relationService;
    private final RabbitTemplate rabbitTemplate;
    private final CategoryService categoryService;
    private final VisitorService visitorService;

    public ArticleServiceImpl(RabbitTemplate rabbitTemplate, ArticleTagRelationService relationService, TagService tagService, CategoryService categoryService, ThreadPoolExecutor executor, VisitorService visitorService) {
        this.rabbitTemplate = rabbitTemplate;
        this.relationService = relationService;
        this.tagService = tagService;
        this.executor = executor;
        this.categoryService = categoryService;
        this.visitorService = visitorService;
    }

    private PageUtils queryPage(Map<String, Object> params) throws ExecutionException, InterruptedException {
        PageUtils page = new PageUtils(params);
        CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> {
            //分页查询
            List<ArticleInfoVo> articleList = baseMapper.queryArticleList(page);
            articleList.forEach(item -> {
                //设置日期
                Date date = new Date();
                if (!item.getPublishTime().equals("0")) {
                    date.setTime(Long.parseLong(item.getPublishTime()));
                    item.setPublishTime(DateUtils.formatTime(date));
                }
                date.setTime(Long.parseLong(item.getUpdateTime()));
                item.setUpdateTime(DateUtils.formatTime(date));
            });
            page.setList(articleList);
        }, executor);
        Long count = baseMapper.getCount();
        page.setTotalCount(Math.toIntExact(count));
        f1.get();
        return page;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PageUtils getBackArticleList(Map<String, Object> params) throws ExecutionException, InterruptedException {
        //没有修改他人的选项 就不要让他看到他人的文章
        PageUtils page = queryPage(params);
        if (!AuthorityUtils.hasAuthority("article-operation-to-others")) {
            ManagerEntity entity = Local.MANAGER_LOCAL.get();
            List<ArticleInfoVo> list = (List<ArticleInfoVo>) page.getList();
            List<ArticleInfoVo> infoVos = list.stream().filter(item -> item.getAuthorName().equals(entity.getNickname())).collect(Collectors.toList());
            page.setList(infoVos);
        }
        return page;
    }


    @Override
    @SuppressWarnings("unchecked")
    public PageUtils getFrontArticleList(Map<String, Object> params) throws ExecutionException, InterruptedException {
        PageUtils page = queryPage(params);
        List<ArticleInfoVo> list = (List<ArticleInfoVo>) page.getList();
        List<ArticleInfoVo> infoVos = list.stream().filter(item -> item.getArticleStatus() >= 3 &&
                DateUtils.formatTime(item.getPublishTime()).getTime() < new Date().getTime())
                .collect(Collectors.toList());
        page.setList(infoVos);
        return page;
    }

    @Override
    @Transactional
    public void saveArticle(ArticleVo article) throws ExecutionException, InterruptedException {
        if (article.getId() != null &&
                !article.getAuthorId().equals(Local.MANAGER_LOCAL.get().getId()) &&
                !AuthorityUtils.hasAuthority("article-operation-to-others")
        ) throw new NoPermissionsException();

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
            } else {
                baseMapper.updateById(entity);
            }
            //将文章保存到es
            EsTypeDto dto = new EsTypeDto();
            dto.setId(entity.getId());
            dto.setType(article.getId() == null ? EsType.SAVE : EsType.UPDATE);
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
                    .eq("article_id", entity.getId()));
            if (relationEntityList.size() > 0)
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
        BeanUtils.copyProperties(draftVo, entity);
        if (entity.getId() != null) {
            //有id表示文章已经存在 这时要将文章状态转换为草稿状态 即未发布状态
            entity.setArticleStatus(EntityConstant.ARTICLE_UNPUBLISHED);
            entity.setUpdateTime(new Date().getTime());
            baseMapper.updateById(entity);
        } else {
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
    @Transactional
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

    @Override
    public ArticleEsModel getEsArticle(Long id) {
        return baseMapper.getEsModel(id);
    }

    @Override
    public List<ArticleEsModel> getEsArticleList(Integer page, Integer limit) {
        return baseMapper.getEsArticleList(page, limit);
    }

    @Override
    public List<CommentAdminVo.Article> getArticleTitles(List<Long> articleIds) {
        return baseMapper.getTitles(articleIds);
    }

    @Override
    public void articleStatistics(DashBoardVo dashBoardVo) throws ExecutionException, InterruptedException {
        CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> {
            int count = visitorService.count();
            dashBoardVo.setVisitNum(String.valueOf(count));
        });
        //所有分类
        CompletableFuture<List<CategoryEntity>> f3 = CompletableFuture.supplyAsync(categoryService::list);
        //所有文章
        List<ArticleEntity> articleList = new LinkedList<>(baseMapper.getAllArticleTitleAndCategoryId());
        //设置文章欢迎度
        Object[][] articlePopular = new Object[articleList.size()+1][4];
        for (int i = 0; i < articleList.size(); i++) {
            ArticleEntity entity = articleList.get(i);
            articlePopular[i+1][0] = entity.getTitle();
            articlePopular[i+1][1] = entity.getLikeNum();
            articlePopular[i+1][2] = entity.getCommentNum();
            articlePopular[i+1][3] = entity.getVisitNum();
        }
        dashBoardVo.setArticlePopular(articlePopular);
        dashBoardVo.setArticleNum(articleList.size());

        List<CategoryEntity> categoryList = f3.get();
        ArrayList<DashBoardVo.ArticleDistribution> articleDistributions = new ArrayList<>();
        for (CategoryEntity categoryEntity : categoryList) {
            DashBoardVo.ArticleDistribution distribution = new DashBoardVo.ArticleDistribution();
            //设置分类名称
            distribution.setName(categoryEntity.getCategoryName());
            List<DashBoardVo.ArticleDistribution.Children> childrenList = new ArrayList<>();
            //构建分类下的文章
            for (int i = 0; i < articleList.size(); i++) {
                ArticleEntity articleEntity = articleList.get(i);
                //找到某个分类下的所有文章并将其构建成articleDistributions
                if (categoryEntity.getId().equals(articleEntity.getArticleCategoryId())) {
                    //找到文章
                    DashBoardVo.ArticleDistribution.Children children = new DashBoardVo.ArticleDistribution.Children();
                    children.setName(articleEntity.getTitle());
                    //默认是以点赞数为文章的权重 最少是一
                    children.setValue(articleEntity.getLikeNum()==0?1:Math.toIntExact(articleEntity.getLikeNum()));
                    childrenList.add(children);
                    articleList.remove(articleEntity);
                }
            }
            distribution.setChildren(childrenList);
            articleDistributions.add(distribution);
        }
        dashBoardVo.setArticleDistribution(articleDistributions);
        f1.get();
    }

    @Override
    public void addCommentNum(Long articleId) {
        baseMapper.addCommentNum(articleId);
    }


}