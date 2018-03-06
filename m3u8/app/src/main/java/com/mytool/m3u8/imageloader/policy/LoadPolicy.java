package com.mytool.m3u8.imageloader.policy;

import com.mytool.m3u8.imageloader.request.BitmapRequest;

/**
 * 加载策略
 * Created by wanghaofei on 2018/1/12.
 */

public interface LoadPolicy {

    /**
     * 两个BitmapRequest进行比较
     * @param request1
     * @param request2
     * @return 小于0，request1 < request2，大于0，request1 > request2，等于
     */
    public int compareTo(BitmapRequest request1, BitmapRequest request2);

}
