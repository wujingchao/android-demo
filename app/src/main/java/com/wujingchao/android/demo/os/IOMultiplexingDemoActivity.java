package com.wujingchao.android.demo.os;

import android.os.Handler;
import android.os.Bundle;
import android.widget.Button;

import com.wujingchao.android.demo.BaseActivity;
import com.wujingchao.android.demo.R;
import com.wujingchao.android.demo.util.ThreadUtils;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class IOMultiplexingDemoActivity extends BaseActivity {

    @BindView(R.id.bt_select) Button btSelect;

    @BindView(R.id.bt_poll) Button btPoll;

    @BindView(R.id.bt_epoll) Button btEpoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iomultiplexing_demo);
        ButterKnife.bind(this);
    }

    private volatile boolean selectServerIsRunning;

    private volatile boolean epollServerIsRunning;

    private Handler mHandler = new Handler();

    @OnClick(R.id.bt_select)
    public void startSelectServer() {

        if (!selectServerIsRunning) {
            ThreadUtils.submit(new Runnable() {

                @Override
                public void run() {
                    startSelectServerNative(null, 9000);
                    selectServerIsRunning = false;
                }
            });
            selectServerIsRunning = true;
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MockSocketIntentService.sendSocketMsg("127.0.0.1", 9000, "select hello");
            }
        }, 2000);
    }

    public native boolean startSelectServerNative(String ip, int port);

    public native boolean startEPollServerNative(String ip, int port);

    @OnClick(R.id.bt_poll)
    public void startPollServer() {

    }

    @OnClick(R.id.bt_epoll)
    public void startEpollServer() {
        if (!epollServerIsRunning) {
            ThreadUtils.submit(new Runnable() {

                @Override
                public void run() {
                    startEPollServerNative("127.0.0.1", 9001);
                    epollServerIsRunning = false;
                }
            });
            epollServerIsRunning = true;
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MockSocketIntentService.sendSocketMsg("127.0.0.1", 9001, "epoll hello");
            }
        }, 2000);
    }
}
