package com.mytool.m3u8.imageloader.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by wanghaofei on 2018/1/12.
 */

public abstract class BitmapDecoder {

    public abstract Bitmap decodeBitmapWithOption(BitmapFactory.Options options);

    /**
     * 压缩图片
     * @param reqWidth 指定要缩放后的宽度
     * @param reqHeight 指定要缩放后的高度
     * @return
     */
    public Bitmap decodeBitmap(int reqWidth,int reqHeight){
        //1.初始化Options
        BitmapFactory.Options options = new BitmapFactory.Options();
        //Bitmap对象不占用内存
        //只需要读取图片的宽高信息，无需将整张图片加载到内存中
        options.inJustDecodeBounds = true;
        //2.根据options加载Bitmap，图片的数据存储在options中（第一次读取图片的宽高）
        decodeBitmapWithOption(options);
        //3.计算图片缩放的比例
        calculateSampleSizeWithOption(options,reqWidth,reqHeight);
        //4.通过options设置的缩放比例记载图片（第二次根据缩放比例读取一个缩放后的图片）
        return decodeBitmapWithOption(options);
    }

    /**
     * 计算图片缩放的比例
     * @param options
     * @param reqWidth
     * @param reqHeight
     */
    private void calculateSampleSizeWithOption(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        //计算缩放的比例
        //图片的原始宽高
        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;
        if(width > reqWidth || height > reqHeight){
            //宽高的缩放比例
            int heightRatio = Math.round((float)height / (float)reqHeight);
            int widthRatio = Math.round((float)width / (float)reqWidth);

            //有的图是长图、有的是宽图
            inSampleSize = Math.max(heightRatio, widthRatio);
        }

        //全景图
        //当inSampleSize为2，图片的宽与高变成原来的1/2
        //options.inSampleSize = 2
        options.inSampleSize = inSampleSize;

        //每个像素2个字节
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        //Bitmap占用内存
        options.inJustDecodeBounds = false;
        //当系统内存不足时可以回收Bitmap
        options.inPurgeable = true;
        options.inInputShareable = true;


    }

}
