package com.mytool.m3u8.imageloader.core;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.mytool.m3u8.imageloader.config.DisplayConfig;
import com.mytool.m3u8.imageloader.config.ImageLoaderConfig;
import com.mytool.m3u8.imageloader.request.BitmapRequest;
import com.mytool.m3u8.imageloader.request.RequestQueue;

/**
 * Created by wanghaofei on 2018/1/12.
 */

public class SimpleImageLoader {

    private ImageLoaderConfig mConfig;

    private RequestQueue mRequestQueue;

    private static SimpleImageLoader mInstance;

    private SimpleImageLoader() {

    }

    private SimpleImageLoader(ImageLoaderConfig config) {
        this.mConfig = config;
        //初始化请求队列
        mRequestQueue = new RequestQueue(config.getThreadCount());
        //开始，请求转发线程开始不断从队列中获取请求，进行转发处理
        mRequestQueue.start();
    }

    public static SimpleImageLoader getInstance(ImageLoaderConfig config) {
        if (mInstance == null) {
            synchronized (SimpleImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new SimpleImageLoader(config);
                }
            }
        }
        return mInstance;
    }


    /**
     * 获取SimpleImageLoader的实例
     *
     * @return 只有在SimpleImageLoader getInstance(ImageLoaderConfig config)调用过之后，才能返回一个实例化了的SimpleImageLoader对象
     */

    public static SimpleImageLoader getObject() {
        if (mInstance == null) {
            throw new UnsupportedOperationException("getInstance(ImageLoaderConfig config) 没有执行过！");
        }
        return mInstance;
    }

    public void displayImage(ImageView imageView, String uri) {

        displayImage(imageView,uri,null,null);
    }

    /**
     * 显示图片
     * @param imageView
     * @param uri
     * @param config
     * @param imageListener
     */
    public void displayImage(ImageView imageView, String uri,
                             DisplayConfig config, ImageListener imageListener){
        //生成一个请求，添加到请求队列中
        BitmapRequest request = new BitmapRequest(imageView,uri,config,imageListener);
        mRequestQueue.addRequest(request);

    }


    /**
     * 图片加载的监听
     */
    public static interface ImageListener{
        /**
         * 加载完成
         * @param imageView
         * @param bitmap
         * @param uri
         */
        void onComplete(ImageView imageView, Bitmap bitmap,String uri);
    }

    public ImageLoaderConfig getmConfig() {
        return mConfig;
    }
}
