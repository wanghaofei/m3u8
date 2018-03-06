package com.mytool.m3u8.http.interfaces;

import org.apache.http.HttpEntity;

import java.util.Map;

/**
 * Created by wanghaofei on 2018/1/5.
 */

public interface IHttpListener {

    /**
     * 网络访问
     * 处理结果  回调
     * @param httpEntity
     */
    void onSuccess(HttpEntity httpEntity);

    void errorCode(int code);

    void fail(String msg);

    void addHttpHeader(Map<String,String> headerMap);
}
