package com.freedy.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedy.backend.dao.SettingDao;
import com.freedy.backend.dao.ShorthandDao;
import com.freedy.backend.entity.SettingEntity;
import com.freedy.backend.entity.ShorthandEntity;
import com.freedy.backend.entity.TagEntity;
import com.freedy.backend.service.ShorthandService;
import com.freedy.backend.utils.PageUtils;
import com.freedy.backend.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Freedy
 * @date 2021/5/20 1:58
 */
@Service
public class ShorthandServiceImpl extends ServiceImpl<ShorthandDao, ShorthandEntity>  implements ShorthandService {
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ShorthandEntity> page = this.page(
                new Query<ShorthandEntity>().getPage(params),
                new QueryWrapper<ShorthandEntity>()
        );
        return new PageUtils(page);
    }

}
