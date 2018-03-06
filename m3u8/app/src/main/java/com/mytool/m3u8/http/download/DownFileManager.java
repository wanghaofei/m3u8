package com.mytool.m3u8.http.download;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.mytool.m3u8.dao.db.BaseDaoFactory;
import com.mytool.m3u8.http.download.dao.DownLoadDao;
import com.mytool.m3u8.http.download.enums.DownloadStatus;
import com.mytool.m3u8.http.download.enums.DownloadStopMode;
import com.mytool.m3u8.http.download.enums.Priority;
import com.mytool.m3u8.http.download.interfaces.IDownloadCallable;
import com.mytool.m3u8.http.download.interfaces.IDownloadServiceCallable;
import com.mytool.m3u8.http.interfaces.IHttpListener;
import com.mytool.m3u8.http.interfaces.IHttpService;
import com.mytool.m3u8.http.task.HttpTask;
import com.mytool.m3u8.http.task.RequestHodler;
import com.mytool.m3u8.http.task.ThreadPoolManager;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.FutureTask;

/**
 * Created by wanghaofei on 2018/1/9.
 */

public class DownFileManager implements IDownloadServiceCallable {

    //    private  static
    private byte[] lock = new byte[0];

    DownLoadDao downLoadDao = BaseDaoFactory.getInstance().getDataHelper(DownLoadDao.class, DownloadItemInfo.class);
    java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    /**
     * 观察者模式
     */
    private final List<IDownloadCallable> applisteners = new CopyOnWriteArrayList<IDownloadCallable>();

    /**
     * 怎在下载的所有任务
     */
    private static List<DownloadItemInfo> downloadFileTaskList = new CopyOnWriteArrayList();

    Handler handler = new Handler(Looper.getMainLooper());


    public int download(String url) {
        String[] preFix = url.split("/");
        return this.download(url, Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + preFix[preFix.length - 1]);
    }

    public int download(String url, String filePath) {
        String[] preFix = url.split("/");
        String displayName = preFix[preFix.length - 1];
        return this.download(url, filePath, displayName);
    }

    public int download(String url, String filePath, String displayName) {
        return this.download(url, filePath, displayName, Priority.middle);
    }

