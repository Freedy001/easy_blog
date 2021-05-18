package com.freedy.backend.service.impl;

import com.freedy.backend.entity.ManagerEntity;
import com.freedy.backend.exception.NoPermissionsException;
import com.freedy.backend.utils.Local;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedy.backend.utils.PageUtils;
import com.freedy.backend.utils.Query;

import com.freedy.backend.dao.TagDao;
import com.freedy.backend.entity.TagEntity;
import com.freedy.backend.service.TagService;


@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagDao, TagEntity> implements TagService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TagEntity> page = this.page(
                new Query<TagEntity>().getPage(params),
                new QueryWrapper<TagEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public void createTagsByName(List<TagEntity> notExistedTag) {
        baseMapper.createTagsByName(notExistedTag);
    }

    @Override
    public void deleteTags(List<Integer> ids) {
        ManagerEntity manager = Local.MANAGER_LOCAL.get();
        if (manager.getStatus() != 1) {
            List<TagEntity> tagEntities = baseMapper.selectBatchIds(ids);
            Integer userId = manager.getId();
            for (TagEntity entity : tagEntities) {
                if (!entity.getCreatorId().equals(userId)) {
                    throw new NoPermissionsException();
                }
            }
        }
        baseMapper.deleteBatchIds(ids);
    }

}