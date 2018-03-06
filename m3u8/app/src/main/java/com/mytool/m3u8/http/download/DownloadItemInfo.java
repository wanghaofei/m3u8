package com.mytool.m3u8.http.download;

import com.mytool.m3u8.http.download.enums.DownloadStatus;
import com.mytool.m3u8.http.task.HttpTask;

/**
 * Created by wanghaofei on 2018/1/9.
 */

public class DownloadItemInfo extends BaseEntity<DownloadItemInfo> {

    public DownloadItemInfo(String url, String filePath) {
        this.url = url;
        this.filePath = filePath;
    }

    public DownloadItemInfo() {
    }

    private long currentLength;

    private long totalLength;


    private transient HttpTask httpTask;



    /**
     * 下载id
     */
    public Integer id;

    /**
     * 下载url
     */
    public String url;

    /**
     * 下载存储的文件路径
     */
    public String filePath;

    /**
     * 下载文件显示名
     */
    public String displayName;
    /**
     * 下载文件总大小
     */
    public Long totalLen;

    /**
     * 下载文件当前大小
     */
    public Long currentLen;

    /**
     * 下载开始时间
     */
    public String startTime;

    /**
     * 下载结束时间
     */
    public String finishTime;

    /**
     * 用户id
     */
    public String userId;

    /**
     * 下载任务类型
     */
    public String httpTaskType;

    /**
     * 下载优先级
     */
    public Integer priority;

    /**
     * 下载停止模式
     */
    public Integer stopMode;


    //下载的状态
    public Integer status;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Long getTotalLen() {
        return totalLen;
    }

    public void setTotalLen(Long totalLen) {
        this.totalLen = totalLen;
    }

    public Long getCurrentLen() {
        return currentLen;
    }

    public void setCurrentLen(Long currentLen) {
        this.currentLen = currentLen;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHttpTaskType() {
        return httpTaskType;
    }

    public void setHttpTaskType(String httpTaskType) {
        this.httpTaskType = httpTaskType;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getStopMode() {
        return stopMode;
    }

    public void setStopMode(Integer stopMode) {
        this.stopMode = stopMode;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public long getCurrentLength() {
        return currentLength;
    }

    public void setCurrentLength(long currentLength) {
        this.currentLength = currentLength;
    }

    public long getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(long totalLength) {
        this.totalLength = totalLength;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public HttpTask getHttpTask() {
        return httpTask;
    }

    public void setHttpTask(HttpTask httpTask) {
        this.httpTask = httpTask;
    }

    public Integer getStatus() {
        return status;
    }
}
