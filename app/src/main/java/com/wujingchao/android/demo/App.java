package com.wujingchao.android.demo;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by wujingchao on 18/4/29.
 */

public class App extends Application {


    private static Context ctx;

    private static RefWatcher sRefWatcher;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ctx = base;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {//hprof文件的解析是在单独的文件
            return;
        }
        sRefWatcher = LeakCanary.install(this);
    }

    public static void leakWatch(Object ref) {
        sRefWatcher.watch(ref);
    }

    public static Context getContext() {
        return ctx;
    }

}
