package com.mytool.m3u8.http.task;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanghaofei on 2018/1/5.
 */

public class ThreadPoolManager {

    private static ThreadPoolManager instance = new ThreadPoolManager();

    private LinkedBlockingDeque<Future<?>> taskQuene = new LinkedBlockingDeque<>();

    private ThreadPoolExecutor threadPoolExecutor;

    public static ThreadPoolManager getInstance() {
        return instance;
    }

    public ThreadPoolManager() {
        threadPoolExecutor = new ThreadPoolExecutor(4, 10,
                10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4), handler);
        threadPoolExecutor.execute(runnable);
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                FutureTask futureTask = null;

                try {

                    Log.e("zxy", "等待队列:" + taskQuene.size());
                    futureTask = (FutureTask) taskQuene.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (futureTask != null) {
                    threadPoolExecutor.execute(futureTask);
                }
                Log.e("zxy", "线程池大小:" + threadPoolExecutor.getPoolSize());
            }
        }
    };


    public <T> void execte(FutureTask<T> futureTask) throws InterruptedException {
        taskQuene.put(futureTask);
    }


    private RejectedExecutionHandler handler = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
            try {
                taskQuene.put(new FutureTask<Object>(runnable, null) {

                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    public <T> boolean removeTask(FutureTask futureTask)
    {
        boolean result=false;
        /**
         * 阻塞式队列是否含有线程
         */
        if(taskQuene.contains(futureTask))
        {
            taskQuene.remove(futureTask);
        }else
        {
            result=threadPoolExecutor.remove(futureTask);
        }
        return  result;
    }

}
