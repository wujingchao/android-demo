package com.wujingchao.android.demo.app.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.wujingchao.android.demo.BaseActivity;
import com.wujingchao.android.demo.R;

public class TrainingActivity extends BaseActivity implements TrainingListFragment.OnItemClickListener{

    private boolean large;

    private Fragment fragment;

    private String originTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        originTitle = getTitle().toString();
        fragment = getSupportFragmentManager().findFragmentByTag("detail");
        large =  fragment != null;
    }


    @Override
    public void OnItemClick(int position,String title) {
        if(large) {
            TrainingDetailFragment trainingDetailFragment = (TrainingDetailFragment) fragment;
            trainingDetailFragment.setDataPosition(position);
        }else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.replace(android.R.id.content,TrainingDetailFragment.newInstance(position));
            ft.addToBackStack(null);
            ft.commit();
            setTitle(title);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        Log.d(TAG,"backStackEntryCount:" + backStackEntryCount);
        if(backStackEntryCount == 0) {
            setTitle(originTitle);
        }
    }
}
