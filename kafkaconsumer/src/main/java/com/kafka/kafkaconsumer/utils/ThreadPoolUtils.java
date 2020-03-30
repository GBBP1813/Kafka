package com.kafka.kafkaconsumer.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.concurrent.*;

@Slf4j
public class ThreadPoolUtils {

    /**
     * 核心线程数
     */
    private int coreCoolSize = 20;

    /**
     * 最大线程数
     */
    private int maxNumPoolSize = 20;

    /**
     * 线程池维护线程所允许的空闲时间
     */
    private long keepAliveTime = 2000;

    /**
     * 单例线程池
     */
    private ExecutorService threadPoolExecutor;

    /**
     * 定时调度线程池
     */
    private ScheduledExecutorService scheduledExecutor;

    /**
     * 缓冲队列大小
     */
    private int queueSize = 2000;

    /**
     * 初始化标记
     */
    private volatile boolean inited = false;

    private BlockingQueue<Runnable> workQueue;

    public void init(){
        if (inited) {
            return;
        }

        synchronized (this) {
            if (inited) {
                return;
            }

            workQueue = new ArrayBlockingQueue<>(queueSize);
            this.threadPoolExecutor = new ThreadPoolExecutor(coreCoolSize, maxNumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue, new rejectedHandler());
            this.scheduledExecutor = new ScheduledThreadPoolExecutor(coreCoolSize, new rejectedHandler());
            addShutdownHook();
        }

    }

    public void addTask(Runnable task) {

        if (task == null) {
            return;
        }
        log.info("ThreadPool add task : thread hashcode: {}", task.hashCode());

        if (!inited) {
            init();
        }
        threadPoolExecutor.execute(task);

    }


    public  void addDelayTask(Runnable task, long delayTime) {
        if (task == null) {
            return;
        }
        log.info("ScheduledThreadPool add task : thread hashcode: {}", task.hashCode());
        if (!inited) {
            init();
        }
        scheduledExecutor.execute(task);
    }


    private static class rejectedHandler implements RejectedExecutionHandler {
        /**
         * define the reject policy when executor queue is full
         *
         * @see RejectedExecutionHandler
         * #rejectedExecution(java.lang.Runnable, java.util.concurrent.ThreadPoolExecutor)
         */
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 注册hook，在server shutodwn的时候可以记录线程信息
     */
    private void addShutdownHook(){
        // 在重启的时候遍历没有执行完的线程，打印出hashCode（在add的时候记录了该线程的hashCode），便于后续问题查找
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (threadPoolExecutor != null) {
                    Iterator<Runnable> iterator = workQueue.iterator();
                    StringBuffer stringBuffer = new StringBuffer();
                    while (iterator.hasNext()) {
                        stringBuffer.append(iterator.next().hashCode());
                        stringBuffer.append("-");
                    }
                    if (stringBuffer.length() > 1) {
                        log.error("server is shutting down, thread info: {}", stringBuffer);
                    }
                }
                // stop pool
                ThreadPoolUtils.this.stop();
            }
        });

    }

    public void stop() {
        threadPoolExecutor.shutdownNow();
        scheduledExecutor.shutdown();
    }


    public void setCorePoolSize(int corePoolSize) {
        this.coreCoolSize = corePoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maxNumPoolSize = maximumPoolSize;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }
}
