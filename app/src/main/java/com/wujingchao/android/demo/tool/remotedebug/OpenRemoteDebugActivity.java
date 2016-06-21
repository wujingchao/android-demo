package com.wujingchao.android.demo.tool.remotedebug;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Process;
import android.os.UserHandle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wujingchao.android.demo.BaseActivity;
import com.wujingchao.android.demo.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OpenRemoteDebugActivity extends BaseActivity {

    @BindView(R.id.start) Button start;

    @BindView(R.id.stop) Button stop;

    @BindView(R.id.port) EditText port;

    @BindView(R.id.connect_hint) TextView connect;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_remote_debug);
        ButterKnife.bind(this);
    }

    private String getWifiAddress() {
        WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        if(wifiInfo != null) {
            int ip = wifiInfo.getIpAddress();
            return Formatter.formatIpAddress(ip);
        }else {
            return "";
        }
    }

    @OnClick({R.id.start,R.id.stop}) void onClick(View v) {
        int p = Integer.valueOf(port.getText().toString());
        if(p > 65535 || p < 1) {
            Toast.makeText(this,"port must be 0 ~ 65565",Toast.LENGTH_SHORT).show();
            return;
        }
        switch (v.getId()) {
            case R.id.start:
                start.setEnabled(false);
                stop.setEnabled(true);
                setDebug(p);
                connect.setText("PC execute:\n adb connect " + getWifiAddress() + ":5555");
                break;
            case R.id.stop:
                setDebug(-1);
                start.setEnabled(true);
                stop.setEnabled(false);
                break;
        }
    }

    private void setDebug(int port) {
        try {
            Runtime.getRuntime().exec("su");
            File file = new File("/data/");
            file.canWrite();
            Runtime.getRuntime().exec("setprop service.adb.tcp.port " + port);
            Runtime.getRuntime().exec("stop adbd");
            Runtime.getRuntime().exec("start adbd");
        } catch (IOException e) {
            Toast.makeText(this,"Can not get root permission",Toast.LENGTH_SHORT).show();
        }
    }
}
