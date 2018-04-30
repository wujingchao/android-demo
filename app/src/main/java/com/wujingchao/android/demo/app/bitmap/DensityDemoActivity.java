package com.wujingchao.android.demo.app.bitmap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import com.wujingchao.android.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DensityDemoActivity extends AppCompatActivity {


    @BindView(R.id.img) ImageView iv;

    @BindView(R.id.meta) TextView metaTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_density_demo);
        ButterKnife.bind(this);
        metaTv.post(new Runnable() {
            @Override
            public void run() {
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                metaTv.setText("density:" + metrics.density + " densityDpi: " + metrics.densityDpi + " width: " + iv.getWidth() + ", height: " + iv.getHeight());
            }
        });
    }
}
