package com.wujingchao.android.demo.supportLibrary.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.wujingchao.android.demo.R;

/**
 * Created by wujingchao92@gmail.com on 2016/6/19.
 */
class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;

    public MyViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.iv);
    }

}
