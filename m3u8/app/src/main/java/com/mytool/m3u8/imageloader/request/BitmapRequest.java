package com.mytool.m3u8.imageloader.request;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.mytool.m3u8.imageloader.config.DisplayConfig;
import com.mytool.m3u8.imageloader.core.SimpleImageLoader;
import com.mytool.m3u8.imageloader.policy.LoadPolicy;
import com.mytool.m3u8.imageloader.utils.MD5Utils;

import java.lang.ref.SoftReference;

/**
 * Created by wanghaofei on 2018/1/12.
 */

public class BitmapRequest implements Comparable<BitmapRequest> {
    //加载策略
    private LoadPolicy loadPolicy = SimpleImageLoader.getObject().getmConfig().getLoadPolicy();

    //序列号
    private int serialNO;

    //图片控件
    //当系统内存不足时，把引用的对象进行回收
    private SoftReference<ImageView> mimageViewRef;
    //图片路径
    private String imageUri;
    //MD5的图片路径
    private String imageUriMD5;

    private DisplayConfig displayConfig = SimpleImageLoader.getObject().getmConfig().getDisplayConfig();

    public SimpleImageLoader.ImageListener imageListener;

    public BitmapRequest(ImageView imageView, String uri, DisplayConfig config, SimpleImageLoader.ImageListener imageListener) {
        this.mimageViewRef = new SoftReference<ImageView>(imageView);
        //设置可见的ImageView的tag为，要下载的图片路径
        imageView.setTag(uri);
        this.imageUri = uri;
        this.imageUriMD5 = MD5Utils.toMD5(uri);
        if (config != null) {
            this.displayConfig = config;
        }
        this.imageListener = imageListener;
    }


    @Override
    public int compareTo(@NonNull BitmapRequest another) {
        return loadPolicy.compareTo(this, another);
    }

    public int getSerialNO() {
        return serialNO;
    }

    public void setSerialNO(int serialNO) {
        this.serialNO = serialNO;
    }

    public ImageView getImageView() {
        return mimageViewRef.get();
    }

    public String getImageUri() {
        return imageUri;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((loadPolicy == null) ? 0 : loadPolicy.hashCode());
        result = prime * result + serialNO;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BitmapRequest other = (BitmapRequest) obj;
        if (loadPolicy == null) {
            if (other.loadPolicy != null)
                return false;
        } else if (!loadPolicy.equals(other.loadPolicy))
            return false;
        if (serialNO != other.serialNO)
            return false;
        return true;
    }

    public String getImageUriMD5() {
        return imageUriMD5;
    }


}
