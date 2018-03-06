package com.mytool.m3u8.http.interfaces;

import java.util.Map;

/**
 * Created by wanghaofei on 2018/1/5.
 */
public interface IHttpService {

    /**
     * 设置url
     *
     * @param url
     */
    void setUrl(String url);

    /**
     * 执行获取网络
     */
    void excute();

    /**
     * 设置处理接口
     *
     * @param httpListener
     */
    void setHttpListener(IHttpListener httpListener);

    /**
     * 设置请求参数
     * String  1
     * byte[]  2
     */
    void setRequestData(byte[] requestData);



    void pause();

    /**
     *
     * 以下的方法是 额外添加的
     * 获取请求头的map
     * @return
     */
    Map<String,String> getHttpHeadMap();

    boolean cancle();

    boolean isCancle();

    boolean isPause();


}
