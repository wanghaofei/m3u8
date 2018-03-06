package com.mytool.m3u8.dao.db;

import java.util.List;

/**
 * Created by wanghaofei on 2018/1/4.
 */

public interface IBaseDao<T> {

    /**
     * 插入数据
     *
     * @param entity
     * @return
     */
    Long insert(T entity);

    /**
     * 更新数据
     *
     * @param entity
     * @param where
     * @return
     */
    int update(T entity, T where);

    /**
     * 根据条件删除
     *
     * @param where
     * @return
     */
    int delete(T where);

    /**
     * 根据条件查询
     *
     * @param where
     * @return
     */
    List<T> query(T where);

    List<T> query(T where, String orderBy, Integer startIndex, Integer limit);

    List<T> query(String sql);

}
