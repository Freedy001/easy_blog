package com.freedy.backend.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedy.backend.utils.PageUtils;
import com.freedy.backend.utils.Query;

import com.freedy.backend.dao.VisitorDao;
import com.freedy.backend.entity.VisitorEntity;
import com.freedy.backend.service.VisitorService;


@Service("visitorService")
public class VisitorServiceImpl extends ServiceImpl<VisitorDao, VisitorEntity> implements VisitorService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<VisitorEntity> page = this.page(
                new Query<VisitorEntity>().getPage(params),
                new QueryWrapper<VisitorEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<Integer> visitorNumInPath7Days(long time) {
        return baseMapper.visitorNumInPath7Days(time);
    }

}