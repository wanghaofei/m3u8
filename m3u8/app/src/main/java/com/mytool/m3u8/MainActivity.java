package com.mytool.m3u8;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mytool.m3u8.dao.User;
import com.mytool.m3u8.dao.UserDao;
import com.mytool.m3u8.dao.db.BaseDaoFactory;
import com.mytool.m3u8.dao.db.IBaseDao;
import com.mytool.m3u8.http.TestDownActivity;
import com.mytool.m3u8.http.download.DownFileManager;
import com.mytool.m3u8.http.interfaces.IDataListener;
import com.mytool.m3u8.http.task.Volley;
import com.mytool.m3u8.imageloader.config.ImageLoaderConfig;
import com.mytool.m3u8.imageloader.core.SimpleImageLoader;
import com.mytool.m3u8.imageloader.policy.SerialPolicy;
import com.mytool.m3u8.threadpool.ThreadTest;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * 直播地址
     * https://www.cnblogs.com/tangyuanby2/p/5806196.html
     */

    private String SD_CARD_PATH;
    private static final String BASE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView)this.findViewById(R.id.one_view);

        SD_CARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        // Example of a call to a native method

//        startService(new Intent(MainActivity.this,WebService.class));

        TextView tv = (TextView) findViewById(R.id.sample_text);
//        tv.setText(testJNI());

//        ThreadTest.poolMethod();

        Log.e("dingpa", SD_CARD_PATH);

//        IBaseDao<User> baseDao = BaseDaoFactory.getInstance().getDataHelper(UserDao.class,User.class);
//
//        for (int i=0;i<20;i++)
//        {
//            User user=new User(i,"teacher"+i,"123456"+i);
//            baseDao.insert(user);
//        }



    }







    public void begin(View view) {

        downM3u8File();

    }

    public void play(View view) {

        startActivity(new Intent(MainActivity.this, PlayVodActivity.class));


    }


    private void downM3u8File() {

        String path = "http://3811.liveplay.myqcloud.com/live/m3u8/3811_channel509.m3u8?AUTH=t2VQwMQRwZeYUAgP4p4PD5KbuI4mwIGzVnUJLeBSwH0fLswHdoHOSHE3IudSe5voFDEmVg3SCJVO1lezDenaPQ==";

        String newPath = "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8";

        String savePath = BASE_PATH + "mmmuu8" + File.separator + "test" + File.separator + "bbb.m3u8";

        RequestParams requestParams = new RequestParams(newPath);
        requestParams.setSaveFilePath(savePath);


        x.http().get(requestParams, new Callback.CommonCallback<File>() {
            @Override
            public void onSuccess(File result) {

                Log.e("three", result.getPath());
                List<String> tsList = M3u8ParserUtils
                        .getM3u8ToFlag(result.getPath(), ".ts");

                String saveFilePath = BASE_PATH + "mmmuu8" + File.separator + "test";

                for (String sss : tsList) {
                    Log.e("three", "sss=" + sss);
                    downTs(sss, saveFilePath);
                }
                Toast.makeText(MainActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("three", ex.getMessage());


            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    private void downTs(String tsPath, String fileName) {

        String tsLocal = fileName + File.separator + tsPath;
        String downPath = "http://ivi.bupt.edu.cn/hls/" + tsPath;
        RequestParams requestParams = new RequestParams(downPath);
        requestParams.setSaveFilePath(tsLocal);


        x.http().get(requestParams, new Callback.CommonCallback<File>() {
            @Override
            public void onSuccess(File result) {

                Log.e("three", result.getPath());

                Toast.makeText(MainActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("three", ex.getMessage());


            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }

}
