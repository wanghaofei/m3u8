package com.mytool.m3u8.imageloader.cache;

import android.graphics.Bitmap;

import com.mytool.m3u8.imageloader.request.BitmapRequest;

/**
 * Created by wanghaofei on 2018/1/12.
 */

public interface BitmapCache {

    /**
     * 缓存
     * @param request
     * @param bitmap
     */
    void put(BitmapRequest request, Bitmap bitmap);

    /**
     * 获取缓存
     * @param request
     * @return
     */
    Bitmap get(BitmapRequest request);

    /**
     * 移除缓存
     * @param request
     */
    void remove(BitmapRequest request);

}
