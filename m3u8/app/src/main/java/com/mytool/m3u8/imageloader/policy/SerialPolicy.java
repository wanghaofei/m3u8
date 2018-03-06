package com.mytool.m3u8.imageloader.policy;

import com.mytool.m3u8.imageloader.request.BitmapRequest;

/**
 * Created by wanghaofei on 2018/1/12.
 */

public class SerialPolicy implements LoadPolicy {

    @Override
    public int compareTo(BitmapRequest request1, BitmapRequest request2) {
        return request1.getSerialNO() - request2.getSerialNO();
    }

}
