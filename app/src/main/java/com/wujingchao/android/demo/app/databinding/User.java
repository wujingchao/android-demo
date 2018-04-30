package com.wujingchao.android.demo.app.databinding;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;

import com.wujingchao.android.demo.BR;
import com.wujingchao.android.demo.R;

/**
 * Created by wujingchao on 17/6/4.
 */

public class User extends BaseObservable{

    private static final String TAG = "User";

    private int id;

    private String name;

    private int age;

    private int portrait;

    public ObservableField<String> address = new ObservableField<>("NULL");

    public User(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public User(int id, String name, int age, int portrait) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.portrait = portrait;
    }

    @Bindable
    public int getId() {
        return id;
    }

    @Bindable
    public String getName() {
        return name;
    }

    @Bindable
    public int getAge() {
        return age;
    }

    @Bindable
    public int getPortrait() {
        return portrait;
    }

    public void setId(int id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public void setAge(int age) {
        this.age = age;
        notifyPropertyChanged(BR.age);
    }

    public void setPortrait(int portrait) {
        this.portrait = portrait;
        notifyPropertyChanged(BR.portrait);
    }


}
