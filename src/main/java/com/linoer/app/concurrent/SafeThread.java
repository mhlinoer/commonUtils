package com.linoer.app.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class SafeThread implements Runnable{
    private static final Logger log = LoggerFactory.getLogger(SafeThread.class);

    private static ThreadPoolExecutor THREAD_POOL;

    private static final int MAX = 8;

    static {
        int max = Runtime.getRuntime().availableProcessors();
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(200);
        RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();

        THREAD_POOL = new ThreadPoolExecutor(
                1,
                Math.min(max, MAX),
                60L,
                TimeUnit.SECONDS,
                queue,
                rejectedExecutionHandler
        );

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // 使新任务无法提交.
            THREAD_POOL.shutdown();

            try {
                // 等待10秒如果还没有关闭，则强制关闭
                if (THREAD_POOL.awaitTermination(10L, TimeUnit.SECONDS)) {
                    THREAD_POOL.shutdownNow();
                }
            } catch (InterruptedException e) {
                log.error("Thread close error", e);
                Thread.currentThread().interrupt();
                THREAD_POOL.shutdownNow();
            }
        }));
    }

    @Override
    public void run() {

    }
}
