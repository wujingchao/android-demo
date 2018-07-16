package com.wujingchao.android.demo.os;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.wujingchao.android.demo.BaseActivity;
import com.wujingchao.android.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PipeDemo extends BaseActivity {


    @BindView(R.id.content)
    TextView contentTv;

    @BindView(R.id.constraint_layout)
    ViewGroup constraint_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pipe_demo);
        ButterKnife.bind(this);
        String s = "sss";
        testStr(s);
    }

    public native String readFromChildPipe();

    public native void allocManyLocalRef();

    public native void testStr(String s);

    public native void jniException();

    public void throwRuntimeExc() {
        throw new RuntimeException("throwRuntimeExc");
    }

    public void readPipe(View view) {
        constraint_layout.scrollBy(-100, -100);
//        constraint_layout.setTranslationX(100);
        Log.d(TAG, "TranslationX : " + constraint_layout.getTranslationX());
        contentTv.setText(readFromChildPipe());
    }

    public void allocManyLocalRef(View view) {
        allocManyLocalRef();
    }

    public void jniException(View view) {
        try {
            jniException();
        }catch (Throwable e) {
            Log.e(TAG, "-------------------------------------");
            e.printStackTrace();
            Log.e(TAG, "-------------------------------------");
        }
    }
}
