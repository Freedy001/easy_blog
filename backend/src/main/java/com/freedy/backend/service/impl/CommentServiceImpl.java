package com.freedy.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.freedy.backend.common.utils.DateUtils;
import com.freedy.backend.common.utils.Query;
import com.freedy.backend.entity.vo.CommentAdminVo;
import com.freedy.backend.entity.vo.CommentVo;
import com.freedy.backend.exception.ArgumentErrorException;
import com.freedy.backend.service.ArticleService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedy.backend.common.utils.PageUtils;

import com.freedy.backend.dao.CommentDao;
import com.freedy.backend.entity.CommentEntity;
import com.freedy.backend.service.CommentService;


@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentDao, CommentEntity> implements CommentService {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ArticleService articleService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        long id;
        int limit;
        int page;
        try {
            id = Long.parseLong((String) params.get("articleId"));
            limit = Integer.parseInt((String) params.get("limit"));
            page = Integer.parseInt((String) params.get("page"));
        } catch (NumberFormatException e) {
            throw new ArgumentErrorException();
        }
        //将其转化为链表 效率更高
        LinkedList<CommentEntity> commentList = new LinkedList<>(baseMapper.getCommentList(page, limit, id));
        ArrayList<CommentVo> parentNode = new ArrayList<>();
        //找出所有根节点 这里必须使用迭代器否则会报ConcurrentModificationException
        ListIterator<CommentEntity> iterator = commentList.listIterator();
        while (iterator.hasNext()) {
            CommentEntity item = iterator.next();
            if (item.getFatherCommentId() == null) {
                CommentVo commentVo = new CommentVo();
                BeanUtils.copyProperties(item, commentVo);
                String time = DateUtils.formatTime(new Date(item.getCreateTime()));
                commentVo.setCreateTime(time);
                parentNode.add(commentVo);
                //因为是树状结构,一个节点只能被一个节点所引用
                //所以可以删掉 减少后续遍历次数
                iterator.remove();
            }
        }
        //找出每个节点的所有子节点
        for (CommentVo commentVo : parentNode) {
            buildCommentTree(commentList, commentVo, commentVo);
            commentVo.getChildComment().sort((o1, o2) -> Math.toIntExact(DateUtils.formatTime(o2.getCreateTime()).getTime() - DateUtils.formatTime(o1.getCreateTime()).getTime()));
        }
        parentNode.sort((o1, o2) -> Math.toIntExact(DateUtils.formatTime(o2.getCreateTime()).getTime() - DateUtils.formatTime(o1.getCreateTime()).getTime()));

        return new PageUtils(parentNode, 10, limit, page);
    }

    /**
     * 将评论树扁平化处理
     *
     * @param commentList 迭代器对象
     * @param childNode   子节点
     * @param parentNode  父节点
     */
    private void buildCommentTree(LinkedList<CommentEntity> commentList, CommentVo childNode, CommentVo parentNode) {
        for (int i = 0; i < commentList.size(); i++) {
            CommentEntity entity = commentList.get(i);
            if (childNode.getId().equals(entity.getFatherCommentId())) {
                //找到parentNode的直接子项
                CommentVo childVo = new CommentVo();
                BeanUtils.copyProperties(entity, childVo);
                String time = DateUtils.formatTime(new Date(entity.getCreateTime()));
                childVo.setCreateTime(time);
                childVo.setParentName(childNode.getUsername());
                parentNode.getChildComment().add(childVo);
                //同上58行
                commentList.remove(entity);
                buildCommentTree(commentList, childVo, parentNode);
            }
        }
    }

    @Override
    public void publishComment(CommentEntity comment) {
        comment.setCreateTime(new Date().getTime());
        if (comment.getFatherCommentId() != null) {
            CommentEntity fatherEntity = baseMapper.selectById(comment.getFatherCommentId());
            comment.setFlore(fatherEntity.getFlore());
        } else {
            Integer topFlore;
            RLock lock = redissonClient.getLock("lock");
            try {
                //加锁防止获取到相同楼层
                lock.lock(30, TimeUnit.SECONDS);
                topFlore = baseMapper.getTopFlore(comment.getArticleId());
            } finally {
                lock.unlock();
            }
            if (topFlore == null) {
                comment.setFlore(1);
            } else {
                comment.setFlore(topFlore + 1);
            }
        }
        baseMapper.insert(comment);
    }

//    @Override
//    public PageUtils queryAdminPage(Map<String, Object> params) throws ExecutionException, InterruptedException {
//        IPage<CommentEntity> page = this.page(new Query<CommentEntity>().getPage(params),
//                new QueryWrapper<CommentEntity>().lambda().orderByDesc(CommentEntity::getCreateTime));
//        List<CompletableFuture<CommentAdminVo>> futureList = page.getRecords().stream().map(item -> CompletableFuture.supplyAsync(() -> {
//            CommentAdminVo adminVo = new CommentAdminVo();
//            BeanUtils.copyProperties(item, adminVo);
//            CommentAdminVo.Article article = articleService.getArticleTitles(Collections.singletonList(item.getArticleId())).get(0);
//            adminVo.setArticle(article);
//            adminVo.setId(item.getId().toString());
//            adminVo.setCreateTime(DateUtils.formatTime(new Date(item.getCreateTime())));
//            if (item.getFatherCommentId() != null) {
//                CommentEntity entity = baseMapper.selectById(item.getFatherCommentId());
//                adminVo.setFatherComment(entity.getContent());
//            }
//            return adminVo;
//        })).collect(Collectors.toList());
//        List<CommentAdminVo> vos = new ArrayList<>();
//        for (CompletableFuture<CommentAdminVo> item : futureList) {
//            vos.add(item.get());
//        }
//        return new PageUtils(vos, (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
//    }

    @Override
    public PageUtils queryAdminPage(Map<String, Object> params) throws ExecutionException, InterruptedException {
        PageUtils utils = new PageUtils(params);
        CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> {
            List<CommentAdminVo> vos = baseMapper.getAdminCommentList(utils);
            utils.setList(vos);
        });
        utils.setTotalCount(count());
        f1.get();
        return utils;
    }

}