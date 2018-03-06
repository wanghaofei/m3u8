package com.mytool.m3u8.imageloader.cache;

import android.graphics.Bitmap;

import com.mytool.m3u8.imageloader.request.BitmapRequest;

/**
 * Created by wanghaofei on 2018/1/12.
 */

public class NoCache implements BitmapCache {

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        // TODO Auto-generated method stub

    }

    @Override
    public Bitmap get(BitmapRequest request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void remove(BitmapRequest request) {
        // TODO Auto-generated method stub

    }

}
