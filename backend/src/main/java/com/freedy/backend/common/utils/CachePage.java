package com.freedy.backend.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * PageUtils的泛型模式
 *  因为springCache不能转换泛型为任意的
 *  所有当使用springCache时将使用此类作为分页工具类
 * @author Freedy
 * @date 2021/5/15 16:20
 */
public class CachePage<T> extends PageUtils{

    public static void main(String[] args) {

    }

    public CachePage(List<T> list, int totalCount, int pageSize, int currPage) {
        super(list, totalCount, pageSize, currPage);
    }

    public CachePage(IPage<T> page) {
        super(page);
    }

    public CachePage(Map<String, Object> params) {
        super(params);

    }


    @Override
    public List<?> getList() {
        return super.getList();
    }

    @Override
    public void setList(List<?> list) {
        super.setList(list);
    }
}
