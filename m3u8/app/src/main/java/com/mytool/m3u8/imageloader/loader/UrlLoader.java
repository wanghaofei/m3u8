package com.mytool.m3u8.imageloader.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mytool.m3u8.imageloader.request.BitmapRequest;
import com.mytool.m3u8.imageloader.utils.BitmapDecoder;
import com.mytool.m3u8.imageloader.utils.ImageViewHelper;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wanghaofei on 2018/1/12.
 */

public class UrlLoader extends AbstractLoader {
    @Override
    protected Bitmap onLoad(BitmapRequest request) {
        HttpURLConnection connection = null;
        InputStream in = null;
        try {
            connection = (HttpURLConnection)new URL(request.getImageUri()).openConnection();
            //mark与reset支持重复使用流，但是InputStream不支持
            in = new BufferedInputStream(connection.getInputStream());
            //标记
            in.mark(in.available());
            final InputStream inputStream = in;
            //匿名内部类
            BitmapDecoder decoder = new BitmapDecoder(){
                @Override
                public Bitmap decodeBitmapWithOption(BitmapFactory.Options options) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                    if(options.inJustDecodeBounds){
                        try {
                            //重置
                            //则输入流总会在调用 mark 之后记住所有读取的字节，并且无论何时调用方法 reset ，都会准备再次提供那些相同的字节
                            //但是，如果在调用 reset 之前可以从流中读取多于 readlimit 的字节，则根本不需要该流记住任何数据。
                            inputStream.reset();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else{
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return bitmap;
                }
            };
            //如何获取ImageView的宽高？
            return decoder.decodeBitmap(ImageViewHelper.getImageViewWidth(request.getImageView()), ImageViewHelper.getImageViewHeight(request.getImageView()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(connection != null){
                connection.disconnect();
            }
            try {
                if(in != null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
