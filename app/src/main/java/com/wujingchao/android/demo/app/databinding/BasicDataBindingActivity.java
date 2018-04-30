package com.wujingchao.android.demo.app.databinding;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wujingchao.android.demo.R;
import com.wujingchao.android.demo.databinding.ActivityBasicDataBindingBinding;

import java.util.concurrent.TimeUnit;

public class BasicDataBindingActivity extends AppCompatActivity {

    private ActivityBasicDataBindingBinding binding;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_basic_data_binding);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = new User(55, "unixman", 25, R.drawable.meizhi001);
        binding.setUser(user);
        binding.setActivity(this);
        new Thread(){
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                user.setAge(user.getAge() + 1);
                user.setId(1000);
                user.setName("shock");
                user.setPortrait(R.drawable.meizhi005);
                user.address.set("ShenZhen");
            }
        }.start();
    }

    public void onPortraitClick(View view) {
        user.setPortrait(R.drawable.meizhi008);
    }

}
