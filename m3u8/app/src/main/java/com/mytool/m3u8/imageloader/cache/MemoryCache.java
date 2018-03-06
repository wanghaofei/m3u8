package com.mytool.m3u8.imageloader.cache;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.mytool.m3u8.imageloader.request.BitmapRequest;

/**
 * Created by wanghaofei on 2018/1/12.
 */

public class MemoryCache implements BitmapCache {

    private LruCache<String, Bitmap> mLruCache;


    public MemoryCache() {
        //缓存的最大值（可用内存的1/8）
        int maxSize = (int)Runtime.getRuntime().freeMemory() / 1024 / 8;
        mLruCache = new LruCache<String, Bitmap>(maxSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //一个Bitmap的大小
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        mLruCache.put(request.getImageUriMD5(), bitmap);
        Log.e("jason", "put in MemoryCache:"+request.getImageUri());
    }

    @Override
    public Bitmap get(BitmapRequest request) {
        Log.e("jason", "get from MemoryCache:"+request.getImageUri());
        return mLruCache.get(request.getImageUriMD5());
    }

    @Override
    public void remove(BitmapRequest request) {
        mLruCache.remove(request.getImageUriMD5());
    }






}
