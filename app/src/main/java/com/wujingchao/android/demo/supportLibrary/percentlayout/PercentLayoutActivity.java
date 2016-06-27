package com.wujingchao.android.demo.supportLibrary.percentlayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.wujingchao.android.demo.BaseActivity;
import com.wujingchao.android.demo.R;

public class PercentLayoutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percent_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_percenlayout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.linearlayout) {
            setContentView(R.layout.activity_percent_layout2);
            return true;
        }else if(itemId == R.id.relativelayout) {
            setContentView(R.layout.activity_percent_layout);
            findViewById(R.id.view1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
