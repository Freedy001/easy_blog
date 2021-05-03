package com.freedy.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.freedy.backend.common.utils.PageUtils;
import com.freedy.backend.entity.TagEntity;

import java.util.List;
import java.util.Map;

/**
 * 文章标签
 *
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
public interface TagService extends IService<TagEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据标签名称列表 创建标签
     */
    void createTagsByName(List<TagEntity> notExistedTag);

}

