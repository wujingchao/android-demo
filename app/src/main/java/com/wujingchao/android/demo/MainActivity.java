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
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by wujingchao92@gmail.com on 2016/6/18.
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.list)   ListView mListView;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private SimpleAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
        Set<String> titles = new HashSet<>();

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
}