    public int download(String url, String filePath,
                        String displayName, Priority priority) {
        if (priority == null) {
            priority = Priority.low;
        }

        File file = new File(filePath);
        DownloadItemInfo downloadItemInfo = null;

        downloadItemInfo = downLoadDao.findRecord(url, filePath);

        //没下载
        if (downloadItemInfo == null) {
            /**
             * 根据文件路径查找
             */
            List<DownloadItemInfo> samesFile = downLoadDao.findRecord(filePath);

            /**
             * 大于0  表示下载
             */
            if (samesFile.size() > 0) {
                DownloadItemInfo sameDown = samesFile.get(0);
                if (sameDown.getCurrentLen() == sameDown.getTotalLen()) {
                    synchronized (applisteners) {
                        for (IDownloadCallable downloadCallable : applisteners) {
                            downloadCallable.onDownloadError(sameDown.getId(), 2, "文件已经下载了");
                        }
                    }
                }
            }

            /**
             * 插入数据库
             */
            downloadItemInfo = downLoadDao.addRecrod(url, filePath, displayName, priority.getValue());
            if (downloadItemInfo != null) {
                synchronized (applisteners) {
                    for (IDownloadCallable downloadCallable : applisteners) {
                        //通知应用层  数据库被添加了
                        downloadCallable.onDownloadInfoAdd(downloadItemInfo.getId());
                    }
                }
            }

            downloadItemInfo = downLoadDao.findRecord(url, filePath);

            if (isDowning(file.getAbsolutePath())) {
                synchronized (applisteners) {
                    for (IDownloadCallable downloadCallable : applisteners) {
                        downloadCallable.onDownloadError(downloadItemInfo.getId(), 4, "正在下载，请不要重复添加");
                    }
                }
                return downloadItemInfo.getId();
            }


            if (downloadItemInfo != null) {
                downloadItemInfo.setPriority(priority.getValue());
                //判断数据库存的 状态是否是完成
                if (downloadItemInfo.getStatus() != DownloadStatus.finish.getValue()) {
                    if (downloadItemInfo.getTotalLen() == 0L || file.length() == 0L) {
                        Log.e("zxy", "还未开始下载");
                        downloadItemInfo.setStatus(DownloadStatus.failed.getValue());
                    }
                    //判断数据库中 总长度是否等于文件长度
                    if (downloadItemInfo.getTotalLen() == file.length() && downloadItemInfo.getTotalLen() != 0) {
                        downloadItemInfo.setStatus(DownloadStatus.finish.getValue());
                        synchronized (applisteners) {
                            for (IDownloadCallable downloadCallable : applisteners) {
                                try {
                                    downloadCallable.onDownloadError(downloadItemInfo.getId(), 4, "已经下载了");
                                } catch (Exception e) {
                                }
                            }
                        }
                    }
                }
                /**
                 * 更新
                 */
                downLoadDao.updateRecord(downloadItemInfo);
            }

            /**
             * 判断是否已经下载完成
             */
            if (downloadItemInfo.getStatus() == DownloadStatus.finish.getValue()) {
                Log.e("zxy", "已经下载完成  回调应用层");
                final int downId = downloadItemInfo.getId();
                synchronized (applisteners) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            for (IDownloadCallable downloadCallable : applisteners) {
                                downloadCallable.onDownloadStatusChanged(downId, DownloadStatus.finish);
                            }
                        }
                    });
                }
                downLoadDao.removeRecordFromMemery(downId);
                return downloadItemInfo.getId();
            }//之前的下载 状态为暂停状态

            List<DownloadItemInfo> allDowning = downloadFileTaskList;
            //当前下载不是最高级  则先退出下载
            if (priority != Priority.high) {
                for (DownloadItemInfo downling : allDowning) {
                    //从下载表中  获取到全部正在下载的任务
                    downling = downLoadDao.findSigleRecord(downling.getFilePath());

                    if (downloadItemInfo != null && downloadItemInfo.getPriority() == Priority.high.getValue()) {
                        if (downloadItemInfo.getFilePath().equals(downling.getFilePath())) {
                            return downloadItemInfo.getId();
                        }
                    }
                }
            }

            DownloadItemInfo addDownloadInfo = reallyDown(downloadItemInfo);
            if (priority == Priority.high || priority == Priority.middle) {
                synchronized (allDowning) {
                    for (DownloadItemInfo downloadItemInfo1 : allDowning) {
                        if (!downloadItemInfo.getFilePath().equals(downloadItemInfo1.getFilePath())) {
                            DownloadItemInfo downingInfo = downLoadDao.findSigleRecord(downloadItemInfo1.getFilePath());
                            if (downingInfo != null) {
                                pause(downloadItemInfo.getId(), DownloadStopMode.auto);
                            }
                        }
                    }
                }
                return downloadItemInfo.getId();
            }
            return -1;

        }

        return -1;
    }


    /**
     * 停止
     * @param downloadId
     * @param mode
     */
    public void pause(int downloadId, DownloadStopMode mode)
    {
        if (mode == null)
        {
            mode = DownloadStopMode.auto;
        }
        final DownloadItemInfo downloadInfo =downLoadDao.findRecordById(downloadId);
        if (downloadInfo != null)
        {
            // 更新停止状态
            if (downloadInfo != null)
            {
                downloadInfo.setStopMode(mode.getValue());
                downloadInfo.setStatus(DownloadStatus.pause.getValue());
                downLoadDao.updateRecord(downloadInfo);
            }
            for (DownloadItemInfo downing:downloadFileTaskList)
            {
                if(downloadId==downing.getId())
                {
                    downing.getHttpTask().pause();
                }
            }
        }
    }


    /**
     * 判断当前是否正在下载
     *
     * @param absolutePath
     * @return
     */

    private boolean isDowning(String absolutePath) {
        for (DownloadItemInfo downloadItemInfo : downloadFileTaskList) {
            if (downloadItemInfo.getFilePath().equals(absolutePath)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 添加观察者
     * @param downloadCallable
     */
    public void setDownCallable(IDownloadCallable downloadCallable)
    {
        synchronized (applisteners)
        {
            applisteners.add(downloadCallable);
        }

    }

    /**
     * 下载
     * @param url
     * MainAcitivity
     * 1
     *
     * 2
     *
     */
    /**
     * 下载
     */
    public DownloadItemInfo reallyDown(DownloadItemInfo downloadItemInfo)
    {
        synchronized (lock)
        {
            //实例化DownloadItem
            RequestHodler requestHodler=new RequestHodler();
            //设置请求下载的策略
            IHttpService httpService=new FileDownHttpService();
            //得到请求头的参数 map
            Map<String,String> map=httpService.getHttpHeadMap();
            /**
             * 处理结果的策略
             */
            IHttpListener httpListener=new DownLoadLitener(downloadItemInfo,this,httpService);

            requestHodler.setHttpListener(httpListener);
            requestHodler.setHttpService(httpService);
            /**
             *  bug  url
             */
            requestHodler.setUrl(downloadItemInfo.getUrl());

            HttpTask httpTask=new HttpTask(requestHodler);
            downloadItemInfo.setHttpTask(httpTask);

            /**
             * 添加
             */
            downloadFileTaskList.add(downloadItemInfo);
            httpTask.start();

        }

        return downloadItemInfo;

    }






//    public void down(String url) {
//        synchronized (lock) {
//            String[] preFixs = url.split("/");
//            String afterFix = preFixs[preFixs.length - 1];
//
//            File file = new File(Environment.getExternalStorageDirectory(), afterFix);
//
//            Log.e("zxy", "path=" + file.getAbsolutePath());
//
//            //实例化DownloadItem
//            DownloadItemInfo downloadItemInfo = new DownloadItemInfo(url, file.getAbsolutePath());
//
//            RequestHodler requestHodler = new RequestHodler();
//            IHttpService iHttpService = new FileDownHttpService();
//            //得到请求头的参数 map
//            Map<String, String> map = iHttpService.getHttpHeadMap();
///**
// * 处理结果的策略
// */
//            IHttpListener httpListener = new DownLoadLitener(downloadItemInfo, this, iHttpService);
//            requestHodler.setUrl(url);
//            requestHodler.setHttpListener(httpListener);
//            requestHodler.setHttpService(iHttpService);
//
//            HttpTask httpTask = new HttpTask(requestHodler);
//            try {
//                ThreadPoolManager.getInstance().execte(new FutureTask<Object>(httpTask, null));
//            } catch (InterruptedException e) {
//
//            }
//
//
//        }
//    }


    @Override
    public void onDownloadStatusChanged(DownloadItemInfo downloadItemInfo) {

    }

    @Override
    public void onTotalLengthReceived(DownloadItemInfo downloadItemInfo) {

    }

    @Override
    public void onCurrentSizeChanged(DownloadItemInfo downloadItemInfo, double downLenth, long speed) {
        Log.e("zxy", "下载速度：" + speed / 1000 + "k/s");
        Log.e("zxy", "-----路径  " + downloadItemInfo.getFilePath() + "  下载长度  " + downLenth + "   速度  " + speed);
    }

    @Override
    public void onDownloadSuccess(DownloadItemInfo downloadItemInfo) {
        Log.e("zxy", "下载成功    路劲  " + downloadItemInfo.getFilePath() + "  url " + downloadItemInfo.getUrl());
    }

    @Override
    public void onDownloadPause(DownloadItemInfo downloadItemInfo) {

    }

    @Override
    public void onDownloadError(DownloadItemInfo downloadItemInfo, int var2, String var3) {

    }
}
