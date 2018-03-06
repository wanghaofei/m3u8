package com.mytool.m3u8;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.VideoView;

import java.io.File;

/**
 * Created by wanghaofei on 2017/12/28.
 */

public class PlayVodActivity extends AppCompatActivity {

    private VideoView videoView;
    private static final String BASE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play);

//        String videoPath = BASE_PATH+"DCIM"+File.separator+"Camera"+File.separator+"VID_20171228_161911.mp4";
        String videoPath = BASE_PATH+"mmmuu8"+File.separator+"cntv1514444359216"+File.separator+"cctv1hd-52571.ts";


        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("http://"
                + M3u8ServerUtils.getLocalIpAddressIpv4() + ":"
                + WebService.PORT);
        String playUrl = sBuffer.toString()+BASE_PATH +"mmmuu8"+File.separator+"test"+File.separator+"bbb.m3u8";

        Log.e("three","playUrl="+playUrl);

        videoView =(VideoView)this.findViewById(R.id.video_view);
        videoView.setVideoPath(playUrl);
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

            }
        });
    }
}
