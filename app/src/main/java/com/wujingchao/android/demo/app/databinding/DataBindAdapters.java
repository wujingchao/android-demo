package com.wujingchao.android.demo.app.databinding;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

/**
 * Created by wujingchao on 17/6/4.
 */

public class DataBindAdapters {

    @BindingAdapter("android:src")
    public static void setImageResource(ImageView imageView, int resId) {
        imageView.setImageResource(resId);
    }
}
