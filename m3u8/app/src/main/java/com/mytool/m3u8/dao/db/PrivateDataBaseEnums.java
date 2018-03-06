package com.mytool.m3u8.dao.db;

import android.os.Environment;

import com.mytool.m3u8.dao.User;
import com.mytool.m3u8.dao.UserDao;

import java.io.File;

/**
 * Created by wanghaofei on 2018/1/11.
 */

public enum PrivateDataBaseEnums {

    database("local/data/database/")
    ;

    private String value;

    PrivateDataBaseEnums(String value) {
        this.value = value;
    }

//    public String getValue() {
//        UserDao userDao=BaseDaoFactory.getInstance().getDataHelper(UserDao.class,User.class);
//        if(userDao!=null)
//        {
//            User currentUser=userDao.getCurrentUser();
//            if(currentUser!=null)
//            {
//                File file=new File(Environment.getExternalStorageDirectory(),"update");
//                if(!file.exists())
//                {
//                    file.mkdirs();
//                }
//                return file.getAbsolutePath()+"/"+currentUser.getUser_id()+"/logic.db";
//            }
//
//        }
//        return value;
//    }
}
