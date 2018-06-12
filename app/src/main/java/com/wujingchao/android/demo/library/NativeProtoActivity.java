package com.wujingchao.android.demo.library;

import android.os.Bundle;

import com.example.tutorial.AddressBookProtos;
import com.google.protobuf.InvalidProtocolBufferException;
import com.wujingchao.android.demo.BaseActivity;
import com.wujingchao.android.demo.R;

import butterknife.BindView;
import android.widget.TextView;

public class NativeProtoActivity extends BaseActivity {

    @BindView(R.id.textView)
    TextView content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_proto);
        bindViews();
        byte[] data = getNativeSerializeBuffer();
        try {
            AddressBookProtos.AddressBook book = AddressBookProtos.AddressBook.parseFrom(data);
            content.setText(book.toString());
        }catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    private native byte[] getNativeSerializeBuffer();
}
