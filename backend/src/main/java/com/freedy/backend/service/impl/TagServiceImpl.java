package com.freedy.backend.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedy.backend.common.utils.PageUtils;
import com.freedy.backend.common.utils.Query;

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

}