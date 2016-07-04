package com.wujingchao.android.demo.supportLibrary.design;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wujingchao.android.demo.R;


public class RecyclerViewFragment extends Fragment {

    private static final String TAG = "RecyclerViewFragment";

    private static final String ARG_PARAM = "string-array";

    private int stringArrayId;

    public RecyclerViewFragment() {
        // Required empty public constructor
    }

    public static RecyclerViewFragment newInstance(int stringArrayId) {
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM,stringArrayId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG,"onCreate");
        if (getArguments() != null) {
            stringArrayId = getArguments().getInt(ARG_PARAM,-1);
        }

    }

    RecyclerView recyclerView;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG,"onCreateView");
        if(recyclerView == null) {
            recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_recycler_view, container, false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setHasFixedSize(true);
            if(stringArrayId != -1) {
                final String [] data = getResources().getStringArray(stringArrayId);
                StringArrayAdapter adapter = new StringArrayAdapter(getActivity(),data);
                recyclerView.setAdapter(adapter);
                adapter.setOnClickListener(new StringArrayAdapter.OnClickListener() {
                    @Override
                    public void onClick(View v, int position) {
                        Intent intent = new Intent(getActivity(),MaterialDesignDetailActivity.class);
                        intent.putExtra("data",data[position]);
                        startActivity(intent);
                    }
                });
            }
        }
        return recyclerView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG,"onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG,"onDetach");
    }

}
