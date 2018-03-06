package com.mytool.m3u8.http.download.interfaces;

import com.mytool.m3u8.http.download.DownloadItemInfo;

/**
 * Created by wanghaofei on 2018/1/9.
 */

public interface IDownloadServiceCallable {


    void onDownloadStatusChanged(DownloadItemInfo downloadItemInfo);

    void onTotalLengthReceived(DownloadItemInfo downloadItemInfo);

    void onCurrentSizeChanged(DownloadItemInfo downloadItemInfo, double downLenth, long speed);

    void onDownloadSuccess(DownloadItemInfo downloadItemInfo);

    void onDownloadPause(DownloadItemInfo downloadItemInfo);

    void onDownloadError(DownloadItemInfo downloadItemInfo, int var2, String var3);

}
