package com.mytool.m3u8.imageloader.loader;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.mytool.m3u8.imageloader.cache.BitmapCache;
import com.mytool.m3u8.imageloader.config.DisplayConfig;
import com.mytool.m3u8.imageloader.core.SimpleImageLoader;
import com.mytool.m3u8.imageloader.request.BitmapRequest;

/**
 * 抽象加载器
 * Created by wanghaofei on 2018/1/12.
 */

public abstract class AbstractLoader implements Loader {

    //缓存策略
    private BitmapCache mCache = SimpleImageLoader.getObject().getmConfig().getBitmapCache();

    //显示配置
    private DisplayConfig mDisplayConfig = SimpleImageLoader.getObject().getmConfig().getDisplayConfig();

    /**
     * 模板方法
     * @param request
     */
    @Override
    public void loadImage(BitmapRequest request) {

        //从缓存获取，缓存中没有再加载
        Bitmap bitmap = mCache.get(request);

        if(bitmap == null){
            //加载前显示的图片
            showLoadingImage(request);

            //加载完成，再缓存
            //具体的加载方式，由子类决定
            bitmap = onLoad(request);
            cacheBitmap(request,bitmap);
        }

        //显示
        deliveryToUIThread(request, bitmap);

    }

    protected void showLoadingImage(BitmapRequest request) {
        //
        if (hasLoadingPlaceHolder()) {
            final ImageView imageView = request.getImageView();
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageResource(mDisplayConfig.loadingImg);
                }
            });
        }
    }


    protected boolean hasLoadingPlaceHolder() {
        return (mDisplayConfig != null && mDisplayConfig.loadingImg > 0);
    }

    protected boolean hasFailedPlaceHolder() {
        return (mDisplayConfig != null && mDisplayConfig.failedImg > 0);
    }


    /**
     * 交给主线程显示
     *
     * @param request
     * @param bitmap
     */
    protected void deliveryToUIThread(final BitmapRequest request, final Bitmap bitmap) {
        ImageView imageView = request.getImageView();
        imageView.post(new Runnable() {
            @Override
            public void run() {
                updateImageView(request, bitmap);
            }
        });
    }

    private void updateImageView(final BitmapRequest request, final Bitmap bitmap) {

        ImageView imageView = request.getImageView();
        //加载正常
        if (bitmap != null && imageView.getTag().equals(request.getImageUri())) {
            imageView.setImageBitmap(bitmap);
        }

        //有可能加载失败
        if (bitmap == null && hasFailedPlaceHolder()) {
            imageView.setImageResource(mDisplayConfig.failedImg);
        }

        //监听
        //回调
        if (request.imageListener != null) {
            request.imageListener.onComplete(imageView, bitmap, request.getImageUri());
        }
    }


    /**
     * 缓存
     * @param request
     * @param bitmap
     */
    private void cacheBitmap(BitmapRequest request, Bitmap bitmap) {
        if(request != null && bitmap != null){
            synchronized (mCache) {
                mCache.put(request,bitmap);
            }
        }
    }

    /**
     * 具体的加载实现
     * @param request
     * @return
     */
    protected abstract Bitmap onLoad(BitmapRequest request);

}
