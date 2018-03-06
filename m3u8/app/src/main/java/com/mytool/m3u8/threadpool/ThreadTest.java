package com.mytool.m3u8.threadpool;

import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanghaofei on 2018/1/3.
 */

public class ThreadTest {

    public static void poolMethod() {
        //创建了一个线程数为3的固定线程数量的线程池，同理该线程池支持的线程最大并发数也是3
        // ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        //创建一个只有一个线程的线程池，每次只能执行一个线程任务，多余的任务会保存到一个任务队列中，等待线程处理完再依次处理任务队列中的任务
//        ExecutorService fixedThreadPool = Executors.newSingleThreadExecutor();
//        for (int i = 0; i <= 10; i++) {
//
//            final int index = i;
//            fixedThreadPool.execute(new Runnable() {
//                @Override
//                public void run() {
//                    String threadName = Thread.currentThread().getName();
//                    Log.e("zxy", "线程：" + threadName + ",正在执行第" + index + "个任务");
//                    try {
//                        Thread.sleep(2000);
//                    } catch (Exception e) {
//
//                    }
//
//                }
//            });
//        }

        //创建一个可以根据实际情况调整线程池中线程的数量的线程池
//        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
//
//        for (int i=0;i<=10;i++){
//            final int index = i;
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            cachedThreadPool.execute(new Runnable() {
//                @Override
//                public void run() {
//                    String threadName = Thread.currentThread().getName();
//                    Log.e("zxy", "线程：" + threadName + ",正在执行第" + index + "个任务");
//                    try {
//                        long time = index * 500;
//                        Thread.sleep(time);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }

        //创建一个可以定时或者周期性执行任务的线程池
//        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(3);
//        //延迟2秒后执行该任务
//        scheduledThreadPool.schedule(new Runnable() {
//            @Override
//            public void run() {
//                String threadName = Thread.currentThread().getName();
//                Log.e("zxy", "线程：" + threadName + ",正在执行任务");
//            }
//        }, 2, TimeUnit.SECONDS);
//        //延迟1秒后，每隔2秒执行一次该任务
//        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                String threadName = Thread.currentThread().getName();
//                Log.e("zxy", "线程：" + threadName + ",正在执行任务");
//            }
//        }, 1, 2, TimeUnit.SECONDS);



        //创建一个可以定时或者周期性执行任务的线程池，该线程池的线程数为1
//        ScheduledExecutorService singleThreadScheduledPool = Executors.newSingleThreadScheduledExecutor();
//        //延迟1秒后，每隔2秒执行一次该任务
//        singleThreadScheduledPool.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                String threadName = Thread.currentThread().getName();
//                Log.v("zxy", "线程：" + threadName + ",正在执行");
//            }
//        }, 1, 2, TimeUnit.SECONDS);






    }


}
