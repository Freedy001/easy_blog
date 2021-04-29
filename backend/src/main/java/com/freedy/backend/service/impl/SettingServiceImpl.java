package com.freedy.backend.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedy.backend.common.utils.PageUtils;
import com.freedy.backend.common.utils.Query;

import com.freedy.backend.dao.SettingDao;
import com.freedy.backend.entity.SettingEntity;
import com.freedy.backend.service.SettingService;


@Service("settingService")
public class SettingServiceImpl extends ServiceImpl<SettingDao, SettingEntity> implements SettingService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SettingEntity> page = this.page(
                new Query<SettingEntity>().getPage(params),
                new QueryWrapper<SettingEntity>()
        );

        return new PageUtils(page);
    }

}