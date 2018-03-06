package com.mytool.m3u8.threadpool;

/**
 * Created by wanghaofei on 2018/1/2.
 */

public class JThread extends Thread {

    //thread pool
    private ThreadPool threadPool;
    //task
    private Runnable target;

    private boolean isShutDown = false;
    //是否闲置
    private boolean isIdle = false;

    public JThread(Runnable target, String name, ThreadPool threadPool) {
        super(name);
        this.threadPool = threadPool;
        this.target = target;
    }


    public Runnable getTarget() {
        return target;
    }

    public boolean isIdle() {
        return isIdle;
    }

    @Override
    public void run() {
        //if not stop
        while (!isShutDown) {
            isIdle = false;
            if (null != target) {
                //执行任务，注意这里调用的是run()，而不是start()
                target.run();
            }
            //任务结束，闲置状态
            isIdle = true;

            try {
                threadPool.repool(JThread.this);

                synchronized (this) {
                    //线程空闲，等待新的任务到来
                    wait();
                }
            } catch (Exception e) {

            }
            isIdle = false;
        }
    }


    public synchronized void setTarget(Runnable target) {
        this.isShutDown = false;
        this.target = target;
        //设置任务之后，通知run方法，开始执行任务
        notifyAll();
    }


    /**
     * 关闭线程
     */
    public synchronized void shutDown() {

        this.isShutDown = true;
        notifyAll();
    }

}
