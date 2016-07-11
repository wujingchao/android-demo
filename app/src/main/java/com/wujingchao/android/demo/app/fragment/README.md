### 使用Fragment适配平板与手机

Fragment是Android 3.0版本引入的组件，而Android 3.0是基于平板设计的版本，可以猜想Fragment出现的原因之一就是适配大屏幕的Android设备。当然，也提供了support-v4的library支持更老的Android API。Android平板的Setting界面就是很好的案例，左边显示列表，右边显示对应列表项的详细内容。就像这样:


![fragment_large__list_detail](http://o90rk2b64.bkt.clouddn.com/fragment_large__list_detail.png)


在小屏幕的设备上，一屏只能显示列表或者内容，所以就需要分开显示，就像这样:

![fragment_list_detail](http://o90rk2b64.bkt.clouddn.com/fragment_list_detail.png)

完整的代码可以参考[Github上的实现](https://github.com/wujingchao/android-demo/tree/master/app/src/main/java/com/wujingchao/android/demo/app/fragment)，涉及的要点:

#### 1.布局加载

需要为屏幕的大小适配合适的布局文件，Android提供了[限定符(Qualifer)](https://developer.android.com/guide/topics/resources/providing-resources.html#QualifierRules)，可以根据屏幕宽高dp(Density-independent pixel)大小来适配，也可以根据屏幕的大小(Screen size)来适配。

dp的换算公式: dp =  px / (dpi / 160)

公式的由来:
dpi(dots per inch)表示一英寸有多少个像素点，Android官方文档说明 *1 dp = one physical pixel on a 160 dpi screen*，160dpi是一个基线值，屏幕实际dpi与160dpi的比就是屏幕的密度(density)，所以在160dpi的设备屏幕的密度就是1，所以1dp就对应1px，320dpi的密度就是2，1dp就对应2px。

为什么基线值是160呢？因为第一台Android设备HTC G1 就是160dpi。主流的dpi设备序列如下:

- ldpi (low) ~120dpi
- mdpi (medium) ~160dpi
- hdpi (high) ~240dpi
- xhdpi (extra-high) ~320dpi
- xxhdpi (extra-extra-high) ~480dpi
- xxxhdpi (extra-extra-extra-high) ~640dpi

对应的density分别为 *0.75, 1, 2, 3*

根据屏幕宽dp匹配:

- SmallestWidth: sw<N>dp

- Available width: w<N>dp

两者的区别就是SmallestWidth不会随着屏幕方向(orientation)而变化。
例如可以在资源文件夹里面放res/layout-sw600dp/，指定屏幕的至少宽度为600dp就加载该文件夹里面的资源文件。

根据屏幕大小Screen size匹配:
- small  *320x426 dp*
- normal  *320x470 dp*
- large  *480x640 dp*
- xlarge  *720x960 dp*

平板的大小至少为large，所以可以把资源放到res/layout-large/下。

根据上面的描述，准备两套资源分别放到文件夹res/layout,res/layout-large里面即可。

res/layout:
```
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/content"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".app.fragment.TrainingActivity">
    <fragment
        android:name="com.wujingchao.android.demo.app.fragment.TrainingListFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</FrameLayout>

```

res/layout-large:
```
<?xml version="1.0" encoding="utf-8"?>
<com.wujingchao.android.demo.supportLibrary.percentlayout.PercentLinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="horizontal">

    <fragment
        app:layout_widthPercent="30%"
        android:tag="list"
        android:layout_height="match_parent"
        android:name="com.wujingchao.android.demo.app.fragment.TrainingListFragment"/>

    <fragment
        app:layout_widthPercent="70%"
        android:tag="detail"
        android:layout_height="match_parent"
        android:name="com.wujingchao.android.demo.app.fragment.TrainingDetailFragment"/>

</com.wujingchao.android.demo.supportLibrary.percentlayout.PercentLinearLayout>

```

这里用到了PercentLinearLayout，可以参考 [PercentLayout原理以及扩展](http://wujingchao.github.com/2016/06/26/percentlayout/)。

#### 2.判断加载了哪个布局文件

当我们把fragment标签写入了布局文件，那么在LayoutInflater加载布局的时候会实例化Fragment，把Fragment#onCreateView加载的View作为子View放到ViewTree里，并且会把对应的fragment放到Activity#FragmentController里面同一管理，让Fragment拥有自己的声明周期。

所以在Activity#setContentView之后，我们就可以使用FragmentManager查找tag为detail的Fragment是否存在，就可以判断加载的是哪一个布局了。

```
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_training);
    originTitle = getTitle().toString();
    fragment = getSupportFragmentManager().findFragmentByTag("detail");
    large =  fragment != null;
}
```

#### 3.Activity与Fragment通信,Fragment与Activity通信

TrainingListFragment与TrainingDetailFragment的职责是单一的，ListFragment显示列表内容，DetailFragment显示具体的内容，他们都不知道对方的存在。

- Fragment与Activity通信

当ListFragment列表项点击的时候，就需要通知Activity去做相应的操作，就涉及了Fragment需要与Activity通信，可以在getActivity里面获取对应的Activity，并强转为具体的Activity，调用对应的方法。这里有一个耦合性很大的地方就是，获取了指定的Activity。可以在Fragment里面定义一个接口，让对应的Activity实现该接口。

```
public class TrainingListFragment extends ListFragment implements AdapterView.OnItemClickListener {

    //...

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object o = getActivity();
        if(OnItemClickListener.class.isInstance(o)) {
            ((OnItemClickListener)o).OnItemClick(position,adapter.getItem(position));
        }
    }

    interface OnItemClickListener {
        void OnItemClick(int position,String title);
    }
}
```

- Activity与Fragment通信

在Activity实现的OnItemClick方法里面就需要根据前面加载的布局文件判断做怎样的操作。

如果当前加载的布局是适合手机显示的，那么页面只有一个ListFragment，那么就需要加载DetailFragment，并且将ListFragment加入过Framgnet的返回栈(Back Stack)里面。

如果当前加载的布局是适合平板显示的，那么页面有两个Fragment，就需要通知DeatailFragment更新内容，就涉及到了Activity与Fragment通信。可以通过FragmentManager#findFragmentByTag或者FragmentManager#findFragmentById找到对应的Fragment调用方法，这里同样可以使用接口来减小耦合性，简单起见直接强转对应类型了:

```
public class TrainingActivity extends BaseActivity implements TrainingListFragment.OnItemClickListener{

    //...

    @Override
    public void OnItemClick(int position,String title) {
        if(large) {
            TrainingDetailFragment trainingDetailFragment = (TrainingDetailFragment) fragment;
            trainingDetailFragment.setDataPosition(position);
        }else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.replace(android.R.id.content,TrainingDetailFragment.newInstance(position));
            ft.addToBackStack(null);
            ft.commit();
            setTitle(title);
        }
    }
}
```

另外需要注意导入Fragment的包，Fragment在support v4 library和android.jar里面都存在，需要统一，并且获取的FragmentManager也要统一，否则会出现莫名其妙的问题，比如返回栈不起作用。

##### 参考资料

[https://developer.android.com/guide/practices/screens_support.html](https://developer.android.com/guide/practices/screens_support.html)

[https://developer.android.com/training/basics/fragments/fragment-ui.html](https://developer.android.com/training/basics/fragments/fragment-ui.html)

