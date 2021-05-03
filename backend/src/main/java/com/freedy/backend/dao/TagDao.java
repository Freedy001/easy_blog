package com.freedy.backend.dao;

import com.freedy.backend.entity.TagEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 文章标签
 * 
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
@Mapper
public interface TagDao extends BaseMapper<TagEntity> {

    void createTagsByName(List<TagEntity> notExistedTag);

}
