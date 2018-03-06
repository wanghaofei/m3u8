package com.mytool.m3u8.imageloader.loader;

import com.mytool.m3u8.imageloader.request.BitmapRequest;

/**
 * Created by wanghaofei on 2018/1/12.
 */

public interface Loader {

    /**
     * 加载图片
     * @param request
     */
    void loadImage(BitmapRequest request);

}
