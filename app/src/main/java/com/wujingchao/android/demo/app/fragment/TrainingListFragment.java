package com.wujingchao.android.demo.app.fragment;

import android.app.ListFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.wujingchao.android.demo.R;


/**
 * Created by wujingchao92@gmail.com on 2016/7/9.
 */
public class TrainingListFragment extends ListFragment implements AdapterView.OnItemClickListener {


    private static final String TAG = "TrainingListFragment";

    public TrainingListFragment(){}


    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        String[] data = getResources().getStringArray(R.array.training_title);
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,android.R.id.text1,data);
        setListAdapter(adapter);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"onActivityCreated");
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object o = getActivity();
        if(OnItemClickListener.class.isInstance(o)) {
            ((OnItemClickListener)o).OnItemClick(position,adapter.getItem(position));
        }
    }

    interface OnItemClickListener {
        void OnItemClick(int position,String title);
    }
}
