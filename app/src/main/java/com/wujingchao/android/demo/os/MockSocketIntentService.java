package com.wujingchao.android.demo.os;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.wujingchao.android.demo.App;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


public class MockSocketIntentService extends IntentService {

    private static final String EXTRA_IP = "ip";

    private static final String EXTRA_PORT = "port";

    private static final String EXTRA_MSG = "msg";
    private static final String TAG = "MockSocketIntentService";

    public MockSocketIntentService() {
        super("MockSocket");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String ip = intent.getStringExtra(EXTRA_IP);
            int port = intent.getIntExtra(EXTRA_PORT, 0);
            String msg = intent.getStringExtra(EXTRA_MSG);
            if (ip == null) {
                Log.e(TAG, "ip extra is null");
                return;
            }
            sendMsg(ip, port, msg);
        }
    }

    private void sendMsg(String ip, int port, String msg) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(ip, port), 1000 * 5);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeInt(msg.length());
            dos.write(msg.getBytes());
            dos.flush();

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            int len = dis.readInt();
            Log.d(TAG, "read len : " + len);
            byte[] buffer = new byte[len];
            dis.readFully(buffer);

            Log.d(TAG, "readMessage : " + new String(buffer));

        } catch (IOException e) {
            Log.e(TAG, "sendMsg error ", e);
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendSocketMsg(String ip, int port, String msg) {
        Intent intent = new Intent(App.getContext(), MockSocketIntentService.class);
        intent.putExtra(EXTRA_IP, ip);
        intent.putExtra(EXTRA_PORT, port);
        intent.putExtra(EXTRA_MSG, msg);
        App.getContext().startService(intent);
    }

}
