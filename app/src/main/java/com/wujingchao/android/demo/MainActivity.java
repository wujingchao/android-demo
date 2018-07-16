package com.wujingchao.android.demo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.blankj.utilcode.util.LogUtils;
import com.wujingchao.android.demo.util.TraceWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by wujingchao92@gmail.com on 2016/6/18.
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.list)   ListView mListView;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private SimpleAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String prefix = intent.getStringExtra("path");
        adapter = new SimpleAdapter(this,getData(prefix),
                android.R.layout.simple_list_item_1,
                new String[] { "title" },
                new int[] { android.R.id.text1 });
    }

    private List<Map<String, Object>> getData(String path) {
        String[] subPathNames = null;
        String pathWithSlash = "";
        if(!TextUtils.isEmpty(path)) {
            subPathNames = path.split("/");
            pathWithSlash = path;
        }
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory("com.wujingchao.category.DEMO");
        PackageManager pm = getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent,0);
        List<Map<String, Object>> data = new ArrayList<>();
        final List<String> titles = new ArrayList<>();

        for(ResolveInfo ri:resolveInfos) {
            //use actual path naming
            String label = ri.activityInfo.loadLabel(pm).toString();
            if(label.startsWith(pathWithSlash)) {
                String[] fullSubPath = label.split("/");
                String title = "";
                if(TextUtils.equals(label,pathWithSlash)) {
                    title = fullSubPath[fullSubPath.length - 1];
                }else {
                    //top level,subPathNames is null
                    title = subPathNames == null ?  fullSubPath[0] : fullSubPath[subPathNames.length];
                }
                if(!titles.contains(title)) {
                    titles.add(title);
                    Map<String,Object> itemInfoMap = new HashMap<>();
                    String className = ri.activityInfo.name;
                    itemInfoMap.put("title",title);
                    itemInfoMap.put("className",className);
                    itemInfoMap.put("label",label);
                    Intent dest = new Intent();
                    dest.putExtra("title",title);
                    //directly start activity
                    if(TextUtils.equals(label,pathWithSlash + "/" + title)) {
                        dest.setClassName(this,className);
                    }else {
                        dest.setClassName(this,getClass().getName());
                        //top level
                        if(subPathNames == null) {
                            dest.putExtra("path",title);
                        }else {
                            dest.putExtra("path",pathWithSlash + "/" + title);
                        }
                    }
                    itemInfoMap.put("intent",dest);
                    data.add(itemInfoMap);
                }
            }
        }
        Collections.sort(data, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
                String l = (String) lhs.get("title");
                String r = (String)rhs.get("title");
                return l.compareToIgnoreCase(r);
            }
        });
        return data;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mListView.setAdapter(adapter);
    }

    @OnItemClick(R.id.list) void onItemClick(int position) {
        Map<String, Object> map = (Map<String, Object>) adapter.getItem(position);
        Intent intent = (Intent) map.get("intent");
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.wujingchao.android.demo.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify meizhi_a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.wujingchao.android.demo.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (BuildConfig.DEBUG && ev.getAction() == MotionEvent.ACTION_DOWN) {
            try {
                int i = 1/0;
            }catch (RuntimeException e) {
                StringBuilder builder = new StringBuilder();
                for (StackTraceElement element : e.getStackTrace()) {
                    builder.append(element.toString() + "\n");
                }
                LogUtils.d(builder.toString());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            TraceWrapper.endSection();
        }
    }
}
