package com.freedy.backend.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.freedy.backend.constant.CacheConstant;
import com.freedy.backend.constant.RedisConstant;
import com.freedy.backend.entity.*;
import com.freedy.backend.entity.vo.dashboard.DashBoardVo;
import com.freedy.backend.exception.ArgumentErrorException;
import com.freedy.backend.service.*;
import com.freedy.backend.utils.*;
import com.freedy.backend.constant.EntityConstant;
import com.freedy.backend.constant.RabbitConstant;
import com.freedy.backend.entity.dto.EsTypeDto;
import com.freedy.backend.entity.vo.article.ArticleDraftVo;
import com.freedy.backend.entity.vo.article.ArticleVo;
import com.freedy.backend.entity.vo.article.ArticleInfoVo;
import com.freedy.backend.entity.vo.comment.CommentAdminVo;
import com.freedy.backend.enumerate.EsType;
import com.freedy.backend.exception.NoPermissionsException;
import com.freedy.backend.middleWare.es.model.ArticleEsModel;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.freedy.backend.dao.ArticleDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import static com.freedy.backend.constant.EsConstant.INDEX;


/**
 * @author Freedy
 */
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleDao, ArticleEntity> implements ArticleService {
    private final ThreadPoolExecutor executor;
    private final TagService tagService;
    private final ArticleTagRelationService relationService;
    private final RabbitTemplate rabbitTemplate;
    private final CategoryService categoryService;
    private final VisitorService visitorService;
    private final RestHighLevelClient highLevelClient;
    private final StringRedisTemplate redisTemplate;
    private final CommentService commentService;

    public ArticleServiceImpl(RabbitTemplate rabbitTemplate,
                              ArticleTagRelationService relationService,
                              TagService tagService,
                              CategoryService categoryService,
                              ThreadPoolExecutor executor,
                              VisitorService visitorService,
                              RestHighLevelClient highLevelClient,
                              StringRedisTemplate redisTemplate, CommentService commentService) {
        this.rabbitTemplate = rabbitTemplate;
        this.relationService = relationService;
        this.tagService = tagService;
        this.executor = executor;
        this.categoryService = categoryService;
        this.visitorService = visitorService;
        this.highLevelClient = highLevelClient;
        this.redisTemplate = redisTemplate;
        this.commentService = commentService;
    }

    private PageUtils queryPage(Map<String, Object> params) throws ExecutionException, InterruptedException {
        PageUtils page = new PageUtils(params);
        CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> {
            //????????????
            List<ArticleInfoVo> articleList = baseMapper.queryArticleList(page);
            articleList.forEach(item -> {
                //????????????
                Date date = new Date();
                if (item.getPublishTime() != null) {
                    date.setTime(Long.parseLong(item.getPublishTime()));
                    item.setPublishTime(DateUtils.formatTime(date));
                    item.setUpdateTime(DateUtils.formatTime(date));
                }
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
        //??????????????????????????? ????????????????????????????????????
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
    public PageUtils getFrontArticleList(Map<String, Object> params) throws Exception {
        int limit;
        int page;
        try {
            limit = Integer.parseInt((String) params.get(Constant.LIMIT));
            page = Integer.parseInt((String) params.get(Constant.PAGE));
        } catch (NumberFormatException e) {
            throw new ArgumentErrorException();
        }
        SearchRequest request = new SearchRequest(INDEX);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.from((page - 1) * limit);
        builder.size(limit);
        builder.sort("articleStatus", SortOrder.DESC).sort("publishTime", SortOrder.DESC);
        //???????????????????????????
        builder.fetchSource(null, new String[]{"content"});
        request.source(builder);
        //????????????
        SearchResponse search = highLevelClient.search(request, RequestOptions.DEFAULT);
        List<ArticleInfoVo> infoVoList = new ArrayList<>();
        SearchHits hits = search.getHits();
        for (SearchHit hit : hits.getHits()) {
            ArticleEsModel esModel = JSON.parseObject(hit.getSourceAsString(), ArticleEsModel.class);
            ArticleInfoVo vo = new ArticleInfoVo();
            BeanUtils.copyProperties(esModel, vo);
            vo.setId(esModel.getId().toString());
            vo.setPublishTime(DateUtils.formatChineseDate(esModel.getPublishTime()));
            //???????????????
            String redisVisit = redisTemplate.opsForValue().get(RedisConstant.ARTICLE_VISIT_HEADER + vo.getId());
            if (StringUtils.hasText(redisVisit)) {
                vo.setVisitNum(vo.getVisitNum() + Integer.parseInt(redisVisit));
            }
            //???????????????
            String redisLike = redisTemplate.opsForValue().get(RedisConstant.ARTICLE_LIKE_HEADER + vo.getId());
            if (StringUtils.hasText(redisLike)) {
                vo.setLikeNum(vo.getLikeNum() + Integer.parseInt(redisLike));
            }
            if (vo.getArticlePoster() == null) {
                vo.setArticlePoster("/resource/pexels-johannes-plenio-3421812.jpg");
            }else {
                vo.setArticlePoster(ResourceUrlUtil.ConvertToSDUrl(vo.getArticlePoster(),680));
            }
            infoVoList.add(vo);
        }
        return new PageUtils(infoVoList, Math.toIntExact(hits.getTotalHits().value), limit, page);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void saveArticle(ArticleVo article) throws ExecutionException, InterruptedException {
        checkAuthority(article);
        Integer authorId = Local.MANAGER_LOCAL.get().getId();
        ArticleEntity entity = new ArticleEntity();
        BeanUtils.copyProperties(article, entity);
        entity.setArticlePoster(ResourceUrlUtil.ConvertToHDUrl(entity.getArticlePoster()));
        //??????????????????
        entity.setArticleStatus(EntityConstant.ARTICLE_UNPUBLISHED);
        if (article.getId() == null) {
            //??????????????????
            entity.setAuthorId(authorId);
            entity.setCreateTime(System.currentTimeMillis());
        }
        entity.setUpdateTime(System.currentTimeMillis());
        entity.setArticleComment(article.getIsComment() ?
                EntityConstant.ARTICLE_CAN_COMMENT : EntityConstant.ARTICLE_CAN_NOT_COMMENT);
        //??????????????????
        if (entity.getContent() != null) entity.setWordNum(Long.valueOf(MarkDown.countWords(entity.getContent())));
        //????????????
        CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> {
            if (article.getId() == null) {
                baseMapper.insert(entity);
            } else {
                baseMapper.updateById(entity);
            }
        }, executor);
        //???????????????????????????
        List<TagEntity> newTags = new ArrayList<>();
        List<String> notExistedTag = article.getNotExistedTag();
        //????????????
        if (notExistedTag != null && notExistedTag.size() > 0) {
            notExistedTag.forEach(item -> {
                TagEntity tagEntity = new TagEntity();
                tagEntity.setTagName(item);
                tagEntity.setPriority(EntityConstant.PRIORITY_NORMAL);
                tagEntity.setCreatorId(authorId);
                newTags.add(tagEntity);
            });
            tagService.createTagsByName(newTags);
        }
        f1.get();
        //??????????????????????????????
        List<ArticleTagRelationEntity> relationEntityList = new ArrayList<>();
        newTags.forEach(item -> {
            ArticleTagRelationEntity relationEntity = new ArticleTagRelationEntity();
            relationEntity.setTagId(item.getId());
            relationEntity.setArticleId(entity.getId());
            relationEntityList.add(relationEntity);
        });
        List<Integer> existedTags = article.getExistedTags();
        //????????????
        if (existedTags != null && existedTags.size() > 0) {
            existedTags.forEach(item -> {
                ArticleTagRelationEntity relationEntity = new ArticleTagRelationEntity();
                relationEntity.setTagId(item);
                relationEntity.setArticleId(entity.getId());
                relationEntityList.add(relationEntity);
            });
        }
        relationService.remove(new QueryWrapper<ArticleTagRelationEntity>()
                .eq("article_id", entity.getId()));
        if (relationEntityList.size() > 0)
            relationService.saveBatch(relationEntityList);
        if (article.getId() == null) {
            //??????????????????es
            EsTypeDto dto = new EsTypeDto();
            dto.setId(entity.getId());
            dto.setType(EsType.SAVE);
            dto.setPublishTime(entity.getPublishTime());
            //??????????????????
            dto.setOverHead(article.getIsOverhead());
            rabbitTemplate.convertAndSend(RabbitConstant.ES_EXCHANGE_NAME,
                    RabbitConstant.ES_ROUTE_KEY + ".saveOrUpdate",
                    dto);
        } else {
            EsTypeDto dto = new EsTypeDto();
            dto.setId(entity.getId());
            dto.setType(EsType.UPDATE);
            dto.setPublishTime(entity.getPublishTime());
            //??????????????????
            dto.setOverHead(article.getIsOverhead());
            rabbitTemplate.convertAndSend(RabbitConstant.ES_EXCHANGE_NAME,
                    RabbitConstant.ES_ROUTE_KEY + ".saveOrUpdate",
                    dto);
        }
    }

    /**
     * ????????????
     */
    private void checkAuthority(ArticleVo article) {
        if (article.getId() != null && article.getId() == 1) {
            if (AuthorityUtils.hasAuthority("about-setting")) {
                //????????????
                redisTemplate.delete(Objects.requireNonNull(redisTemplate.keys(CacheConstant.ABOUT_CACHE_NAME + "*")));
            } else {
                throw new NoPermissionsException();
            }
        }
        if (article.getId() != null &&
                !article.getAuthorId().equals(Local.MANAGER_LOCAL.get().getId()) &&
                !AuthorityUtils.hasAuthority("article-operation-to-others")
        ) throw new NoPermissionsException();
    }

    @Override
    public ArticleVo getArticle(Long id) {
        ArticleEntity entity = baseMapper.selectById(id);
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(entity, articleVo);
        //??????????????????
        articleVo.setIsOverhead(entity.getArticleStatus().equals(EntityConstant.ARTICLE_OVERHEAD));
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
        entity.setArticleStatus(EntityConstant.ARTICLE_UNPUBLISHED);
        entity.setPublishTime(System.currentTimeMillis());
        if (entity.getContent() != null) entity.setWordNum(Long.valueOf(MarkDown.countWords(entity.getContent())));
        if (entity.getId() != null) {
            //???id???????????????????????? ????????????????????????????????????????????? ??????????????????
            entity.setUpdateTime(System.currentTimeMillis());
            baseMapper.updateById(entity);
        } else {
            //???????????????????????????
            entity.setCreateTime(System.currentTimeMillis());
            entity.setUpdateTime(System.currentTimeMillis());
            //??????????????????
            entity.setAuthorId(Local.MANAGER_LOCAL.get().getId());
            baseMapper.insert(entity);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteArticle(List<Long> articleId) throws ExecutionException, InterruptedException {
        CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> commentService.removeCommentByArticleIds(articleId), executor);
        relationService.removeRelationByArticleIds(articleId);
        f1.get();
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
        //????????????
        CompletableFuture<List<CategoryEntity>> f3 = CompletableFuture.supplyAsync(categoryService::list);
        //????????????
        List<ArticleEntity> articleList = new LinkedList<>(baseMapper.getAllArticleTitleAndCategoryId());
        //?????????????????????
        Object[][] articlePopular = new Object[articleList.size()][4];
        for (int i = 0; i < articleList.size(); i++) {
            ArticleEntity entity = articleList.get(i);
            articlePopular[i][0] = entity.getTitle();
            articlePopular[i][1] = entity.getLikeNum();
            articlePopular[i][2] = entity.getCommentNum();
            articlePopular[i][3] = entity.getVisitNum();
        }
        dashBoardVo.setArticlePopular(articlePopular);
        //????????????????????????
        dashBoardVo.setArticleNum(articleList.size() - 1);

        List<CategoryEntity> categoryList = f3.get();
        ArrayList<DashBoardVo.ArticleDistribution> articleDistributions = new ArrayList<>();
        for (CategoryEntity categoryEntity : categoryList) {
            DashBoardVo.ArticleDistribution distribution = new DashBoardVo.ArticleDistribution();
            //??????????????????
            distribution.setName(categoryEntity.getCategoryName());
            List<DashBoardVo.ArticleDistribution.Children> childrenList = new ArrayList<>();
            //????????????????????????
            for (int i = 0; i < articleList.size(); i++) {
                ArticleEntity articleEntity = articleList.get(i);
                //??????????????????????????????????????????????????????articleDistributions
                if (categoryEntity.getId().equals(articleEntity.getArticleCategoryId())) {
                    //????????????
                    DashBoardVo.ArticleDistribution.Children children = new DashBoardVo.ArticleDistribution.Children();
                    children.setName(articleEntity.getTitle());
                    //??????????????????????????????????????? ????????????
                    children.setValue(articleEntity.getLikeNum() == 0 ? 1 : Math.toIntExact(articleEntity.getLikeNum()));
                    childrenList.add(children);
                    articleList.remove(articleEntity);
                    //?????????????????????????????????
                    i--;
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

    @Override
    public void updateArticleStatus(Long id, Integer articleStatus) {
        baseMapper.updateArticleStatus(id, articleStatus);
    }

    @Override
    public void likeArticle(Long id) {
        baseMapper.likeArticle(id);
    }


    @Override
    @Cacheable(cacheNames = CacheConstant.ABOUT_CACHE_NAME)
    public ArticleEntity getAbout() {
        return getOne(new QueryWrapper<ArticleEntity>().lambda().eq(ArticleEntity::getId, 1));
    }

    @Override
    public String getCreatorEmailByArticleId(Long articleId) {
        return baseMapper.getCreatorEmailByArticleId(articleId);
    }

    @Override
    public void addArticleParameter(String field, Map<String, String> map) {
        if (!map.isEmpty()) {
            baseMapper.updateParameter(field, map);
        }
    }

    @Override
    public void updateComment(Map<String, String> map) {
        if (!map.isEmpty()) {
            baseMapper.updateComment(map);
        }
    }

    @Override
    public void initArticle() throws Exception {
        ArticleEntity about = new ArticleEntity();
        about.setId(1L);
        about.setTitle("about");
        about.setAuthorId(AuthorityUtils.ROOT_ADMIN);
        about.setArticleStatus(EntityConstant.ARTICLE_PUBLISHED);
        about.setContent("# ????????????????????????????????????????????????????????????????????????->????????????????????????\n" +
                "\n" +
                "*?????????????????????*\n" +
                "- ??????????????????\n" +
                "- ?????????????????????\n" +
                "- ??????????????????????????????\n" +
                "- ??????????????????????????????\n" +
                "- ???????????????????????????\n" +
                "- ......\n" +
                "\n" +
                "????????????????????????????????????");
        about.setArticleComment(EntityConstant.ARTICLE_CAN_NOT_COMMENT);
        about.setPublishTime(System.currentTimeMillis());
        about.setCreateTime(System.currentTimeMillis());
        about.setUpdateTime(System.currentTimeMillis());
        baseMapper.insertWithId(about);
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setPriority(EntityConstant.PRIORITY_NORMAL);
        categoryEntity.setCategoryName("Readme");
        categoryEntity.setCreatorId(AuthorityUtils.ROOT_ADMIN);
        categoryService.save(categoryEntity);
        ManagerEntity manager = new ManagerEntity();
        manager.setId(AuthorityUtils.ROOT_ADMIN);
        Local.MANAGER_LOCAL.set(manager);
        ArticleVo welcomeArticle = new ArticleVo();
        welcomeArticle.setTitle("???????????????????????????????????????????????????");
        welcomeArticle.setArticleStatus(EntityConstant.ARTICLE_PUBLISHED);
        welcomeArticle.setIsComment(true);
        welcomeArticle.setIsOverhead(true);
        welcomeArticle.setArticleDesc("???????????????????????????????????????????????????????????????????????????????????????????????????");
        welcomeArticle.setNotExistedTag(Arrays.asList("???????????????", "??????"));
        welcomeArticle.setArticleCategoryId(categoryEntity.getId());
        welcomeArticle.setContent("# ???????????????????????????????????????????????????\n" +
                "\n" +
                "> ???????????????????????????????????????????????????????????????????????????????????????????????????\n" +
                "\n" +
                "\n" +
                "?????????????????????????????????\n" +
                "**?????????**\n" +
                "- ??????????????????\n" +
                "- ?????????????????????\n" +
                "- ??????????????????????????????\n" +
                "- ??????????????????????????????\n" +
                "- ???????????????????????????\n" +
                "......\n" +
                "\n" +
                "???????????????????????????????????????");
        welcomeArticle.setPublishTime(System.currentTimeMillis());
        saveArticle(welcomeArticle);
        Local.MANAGER_LOCAL.remove();
    }

}