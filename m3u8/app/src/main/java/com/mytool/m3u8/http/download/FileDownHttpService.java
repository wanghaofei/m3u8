package com.mytool.m3u8.http.download;

import android.util.Log;

import com.mytool.m3u8.http.interfaces.IHttpListener;
import com.mytool.m3u8.http.interfaces.IHttpService;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by wanghaofei on 2018/1/9.
 */

public class FileDownHttpService implements IHttpService {


    /**
     * 含有请求处理的 接口
     */
    private IHttpListener httpListener;

    private HttpClient httpClient=new DefaultHttpClient();
    private HttpGet httpPost;
    private String url;

    private byte[] requestDate;

    /**
     * 即将添加到请求头的信息
     */
    private Map<String ,String> headerMap= Collections.synchronizedMap(new HashMap<String ,String>());


    /**
     * httpClient获取网络的回调
     */
    private  HttpRespnceHandler httpRespnceHandler=new HttpRespnceHandler();

    /**
     * 增加方法
     */
    private AtomicBoolean pause=new AtomicBoolean(false);

    @Override
    public void setUrl(String url) {

        this.url = url;
    }

    @Override
    public void excute() {
        httpPost=new HttpGet(url);
        constrcutHeader();
        try {
            httpClient.execute(httpPost,httpRespnceHandler);
        } catch (IOException e) {
            httpListener.fail(e.getMessage());
        }
    }


    private void constrcutHeader() {
        Iterator iterator=headerMap.keySet().iterator();
        while (iterator.hasNext())
        {
            String key= (String) iterator.next();
            String value=headerMap.get(key);
            Log.e("zxy"," 请求头信息  "+key+"  value "+value);
            httpPost.addHeader(key,value);
        }
    }


    @Override
    public void setHttpListener(IHttpListener httpListener) {

        this.httpListener = httpListener;
    }


    @Override
    public void setRequestData(byte[] requestData) {

        this.requestDate = requestData;
    }

    @Override
    public void pause() {

        pause.compareAndSet(false,true);
    }

    @Override
    public Map<String, String> getHttpHeadMap() {
        return headerMap;
    }

    @Override
    public boolean cancle() {
        return false;
    }

    @Override
    public boolean isCancle() {
        return false;
    }

    @Override
    public boolean isPause() {
        return pause.get();
    }


    private class HttpRespnceHandler extends BasicResponseHandler
    {
        @Override
        public String handleResponse(HttpResponse response) throws ClientProtocolException {
            //响应吗
            int code=response.getStatusLine().getStatusCode();
            if(code==200 || code == 206)
            {
                httpListener.onSuccess(response.getEntity());
            }else
            {
                httpListener.errorCode(code);
            }


            return null;
        }
    }

}
