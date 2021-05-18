package com.freedy.backend.service.impl;

import com.freedy.backend.entity.ManagerEntity;
import com.freedy.backend.entity.TagEntity;
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

import com.freedy.backend.dao.CategoryDao;
import com.freedy.backend.entity.CategoryEntity;
import com.freedy.backend.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void deleteCategories(List<Integer> ids) {
        ManagerEntity manager = Local.MANAGER_LOCAL.get();
        if (manager.getStatus()!=1){
            List<CategoryEntity> tagEntities = baseMapper.selectBatchIds(ids);
            Integer userId = manager.getId();
            for (CategoryEntity entity : tagEntities) {
                if (!entity.getCreatorId().equals(userId)) {
                    throw new NoPermissionsException();
                }
            }
        }
        baseMapper.deleteBatchIds(ids);
    }

}