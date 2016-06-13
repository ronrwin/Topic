package com.uc.ronrwin.uctopic.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description : This is my code. Do not even change a single
 * line, otherwise ... ^_*
 * <p>
 * Creation    : 2016/5/30
 * Author      : Ronrwin
 */
public class ThreadUtils {
    private static ExecutorService mExecutor = null;
    private static final Object LOCK = new Object();
    private static final BlockingQueue<Runnable> DEFAULT_WORK_QUEUE = new LinkedBlockingDeque<>(10);

    private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {
        private final AtomicInteger counter = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "ThreadUtils thread: " + counter.incrementAndGet());
        }
    };

    public static ExecutorService getExecutorService() {
        synchronized (LOCK) {
            if (mExecutor == null) {
                mExecutor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS,
                        DEFAULT_WORK_QUEUE, THREAD_FACTORY);
            }
        }
        return mExecutor;
    }

    public static void releaseExecutorService() {
        synchronized (LOCK) {
            if (mExecutor != null) {
                mExecutor.shutdown();
                mExecutor = null;
            }
        }
    }
}
