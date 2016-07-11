package com.wujingchao.android.demo.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wujingchao.android.demo.R;

import butterknife.ButterKnife;

/**
 * Created by wujingchao92@gmail.com on 2016/7/9.
 */
public class TrainingDetailFragment extends Fragment {

    private TextView tv;

    private String [] data;

    public static  TrainingDetailFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt("position",position);
        TrainingDetailFragment fragment = new TrainingDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public TrainingDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = getResources().getStringArray(R.array.training_content);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int position = -1;
        if(getArguments() != null) {
            position = getArguments().getInt("position");
        }
        View v = inflater.inflate(R.layout.fragment_training_detail,container,false);
        tv = ButterKnife.findById(v,R.id.tv);
        if(data != null && position < data.length && position >= 0) {
            tv.setText(data[position]);
        }else {
            tv.setText("Empty Content");
        }
        return v;
    }

    void setDataPosition(int position) {
        if(data != null && position < data.length && tv != null) {
            tv.setText(data[position]);
        }
    }
}
