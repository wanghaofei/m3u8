package com.mytool.m3u8.http.download.interfaces;

import com.mytool.m3u8.http.interfaces.IHttpListener;
import com.mytool.m3u8.http.interfaces.IHttpService;

/**
 * Created by wanghaofei on 2018/1/9.
 */

public interface IDownLitener extends IHttpListener {

    void setHttpServive(IHttpService httpServive);


    void  setCancleCalle();


    void  setPuaseCallble();

}
