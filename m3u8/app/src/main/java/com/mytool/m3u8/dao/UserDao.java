package com.mytool.m3u8.dao;

import com.mytool.m3u8.dao.db.BaseDao;

import java.util.List;

/**
 * Created by wanghaofei on 2018/1/4.
 */

public class UserDao extends BaseDao {
    @Override
    protected String createTable() {
        return "create table if not exists tb_user(name varchar(20),password varchar(10))";
    }




    /**
     * 得到当前登录的User
     * @return
     */
    public User getCurrentUser() {
        User user=new User();
        user.setStatus(1);
        List<User> list=query(user);
        if(list.size()>0)
        {
            return list.get(0);
        }
        return null;
    }

}
