package com.wujingchao.android.demo.os;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wujingchao.android.demo.BaseActivity;
import com.wujingchao.android.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PipeDemo extends BaseActivity {


    @BindView(R.id.content)
    TextView contentTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pipe_demo);
        ButterKnife.bind(this);
    }

    public native String readFromChildPipe();

    public native void allocManyLocalRef();

    public void readPipe(View view) {
        contentTv.setText(readFromChildPipe());
    }

    public void allocManyLocalRef(View view) {
        allocManyLocalRef();
    }
}
