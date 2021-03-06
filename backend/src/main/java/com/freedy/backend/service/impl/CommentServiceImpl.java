package com.freedy.backend.service.impl;

import com.freedy.backend.utils.AuthorityUtils;
import com.freedy.backend.utils.DateUtils;
import com.freedy.backend.utils.Local;
import com.freedy.backend.entity.ManagerEntity;
import com.freedy.backend.entity.vo.comment.CommentAdminVo;
import com.freedy.backend.entity.vo.comment.CommentVo;
import com.freedy.backend.exception.ArgumentErrorException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedy.backend.utils.PageUtils;

import com.freedy.backend.dao.CommentDao;
import com.freedy.backend.entity.CommentEntity;
import com.freedy.backend.service.CommentService;

import static com.freedy.backend.constant.RabbitConstant.*;


/**
 * @author Freedy
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentDao, CommentEntity> implements CommentService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ThreadPoolExecutor executor;

    @Override
    public PageUtils queryPage(Map<String, Object> params) throws ExecutionException, InterruptedException {
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
        CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(() -> baseMapper.getCommentNumByArticleId(id), executor);
        //????????????????????? ????????????
        LinkedList<CommentEntity> commentList = new LinkedList<>(baseMapper.getCommentList(page, limit, id));
        ArrayList<CommentVo> parentNode = new ArrayList<>();
        //????????????????????? ???????????????????????????????????????ConcurrentModificationException
        ListIterator<CommentEntity> iterator = commentList.listIterator();
        while (iterator.hasNext()) {
            CommentEntity item = iterator.next();
            if (item.getFatherCommentId() == null) {
                CommentVo commentVo = new CommentVo();
                BeanUtils.copyProperties(item, commentVo);
                String time = DateUtils.formatTime(new Date(item.getCreateTime()));
                commentVo.setCreateTime(time);
                parentNode.add(commentVo);
                //?????????????????????,??????????????????????????????????????????
                //?????????????????? ????????????????????????
                iterator.remove();
            }
        }
        //????????????????????????????????????
        for (CommentVo commentVo : parentNode) {
            buildCommentTree(commentList, commentVo, commentVo);
            commentVo.getChildComment().sort((o1, o2) -> Math.toIntExact(DateUtils.formatTime(o2.getCreateTime()).getTime() - DateUtils.formatTime(o1.getCreateTime()).getTime()));
        }
        parentNode.sort((o1, o2) -> Math.toIntExact(DateUtils.formatTime(o2.getCreateTime()).getTime() - DateUtils.formatTime(o1.getCreateTime()).getTime()));
        Integer num = f1.get();
        return new PageUtils(parentNode, num, limit, page);
    }

    /**
     * ???????????????????????????
     *
     * @param commentList ???????????????
     * @param childNode   ?????????
     * @param parentNode  ?????????
     */
    private void buildCommentTree(LinkedList<CommentEntity> commentList, CommentVo childNode, CommentVo parentNode) {
        for (int i = 0; i < commentList.size(); i++) {
            CommentEntity entity = commentList.get(i);
            if (childNode.getId().equals(entity.getFatherCommentId())) {
                //??????parentNode???????????????
                CommentVo childVo = new CommentVo();
                BeanUtils.copyProperties(entity, childVo);
                String time = DateUtils.formatTime(new Date(entity.getCreateTime()));
                childVo.setCreateTime(time);
                childVo.setParentName(childNode.getUsername());
                parentNode.getChildComment().add(childVo);
                //??????58???
                commentList.remove(entity);
                buildCommentTree(commentList, childVo, parentNode);
            }
        }
    }

    @Override
    public void publishComment(CommentEntity comment) {
        comment.setHasRead(0);
        comment.setCreateTime(System.currentTimeMillis());
        if (comment.getFatherCommentId() != null) {
            CommentEntity fatherEntity = baseMapper.selectById(comment.getFatherCommentId());
            comment.setFlore(fatherEntity.getFlore());
        } else {
            Integer topFlore;
            ReentrantLock reentrantLock = new ReentrantLock();
            reentrantLock.lock();
            try {
                //?????????????????????????????????
                topFlore = baseMapper.getTopFlore(comment.getArticleId());
            } finally {
                reentrantLock.unlock();
            }
            if (topFlore == null) {
                comment.setFlore(1);
            } else {
                comment.setFlore(topFlore + 1);
            }
        }
        baseMapper.insert(comment);
        //???????????? ???????????????????????????????????????
        rabbitTemplate.convertAndSend(THIRD_PART_EXCHANGE_NAME, EMAIL_REPLAY_ROUTING_KEY, comment);
        //????????????????????????ip????????????????????????  ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        rabbitTemplate.convertAndSend(THIRD_PART_EXCHANGE_NAME, IP_REGION_ROUTING_KEY, comment);
    }

    @Override
    public PageUtils queryAdminPage(Map<String, Object> params) throws ExecutionException, InterruptedException {
        PageUtils utils = new PageUtils(params);
        Integer managerId = Local.MANAGER_LOCAL.get().getId();
        String permissionStr = Local.PERMISSION_LOCAL.get();
        CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> {
            List<CommentAdminVo> vos;
            if (AuthorityUtils.hasAuthority(permissionStr,"comment-operation-to-others")) {
                vos = baseMapper.getAdminCommentList(utils);
            } else {
                vos = baseMapper.getOwnCommentList(utils, managerId);
            }
            List<String> list = vos.stream().map(CommentAdminVo::getId).collect(Collectors.toList());
            vos.forEach(item->item.setCreateTime(DateUtils.formatTime(Long.parseLong(item.getCreateTime()))));
            if (list.size() > 0) {
                baseMapper.readAll(list);
            }
            utils.setList(vos);
        }, executor);
        utils.setTotalCount(count());
        f1.get();
        return utils;
    }

    @Override
    public void replay(CommentEntity commentEntity) {
        ManagerEntity entity = Local.MANAGER_LOCAL.get();
        commentEntity.setUsername(entity.getNickname());
        commentEntity.setEmail(entity.getEmail());
        commentEntity.setCommentStatus(1);
        publishComment(commentEntity);
    }

    @Override
    public void deleteComment(List<Long> ids) {
        List<CommentEntity> commentEntities = baseMapper.selectBatchIds(ids);
        List<CommentEntity> commentEntitiesAndFlore = new LinkedList<>(baseMapper.getAllCommentInOneFlore(commentEntities));
        ArrayList<Long> deleteIds = new ArrayList<>(ids);
        for (CommentEntity entity : commentEntities) {
            if (entity.getFatherCommentId() == null) {
                //????????????????????????
                for (int i = 0; i < commentEntitiesAndFlore.size(); i++) {
                    CommentEntity flore = commentEntitiesAndFlore.get(i);
                    if (flore.getArticleId().equals(entity.getArticleId()) &&
                            flore.getFlore().equals(entity.getFlore())
                    ) {
                        deleteIds.add(flore.getId());
                        commentEntitiesAndFlore.remove(flore);
                    }
                }
            } else {
                buildDeleteIds(deleteIds, entity, commentEntitiesAndFlore);
            }
        }
        baseMapper.deleteBatchIds(deleteIds);
    }

    /**
     * ????????????????????????????????????
     * ?????????????????????id???????????????list???
     * ??????????????????
     *
     * @param deleteIds               ??????id list
     * @param parentComment           ????????????
     * @param commentEntitiesAndFlore ???????????????
     */
    private void buildDeleteIds(List<Long> deleteIds,
                                CommentEntity parentComment,
                                List<CommentEntity> commentEntitiesAndFlore) {
        for (int i = 0; i < commentEntitiesAndFlore.size(); i++) {
            CommentEntity flore = commentEntitiesAndFlore.get(i);
            if (parentComment.getId().equals(flore.getFatherCommentId())) {
                deleteIds.add(flore.getId());
                commentEntitiesAndFlore.remove(flore);
                buildDeleteIds(deleteIds, flore, commentEntitiesAndFlore);
            }
        }
    }

    @Override
    public void confirmExaminations(List<Long> asList) {
        //????????????
        rabbitTemplate.convertAndSend(THIRD_PART_EXCHANGE_NAME, EMAIL_REPLAY_ROUTING_KEY, asList);
        baseMapper.confirmExaminations(asList);
    }

    @Override
    public Map<String, String> getArticleCommentNum() {
        return baseMapper.getArticleCommentNum().stream()
                .collect(HashMap::new,
                        (map, result) -> map.put(result.get("id").toString(), result.get("count").toString()),
                        HashMap::putAll);
    }

    @Override
    public void removeCommentByArticleIds(List<Long> articleId) {
        baseMapper.removeCommentByArticleIds(articleId);
    }

    @Override
    public int countNotRead(Integer id) {
        return baseMapper.countNotRead(id);
    }


}