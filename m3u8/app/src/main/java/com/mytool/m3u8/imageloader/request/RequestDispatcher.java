package com.mytool.m3u8.imageloader.request;

import android.util.Log;

import com.mytool.m3u8.imageloader.loader.Loader;
import com.mytool.m3u8.imageloader.loader.LoaderManager;

import java.util.concurrent.BlockingQueue;

/**
 * 请求转发线程，不断从请求队列中获取请求处理
 * Created by wanghaofei on 2018/1/12.
 */
public class RequestDispatcher extends Thread {

    //请求队列
    private BlockingQueue<BitmapRequest> mRequestQueue;

    public RequestDispatcher(BlockingQueue<BitmapRequest> mRequestQueue) {
        this.mRequestQueue = mRequestQueue;
    }

    @Override
    public void run() {
        //非阻塞状态，获取请求处理
        while (!isInterrupted()) {
            //从队列中获取优先级最高的请求进行处理
            try {
                BitmapRequest request = mRequestQueue.take();
                Log.e("jason", "---处理请求" + request.getSerialNO());
                //解析图片地址，获取对象的加载器
                String schema = parseSchema(request.getImageUri());
                Loader loader = LoaderManager.getInstance().getLoader(schema);
                loader.loadImage(request);
            } catch (Exception e) {

            }
        }
    }

    /**
     * 解析图片地址，获取schema
     *
     * @param imageUri
     * @return
     */
    private String parseSchema(String imageUri) {
        if (imageUri.contains("://")) {
            return imageUri.split("://")[0];
        } else {
            Log.e("jason", "图片地址schema异常！");
        }
        return null;
    }

}
