package com.freedy.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedy.backend.utils.PageUtils;
import com.freedy.backend.utils.Query;
import com.freedy.backend.constant.FileConstant;
import com.freedy.backend.dao.ResourceDao;
import com.freedy.backend.entity.ResourceEntity;
import com.freedy.backend.service.ResourceService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Freedy
 * @date 2021/5/7 13:01
 */
@Service("resourceService")
public class ResourceServiceImpl extends ServiceImpl<ResourceDao, ResourceEntity> implements ResourceService{

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ResourceEntity> page = baseMapper.selectPage(
                new Query<ResourceEntity>().getPage(params),
                new QueryWrapper<>()
        );
        //返回压缩图片
        for (ResourceEntity record : page.getRecords()) {
            String[] split = record.getResourceUrl().split("-", 4);
            String zipUrl = split[0]+"-"+split[1]+"-"+split[2] + "-" + FileConstant.ZIP_IMAGE_INFIX + "-" + split[3];
            record.setResourceUrl(zipUrl);
        }
        return new PageUtils(page);
    }

}
