package com.wujingchao.android.demo.os.memory;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.wujingchao.android.demo.BaseActivity;
import com.wujingchao.android.demo.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DebugMemoryActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_memory);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.alloc)
    void allocMemery(View view) {
        byte[] bytes = new byte[1024 * 1024];
        Log.d(TAG,"bytes length:" + bytes.length);
    }
}
