package com.freedy.backend;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.freedy.backend.common.utils.Query;
import com.freedy.backend.dao.ArticleDao;
import com.freedy.backend.dao.ManagerDao;
import com.freedy.backend.dao.TagDao;
import com.freedy.backend.entity.ArticleEntity;
import com.freedy.backend.entity.ManagerEntity;
import com.freedy.backend.entity.vo.ArticleInfoVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Freedy
 * @date 2021/5/2 21:33
 */
@SpringBootTest
public class DaoTest {
    @Autowired
    private TagDao tagDao;
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private ManagerDao managerDao;
    @Test
    public void test(){
        ManagerEntity userEntity = managerDao.selectOne(new QueryWrapper<ManagerEntity>().eq("username", "username"));
        System.out.println(userEntity);
    }
}
