package com.mytool.m3u8.http.task;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSON;
import com.mytool.m3u8.http.interfaces.IDataListener;
import com.mytool.m3u8.http.interfaces.IHttpListener;

import org.apache.http.HttpEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by wanghaofei on 2018/1/8.
 */

public class JsonDealLisener<M> implements IHttpListener {

    private Class<M> responseClass;

    private IDataListener<M> dataListener;

    /**
     * 获取主线程的Handle
     * 通过handle切换至主线程
     */
    Handler handler = new Handler(Looper.getMainLooper());


    public JsonDealLisener(Class<M> responseClass, IDataListener<M> dataListener) {
        this.responseClass = responseClass;
        this.dataListener = dataListener;
    }

    @Override
    public void onSuccess(HttpEntity httpEntity) {

        InputStream inputStream = null;
        try {
            inputStream = httpEntity.getContent();
            String content = getContent(inputStream);
            final M response = JSON.parseObject(content,responseClass);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dataListener.onSuccess(response);
                }
            });
        }catch (Exception e){
            dataListener.fail(e.getMessage());
        }
    }

    @Override
    public void errorCode(int code) {
        dataListener.errorCode(code);
    }

    @Override
    public void fail(String msg) {

        dataListener.fail(msg);
    }

    @Override
    public void addHttpHeader(Map<String, String> headerMap) {

    }


    private String getContent(InputStream inputStream) {
        String content = null;
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;

            try {

                while ((line = reader.readLine()) != null) {

                    sb.append(line + "\n");

                }

            } catch (IOException e) {

                System.out.println("Error=" + e.toString());
                dataListener.fail(e.getMessage());
            } finally {

                try {

                    inputStream.close();

                } catch (IOException e) {

                    System.out.println("Error=" + e.toString());

                }

            }
            return sb.toString();

        } catch (Exception e) {

            e.printStackTrace();
            dataListener.fail(e.getMessage());
        }

        return content;
    }


}
