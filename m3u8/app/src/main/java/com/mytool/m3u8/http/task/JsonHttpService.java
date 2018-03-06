package com.mytool.m3u8.http.task;

import com.mytool.m3u8.http.interfaces.IHttpListener;
import com.mytool.m3u8.http.interfaces.IHttpService;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.Map;

/**
 * Created by wanghaofei on 2018/1/8.
 */

public class JsonHttpService implements IHttpService {

    private IHttpListener httpListener;

    private HttpClient httpClient=new DefaultHttpClient();
    private HttpPost httpPost;
    private String url;

    private HttpGet httpGet;

    private byte[] requestData;

    /**
     * httpClient获取网络的回调
     */
    private HttpRespnceHandler httpRespnceHandler = new HttpRespnceHandler();





    @Override
    public void setUrl(String url) {

        this.url = url;
    }

    @Override
    public void excute() {

//        httpGet = new HttpGet(url);
        httpPost = new HttpPost(url);
        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(requestData);
        httpPost.setEntity(byteArrayEntity);
        try {
            httpClient.execute(httpPost,httpRespnceHandler);
        }catch (Exception e){
            e.printStackTrace();
            httpListener.fail("请求时报错");
        }

    }

    @Override
    public void setHttpListener(IHttpListener httpListener) {

        this.httpListener = httpListener;
    }

    @Override
    public void setRequestData(byte[] requestData) {

        this.requestData = requestData;
    }

    @Override
    public void pause() {

    }

    @Override
    public Map<String, String> getHttpHeadMap() {
        return null;
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
        return false;
    }


    private class HttpRespnceHandler extends BasicResponseHandler{
        @Override
        public String handleResponse(HttpResponse response) throws IOException {

            //响应码
            int code = response.getStatusLine().getStatusCode();
            if(code == 200){
                //成功
                httpListener.onSuccess(response.getEntity());
            }else {
                //错误
                httpListener.errorCode(code);
            }
            return null;
        }
    }


}
