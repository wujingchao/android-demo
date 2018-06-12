package com.wujingchao.android.demo.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ConvertUtils;
import com.wujingchao.android.demo.App;
import com.wujingchao.android.demo.BaseActivity;
import com.wujingchao.android.demo.R;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import butterknife.ButterKnife;

public class ListViewDemoActivity extends BaseActivity {

    @BindView(R.id.list_view) ListView mlistview;

    private List<String> mData = new ArrayList<>();

    private ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);


    private BaseAdapter mAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d(TAG, "getView : " + position + ", convertView = " + convertView);
            TextView textView;
            if (convertView == null) {
                textView = new TextView(parent.getContext());
                textView.setHeight(ConvertUtils.dp2px(100));
            } else {
                textView = (TextView) convertView;
            }
            textView.setText(mData.get(position));
            return textView;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        mData.add("a");
        mData.add("ab");
        mData.add("ac");
        mData.add("ad");
        mData.add("ae");
        mData.add("af");
        mData.add("ag");
        mData.add("ah");
        mData.add("aj");
        mData.add("ak");
        mData.add("al");
        mData.add("am");
        mData.add("an");
        ButterKnife.bind(this);
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.notifyDataSetChanged();
            }
        });
        mlistview.setAdapter(mAdapter);
        Toast.makeText(App.getContext(), "11", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        logListViewState();
                    }
                });
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    private void logListViewState() {
        try {
            Field recyclerField = AbsListView.class.getDeclaredField("mRecycler");
            recyclerField.setAccessible(true);
            Object recyclerObject = recyclerField.get(mlistview);
            Field mActiveViewsField = recyclerObject.getClass().getDeclaredField("mActiveViews");
            mActiveViewsField.setAccessible(true);
            Object mActiveViewsObject = mActiveViewsField.get(recyclerObject);
            int mActiveViewsLen = Array.getLength(mActiveViewsObject);
            Log.d(TAG, "\nmActiveViewsLen : " + mActiveViewsLen);



            Field mScrapViewsField = recyclerObject.getClass().getDeclaredField("mScrapViews");
            mScrapViewsField.setAccessible(true);
            ArrayList<View>[] mScrapViewsViewsObject = (ArrayList<View>[]) mScrapViewsField.get(recyclerObject);
            Log.d(TAG, "\nmScrapViewsLen : " + mScrapViewsViewsObject[0].size());


        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        scheduledThreadPool.shutdownNow();

    }

}
