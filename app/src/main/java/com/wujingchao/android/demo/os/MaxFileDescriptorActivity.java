package com.wujingchao.android.demo.os;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.blankj.utilcode.util.CloseUtils;
import com.wujingchao.android.demo.App;
import com.wujingchao.android.demo.BaseActivity;
import com.wujingchao.android.demo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 测试能够打开最大文件描述符
 */
public class MaxFileDescriptorActivity extends BaseActivity {

    @BindView(R.id.descriptor_count) TextView fileDescriptorCountTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_max_file_descriptor);
        ButterKnife.bind(this);
        new GetFileDescriptorTask(fileDescriptorCountTv).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    static class GetFileDescriptorTask extends AsyncTask<Void, Integer, Integer> {

        private final WeakReference<TextView> contentView;



        GetFileDescriptorTask(TextView contentView) {
            this.contentView = new WeakReference<>(contentView);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            List<FileOutputStream> fileDescriptorHolder = new ArrayList<>(1000);
            int i = 0;
            for (; ; ) {
                try {
                    FileOutputStream fos = new FileOutputStream(App.getContext().getFilesDir() + File.separator + i);
                    fileDescriptorHolder.add(fos);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    CloseUtils.closeIOQuietly(fileDescriptorHolder.toArray(new FileOutputStream[0]));
                    break;
                }
                i++;
                publishProgress(i);
            }

            return i;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            TextView tv = contentView.get();
            if (tv != null) {
                tv.setText("Max FileDescriptor : " + values[0]);
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            TextView tv = contentView.get();
            if (tv != null) {
                tv.setText("Max FileDescriptor : " + integer);
            }
        }
    }

}
