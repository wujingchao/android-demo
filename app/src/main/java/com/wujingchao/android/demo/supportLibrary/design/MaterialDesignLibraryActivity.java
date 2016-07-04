package com.wujingchao.android.demo.supportLibrary.design;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wujingchao.android.demo.BaseActivity;
import com.wujingchao.android.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MaterialDesignLibraryActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.fab)  FloatingActionButton fab;

    @BindView(R.id.root) ViewGroup root;

    @BindView(R.id.toolbar)Toolbar toolbar;

    @BindView(R.id.tab_layout) TabLayout tabLayout;

    @BindView(R.id.navigation_view) NavigationView navigationView;

    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;

    @BindView(R.id.view_pager) ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_library);
        ButterKnife.bind(this);

        //set up actionBar...
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        //set up NavigationView...
        navigationView.setNavigationItemSelectedListener(this);

        viewPager.setAdapter(new Adapter(getSupportFragmentManager(),new int[]{R.array.magical,R.array.grace,R.array.white},
                new String[]{"Magical","Grace","White"}));
        tabLayout.setupWithViewPager(viewPager);

    }

    @OnClick({R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                Snackbar.make(root,"Your are my sunshine",Snackbar.LENGTH_SHORT)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                }else {
                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                }

                                if (Build.VERSION.SDK_INT >= 11) {
                                    recreate();
                                }
                            }
                        })
                        .show();
                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String title = item.getTitle().toString();
        Toast.makeText(this,title,Toast.LENGTH_SHORT).show();
        item.setChecked(true);
        return true;
    }

    static class Adapter extends FragmentPagerAdapter {

        private int[] stringArrays;

        private String[] titles;

        public Adapter(FragmentManager fm,int[] stringArrays,String[] titles) {
            super(fm);
            this.stringArrays = stringArrays;
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return RecyclerViewFragment.newInstance(stringArrays[position]);
        }

        @Override
        public int getCount() {
            return stringArrays.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
