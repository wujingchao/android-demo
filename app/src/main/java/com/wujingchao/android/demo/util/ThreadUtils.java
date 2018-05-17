package com.wujingchao.android.demo.util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public final class ThreadUtils {

    private static Executor pool = Executors.newCachedThreadPool();


    public static void submit(Runnable task) {
        pool.execute(task);
    }

}
