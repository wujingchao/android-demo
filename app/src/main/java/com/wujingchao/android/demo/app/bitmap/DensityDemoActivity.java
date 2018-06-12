package com.wujingchao.android.demo.app.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.wujingchao.android.demo.BaseActivity;
import com.wujingchao.android.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DensityDemoActivity extends BaseActivity {


    @BindView(R.id.img) ImageView iv;

    @BindView(R.id.meta) TextView metaTv;

    static Object leakObj;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        leakObj = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_density_demo);
        ButterKnife.bind(this);
        metaTv.post(new Runnable() {
            @Override
            public void run() {
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                BitmapDrawable bitmapDrawable = (BitmapDrawable) iv.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                StringBuilder builder = new StringBuilder();

                if (metrics.density != metrics.densityDpi/160) {
                    throw new RuntimeException("metrics.density != metrics.densityDpi/160");
                }
                builder.append(" metrics density: " + metrics.densityDpi/160);
                builder.append(" metrics densityDpi " + metrics.densityDpi);
                builder.append(" width: " + iv.getWidth());
                builder.append(" height: " + iv.getHeight());

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeResource(getResources(), R.drawable.meizhi002, options);

//                int scaleWidth = options.outWidth * (metrics.densityDpi /options.inDensity);//or options.inTargetDensity
                int scaleWidth = (int) (options.outWidth * (1f * metrics.densityDpi /options.inDensity));//or options.inTargetDensity
                int scaleHeight = (int) (options.outHeight * (1f * metrics.densityDpi /options.inDensity));//or options.inTargetDensity
                if (scaleWidth * scaleHeight * 4 != bitmap.getByteCount()) {
                    throw new RuntimeException("scaleWidth * scaleHeight * 4 != bitmap.getByteCount()");

                }
                builder.append(" imgSize : " + bitmap.getByteCount());

                Log.e(TAG, builder.toString());
                metaTv.setText(builder);
            }
        });
    }
}
