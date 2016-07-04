package com.wujingchao.android.demo.supportLibrary.design;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wujingchao.android.demo.R;

class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textview);
        }
    }