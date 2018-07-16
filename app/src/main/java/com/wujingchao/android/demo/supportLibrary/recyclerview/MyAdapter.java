package com.wujingchao.android.demo.supportLibrary.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wujingchao.android.demo.R;

import java.util.List;

/**
 * Created by wujingchao92@gmail.com on 2016/6/19.
 */
class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

    private List<Integer> resIds;

    public MyAdapter(List<Integer> resIds){
        this.resIds = resIds;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item,parent,false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
         holder.imageView.setImageResource(resIds.get(position));//TODO  ANR....

         holder.imageView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(mOnItemClickListener != null) {
                     mOnItemClickListener.onClick(holder,position);
                 }
             }
         });

         holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
             @Override
             public boolean onLongClick(View v) {
                 if(mOnItemLongClickListener != null) {
                     mOnItemLongClickListener.onLongClick(holder,position);
                     return true;
                 }
                 return false;
             }
         });
    }

    @Override
    public int getItemCount() {
        return resIds.size();
    }

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }


    interface OnItemClickListener {
        void onClick(RecyclerView.ViewHolder VH ,int position);
    }

    interface OnItemLongClickListener {
        void onLongClick(RecyclerView.ViewHolder VH,int position);
    }

}
