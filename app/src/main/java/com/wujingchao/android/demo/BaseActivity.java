package com.wujingchao.android.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;


/**
 * Created by wujingchao92@gmail.com on 2016/6/18.
 *
 * Responsibility:
 *      1.Set Toolbar Title
 *
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getIntent().getStringExtra("title");
        if(!TextUtils.isEmpty(title)) {
            setTitle(title);
        }
    }


    static {
        System.loadLibrary("c++_shared");
        System.loadLibrary("native-lib");
    }

}
