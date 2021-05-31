package com.freedy.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedy.backend.SysSetting.LoadSetting;
import com.freedy.backend.constant.EntityConstant;
import com.freedy.backend.utils.PageUtils;
import com.freedy.backend.utils.Query;
import com.freedy.backend.constant.FileConstant;
import com.freedy.backend.dao.ResourceDao;
import com.freedy.backend.entity.ResourceEntity;
import com.freedy.backend.service.ResourceService;
import com.freedy.backend.utils.ResourceUrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Freedy
 * @date 2021/5/7 13:01
 */
@Service("resourceService")
public class ResourceServiceImpl extends ServiceImpl<ResourceDao, ResourceEntity> implements ResourceService {

    @Autowired
    private LoadSetting loadSetting;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ResourceEntity> page = baseMapper.selectPage(
                new Query<ResourceEntity>().getPage(params),
                new QueryWrapper<>()
        );
        //返回压缩图片
        page.getRecords().forEach(item-> item.setResourceUrl(ResourceUrlUtil.ConvertToSDUrl(item.getResourceUrl())));
        return new PageUtils(page);
    }

}
