package com.mytool.m3u8.threadpool;

import java.util.List;
import java.util.Vector;

/**
 * Created by wanghaofei on 2018/1/2.
 */

public class ThreadPool {


    private static ThreadPool instance = null;
    //空闲的线程队列
    private List<JThread> idleThreads;
    //已有的线程总数
    private int threadCounter = 0;
    private Boolean isShutDown = false;

    public ThreadPool() {
        //初始化空闲线程队列容量为5
        this.idleThreads = new Vector<>(5);
        this.threadCounter = 0;
    }

    private static class SingleTonHolder {
        private static ThreadPool threadPool = new ThreadPool();
    }

    /*单例获得线程池实例*/
    public static ThreadPool getInstance() {
        return SingleTonHolder.threadPool;
    }

    public int getThreadCounter() {
        return threadCounter;
    }

    /**
     * 将线程放入池中，回收线程
     */
    protected synchronized void repool(JThread repoolingThread) {

        if (!isShutDown) {
            idleThreads.add(repoolingThread);
        } else {
            repoolingThread.shutDown();
        }
    }

    /**
     * 停止池中所有线程
     */
    public synchronized void shutDownAll() {

        this.isShutDown = true;
        for (JThread jThread : idleThreads) {
            jThread.shutDown();
        }
    }

    /**
     * 执行线程任务
     */
    public synchronized void execute(Runnable target) {

        this.isShutDown = false;
        JThread jThread = null;
 /*如果池中有空余线程，直接使用该线程*/
        if (idleThreads.size() > 0) {
            jThread = idleThreads.get(idleThreads.size() - 1);
//将该线程从池中移除
            idleThreads.remove(jThread);
            //立即执行该任务
            jThread.setTarget(target);
        } else {
 /*没有空闲线程，创建新线程*/
            threadCounter++;
            //创建线程
            jThread = new JThread(target, "JThread:" + threadCounter, ThreadPool.this);
            jThread.start();

        }
    }


}
