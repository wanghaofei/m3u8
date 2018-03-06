package com.mytool.m3u8.dao.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.mytool.m3u8.MyApplication;

import java.io.File;

/**
 * Created by wanghaofei on 2018/1/4.
 */

public class BaseDaoFactory {

    private String sqliteDatabasePath;

    private SQLiteDatabase sqLiteDatabase;


    private static BaseDaoFactory baseDaoFactory = new BaseDaoFactory();


    public BaseDaoFactory() {
        sqliteDatabasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/user.db";
        openDatabase();
    }

    public static BaseDaoFactory getInstance() {
        return baseDaoFactory;
    }

    //这里的M是传入的实体类，如：user,T是数据存储实现类如:userdao,<T extends BaseDao<M>,M>此处代表泛型的类型
    public synchronized <T extends BaseDao<M>, M> T getDataHelper(Class<T> tClass, Class<M> entityClass) {

        BaseDao baseDao = null;

        try {
            baseDao = tClass.newInstance();
            baseDao.init(entityClass, sqLiteDatabase);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T) baseDao;
    }


//    public <T> T getData(Class<T> tClass){
//
//    }


    private void openDatabase() {

//        String DATABASE_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()
//                + "/com/cn21/account/public";
////数据库名
//         String DATABASE_FILENAME = "account.db3";
//
//        String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
//        File dir = new File(DATABASE_PATH);
//        if(!dir.exists())
//            dir.mkdirs();


//        //创建一个帮助类对象
//        MySqliteOpenHelper mySqliteOpenHelper = new MySqliteOpenHelper(MyApplication.getInstance());
//        //调用getReadableDatabase方法,来初始化数据库的创建
//        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();

        this.sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(sqliteDatabasePath,null);
    }

}
