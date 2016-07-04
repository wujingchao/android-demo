package com.wujingchao.android.demo.supportLibrary.design;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wujingchao.android.demo.R;

class StringArrayAdapter extends RecyclerView.Adapter<ViewHolder> {

        private String [] strings;

        private Context ctx;

        private OnClickListener onClickListener;

        StringArrayAdapter(Context ctx, String[] strings) {
            this.ctx = ctx;
            this.strings = strings;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.design_recycler_view_item,parent,false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.textView.setText(strings[position]);
            if(onClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListener.onClick(v,holder.getAdapterPosition());
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return strings.length;
        }

        public void setOnClickListener(OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }
        
        
        interface OnClickListener {

            public void onClick(View v,int position);
        }

}