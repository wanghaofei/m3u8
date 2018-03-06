package com.mytool.m3u8.http.download.dao;

import android.database.Cursor;

import com.mytool.m3u8.dao.db.BaseDao;
import com.mytool.m3u8.http.download.DownloadItemInfo;
import com.mytool.m3u8.http.download.enums.DownloadStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by wanghaofei on 2018/1/10.
 */
public class DownLoadDao extends BaseDao<DownloadItemInfo> {

    /**
     * 保存应该下载的集合
     * 不包括已经下载成功的
     */
    private List<DownloadItemInfo> downloadItemInfoList = Collections.synchronizedList(new ArrayList<DownloadItemInfo>());

    private DownloadInfoComparator downloadInfoComparator=new DownloadInfoComparator();


    @Override
    protected String createTable() {
        return "create table if not exists  t_downloadInfo(" + "id Integer primary key, " + "url TEXT not null," + "filePath TEXT not null, "
                + "displayName TEXT, " + "status Integer, " + "totalLen Long, " + "currentLen Long," + "startTime TEXT," + "finishTime TEXT," + "userId TEXT, "
                + "httpTaskType TEXT," + "priority  Integer," + "stopMode Integer," + "downloadMaxSizeKey TEXT," + "unique(filePath))";
    }


    @Override
    public List<DownloadItemInfo> query(String sql) {
        return super.query(sql);
    }


    /**
     * 生成下载id
     *
     * @return 返回下载id
     */
    private Integer generateRecordId() {

        int maxId = 0;
        String sql = "select max(id) from " + getTableName();

        synchronized (DownLoadDao.class) {

            Cursor cursor = this.database.rawQuery(sql, null);
            if (cursor.moveToNext()) {

                String[] colmName = cursor.getColumnNames();

                int index = cursor.getColumnIndex("max(id)");
                if (index != -1) {
                    Object value = cursor.getInt(index);
                    if (value != null) {
                        maxId = Integer.parseInt(String.valueOf(value));
                    }
                }
            }

        }
        return maxId + 1;
    }


    /**
     * 根据下载地址和下载文件路径查找下载记录
     *
     * @param url      下载地址
     * @param filePath 下载文件路径
     * @return
     */
    public DownloadItemInfo findRecord(String url, String filePath) {

        synchronized (DownLoadDao.class) {

            for (DownloadItemInfo record : downloadItemInfoList) {
                if (record.getUrl().equals(url) && record.getFilePath().equals(filePath)) {
                    return record;
                }
            }

            /**
             * 内存集合找不到
             * 就从数据库中查找
             */
            DownloadItemInfo where = new DownloadItemInfo();
            where.setUrl(url);
            where.setFilePath(filePath);

            List<DownloadItemInfo> resultList = super.query(where);

            if (resultList.size() > 0) {
                return resultList.get(0);
            }
            return null;
        }
    }


    /**
     * 根据 下载文件路径查找下载记录
     * <p>
     * 下载地址
     *
     * @param filePath 下载文件路径
     * @return
     */
    public List<DownloadItemInfo> findRecord(String filePath) {
        synchronized (DownLoadDao.class) {
            DownloadItemInfo where = new DownloadItemInfo();
            where.setFilePath(filePath);
            List<DownloadItemInfo> resultList = super.query(where);
            return resultList;
        }

    }


    /**
     * 添加下载记录
     *
     * @param url         下载地址
     * @param filePath    下载文件路径
     * @param displayName 文件显示名
     * @param priority    小组优先级
     *                    TODO
     * @return 下载id
     */

    public DownloadItemInfo addRecrod(String url, String filePath, String displayName, int priority) {
        synchronized (DownLoadDao.class) {
            DownloadItemInfo existDownloadInfo = findRecord(url, filePath);
            if (existDownloadInfo == null) {
                DownloadItemInfo record = new DownloadItemInfo();
                record.setId(generateRecordId());
                record.setUrl(url);
                record.setFilePath(filePath);
                record.setDisplayName(displayName);
                record.setStatus(DownloadStatus.waitting.getValue());
                record.setTotalLen(0L);
                record.setCurrentLen(0L);
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                record.setStartTime(dateFormat.format(new Date()));
                record.setFinishTime("0");
                record.setPriority(priority);
                super.insert(record);
                downloadItemInfoList.add(record);
                return record;
            }
        }
        return null;
    }


    /**
     * 更新下载记录
     *
     * @param record 下载记录
     * @return
     */
    public int updateRecord(DownloadItemInfo record) {
        DownloadItemInfo where = new DownloadItemInfo();
        where.setId(record.getId());
        int result = 0;
        synchronized (DownLoadDao.class) {
            try {
                result = super.update(record, where);
            } catch (Throwable e) {
            }

            if (result > 0) {
                for (int i = 0; i < downloadItemInfoList.size(); i++) {
                    if (downloadItemInfoList.get(i).getId().intValue() == record.getId()) {
                        downloadItemInfoList.set(i, record);
                        break;
                    }
                }
            }
        }
        return result;
    }


    /**
     * 根据下载地址和下载文件路径查找下载记录
     * <p>
     * 下载地址
     *
     * @param filePath 下载文件路径
     * @return
     */
    public DownloadItemInfo findSigleRecord(String filePath) {
        List<DownloadItemInfo> downloadInfoList = findRecord(filePath);
        if (downloadInfoList.isEmpty()) {
            return null;
        }
        return downloadInfoList.get(0);
    }


    /**
     * 根据id查找下载记录对象
     *
     * @param recordId
     * @return
     */
    public DownloadItemInfo findRecordById(int recordId) {
        synchronized (DownLoadDao.class) {
            for (DownloadItemInfo record : downloadItemInfoList) {
                if (record.getId() == recordId) {
                    return record;
                }
            }

            DownloadItemInfo where = new DownloadItemInfo();
            where.setId(recordId);
            List<DownloadItemInfo> resultList = super.query(where);
            if (resultList.size() > 0) {
                return resultList.get(0);
            }
            return null;
        }

    }


    /**
     * 根据id从内存中移除下载记录
     *
     * @param id 下载id
     * @return true标示删除成功，否则false
     */
    public boolean removeRecordFromMemery(int id) {
        synchronized (DownloadItemInfo.class) {
            for (int i = 0; i < downloadItemInfoList.size(); i++) {
                if (downloadItemInfoList.get(i).getId() == id) {
                    downloadItemInfoList.remove(i);
                    break;
                }
            }
            return true;
        }
    }


    /**
     * 比较器
     */
    class DownloadInfoComparator implements Comparator<DownloadItemInfo> {
        @Override
        public int compare(DownloadItemInfo lhs, DownloadItemInfo rhs) {
            return rhs.getId() - lhs.getId();
        }
    }


}
