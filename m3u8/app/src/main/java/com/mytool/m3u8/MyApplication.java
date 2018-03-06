package com.mytool.m3u8;

import android.app.Application;

import org.xutils.x;

/**
 * Created by wanghaofei on 2017/12/22.
 */

public class MyApplication extends Application {

    private static MyApplication myApplication;

    public static MyApplication getInstance(){

        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        myApplication = new MyApplication();
    }
}
