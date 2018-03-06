package com.mytool.m3u8.http.interfaces;

/**
 * Created by wanghaofei on 2018/1/5.
 */

public interface IDataListener<M> {

    /**
     * 回调结果给调用层
     * @param m
     */
    void onSuccess(M m);

    void errorCode(int code);

    void fail(String msg);

}
