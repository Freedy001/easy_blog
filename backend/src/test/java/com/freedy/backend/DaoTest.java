package com.freedy.backend;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.freedy.backend.api.ArticleController;
import com.freedy.backend.dao.ArticleDao;
import com.freedy.backend.dao.ManagerDao;
import com.freedy.backend.dao.TagDao;
import com.freedy.backend.entity.ManagerEntity;
import com.freedy.backend.utils.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;

/**
 * @author Freedy
 * @date 2021/5/2 21:33
 */
@SpringBootTest
public class DaoTest {
    @Autowired
    private ArticleController articleController;

    @Test
    public void test() throws ExecutionException, InterruptedException {
        Result list = articleController.info(1L);
        System.out.println(list);
    }
}
