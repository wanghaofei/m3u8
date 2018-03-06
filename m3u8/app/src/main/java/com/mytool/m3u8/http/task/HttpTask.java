package com.mytool.m3u8.http.task;

import com.alibaba.fastjson.JSON;
import com.mytool.m3u8.http.interfaces.IHttpListener;
import com.mytool.m3u8.http.interfaces.IHttpService;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.FutureTask;

/**
 * Created by wanghaofei on 2018/1/8.
 */

public class HttpTask<T> implements Runnable {

    private IHttpService httpService;
    private FutureTask futureTask;

    public HttpTask(RequestHodler<T> requestHodler) {
        httpService = requestHodler.getHttpService();
        httpService.setHttpListener(requestHodler.getHttpListener());
        httpService.setUrl(requestHodler.getUrl());

        //增加方法
        IHttpListener httpListener=requestHodler.getHttpListener();
        httpListener.addHttpHeader(httpService.getHttpHeadMap());

        T request = requestHodler.getRequestInfo();
        String requestInfo = JSON.toJSONString(request);

        try {
            httpService.setRequestData(requestInfo.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        httpService.excute();
    }


    /**
     * 新增方法
     */
    public void start()
    {
        futureTask=new FutureTask(this,null);
        try {
            ThreadPoolManager.getInstance().execte(futureTask);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    /**
     * 新增方法
     */
    public  void pause()
    {
        httpService.pause();
        if(futureTask!=null)
        {
            ThreadPoolManager.getInstance().removeTask(futureTask);
        }

    }


}
