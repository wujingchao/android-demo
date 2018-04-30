package com.wujingchao.android.demo;

import android.app.Application;
import android.content.Context;

/**
 * Created by wujingchao on 18/4/29.
 */

public class App extends Application {


    private static Context ctx;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ctx = base;
    }

    public static Context getContext() {
        return ctx;
    }

}
