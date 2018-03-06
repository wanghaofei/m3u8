package com.mytool.m3u8.dao;

import com.mytool.m3u8.dao.db.annotion.DbFiled;
import com.mytool.m3u8.dao.db.annotion.DbTable;

/**
 * Created by wanghaofei on 2018/1/4.
 */

@DbTable("tb_user")
public class User {

    public int user_Id=0;

    public int getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(int user_Id) {
        this.user_Id = user_Id;
    }

    public User(int user_Id, String name, String password) {
        this.user_Id = user_Id;
        this.name = name;
        this.password = password;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    private String pwd;

    @DbFiled("name")
    public String name;

    @DbFiled("password")
    public String password;

    public  Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_Id=" + user_Id +
                ", pwd='" + pwd + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
