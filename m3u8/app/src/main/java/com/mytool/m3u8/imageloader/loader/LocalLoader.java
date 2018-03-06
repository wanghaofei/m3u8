package com.mytool.m3u8.imageloader.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.mytool.m3u8.imageloader.request.BitmapRequest;
import com.mytool.m3u8.imageloader.utils.BitmapDecoder;
import com.mytool.m3u8.imageloader.utils.ImageViewHelper;

import java.io.File;

/**
 * Created by wanghaofei on 2018/1/12.
 */

public class LocalLoader extends AbstractLoader {
    @Override
    protected Bitmap onLoad(BitmapRequest request) {
        final String path = Uri.parse(request.getImageUri()).getPath();
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }

        BitmapDecoder decoder = new BitmapDecoder() {
            @Override
            public Bitmap decodeBitmapWithOption(BitmapFactory.Options options) {
                return BitmapFactory.decodeFile(path, options);
            }
        };

        return decoder.decodeBitmap(ImageViewHelper.getImageViewWidth(request.getImageView()),
                ImageViewHelper.getImageViewHeight(request.getImageView()));
    }
}
