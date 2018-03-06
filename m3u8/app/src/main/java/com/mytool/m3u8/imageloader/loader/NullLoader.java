package com.mytool.m3u8.imageloader.loader;

import android.graphics.Bitmap;
import android.util.Log;

import com.mytool.m3u8.imageloader.request.BitmapRequest;

/**
 * Created by wanghaofei on 2018/1/12.
 */

public class NullLoader extends AbstractLoader {

    @Override
    protected Bitmap onLoad(BitmapRequest request) {
        Log.e("jason", "图片无法记载!");
        return null;
    }

}
