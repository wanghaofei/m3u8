package com.mytool.m3u8.http.task;

import com.mytool.m3u8.http.interfaces.IDataListener;
import com.mytool.m3u8.http.interfaces.IHttpListener;
import com.mytool.m3u8.http.interfaces.IHttpService;

import java.util.concurrent.FutureTask;

/**
 * Created by wanghaofei on 2018/1/8.
 */

public class Volley {

    public static <T, M> void sendRequest(T requestInfo, String url, Class<M> response, IDataListener dataListener) {
        RequestHodler<T> requestHodler = new RequestHodler<>();
        requestHodler.setUrl(url);
        IHttpService httpService = new JsonHttpService();
        requestHodler.setHttpService(httpService);
        IHttpListener httpListener = new JsonDealLisener<>(response,dataListener);
        requestHodler.setHttpListener(httpListener);
        requestHodler.setRequestInfo(requestInfo);
        HttpTask<T> httpTask = new HttpTask<>(requestHodler);

        try {
            ThreadPoolManager.getInstance().execte(new FutureTask<Object>(httpTask,null));
        }catch (Exception e){
            dataListener.fail(e.getMessage());
        }
    }

}
