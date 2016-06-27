### PercentLayout原理以及扩展

#### 概述

Percent Support Library 用于使用百分比控制子View在布局里面占用的大小，相比于layout_weight这个属性具有更高的灵活性，并且margin属性也支持使用百分比控制，这是layout_weight不具备的，官方只提供了PercentFrameLayout和PercentRelativeLayout，但是我们可以使用PercentLayoutHelper来让我们的布局也支持百分比的效果。目前支持的属性有：

- layout_widthPercent
- layout_heightPercent
- layout_marginPercent
- layout_marginLeftPercent
- layout_marginTopPercent
- layout_marginRightPercent
- layout_marginBottomPercent
- layout_marginStartPercent
- layout_marginEndPercent
- layout_aspectRatio

特别地，layout_aspectRatio是用来表示宽高比例，当我们指定宽或者高的任一边的长度或者百分比，其就能够自动地计算出来另外一边的长度。

#### 使用

在module级别的build.gradle里面的dependencies加上依赖即可:

```
dependencies {
	com.android.support:percent:23.3.0
}
```

先来看一下效果图:

![PercentRelativeLayout](http://o90rk2b64.bkt.clouddn.com/percentrelativelayout.png)

直接在布局文件里面声明即可，也比较好理解，不再累述，下面直接看实现的原理。

```
<android.support.percent.PercentRelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/content"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <View
        android:id="@+id/view1"
        app:layout_widthPercent="25%"
        app:layout_heightPercent="10%"
        android:background="#333366"/>

    <View
        android:id="@+id/view2"
        android:layout_toRightOf="@id/view1"
        app:layout_widthPercent="25%"
        app:layout_heightPercent="10%"
        android:background="#999933"/>

    <View
        android:id="@+id/view3"
        android:layout_toRightOf="@id/view2"
        app:layout_widthPercent="25%"
        app:layout_heightPercent="10%"
        android:background="#996600"/>

    <View
        android:layout_toRightOf="@id/view3"
        app:layout_widthPercent="25%"
        app:layout_heightPercent="10%"
        android:background="#333333"/>

    <View
        app:layout_aspectRatio="578%"
        app:layout_widthPercent="100%"
        app:layout_marginTopPercent="10%"
        android:background="#669999" />


</android.support.percent.PercentRelativeLayout>
```

#### 原理

由于上面的例子是PercentRelativeLayout，所以我们使用其来讲解，其实PercentFrameLayout和PercentRelativeLayout里面的代码几乎一样。

系统初始化布局都是通过LayoutInflater来实现的,比如在setContentView里面。LayoutParams在平时使用还是比较多的，其作用就是让父View决定如何摆放自己以及自己的宽高。当我们把child view写到布局里面，那么child view的LayoutParams由ViewGroup的generateDefaultLayoutParams来设置。下面就是LayoutInflater的为子View赋值LayoutParams的关键代码:

```
void rInflate(XmlPullParser parser, View parent, final AttributeSet attrs,
    boolean finishInflate, boolean inheritContext) {
    //...
    while (((type = parser.next()) != XmlPullParser.END_TAG ||
        parser.getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {

    	//...
        final View view = createViewFromTag(parent, name, attrs, inheritContext);
        final ViewGroup viewGroup = (ViewGroup) parent;
        final ViewGroup.LayoutParams params = viewGroup.generateLayoutParams(attrs);
        rInflate(parser, view, attrs, true, true);
        viewGroup.addView(view, params);
    //...
}
```

PercentRelativeLayout所以就实现了generateLayoutParams的方法，并且返回的是继承的RelativeLayout.LayoutParams，这样就保留了RelativeLayout原来属性。并且generateLayoutParams的方法参数是AttributeSet，里面就包含了我们声明的PercentLayout的属性值，例如layout_widthPercent等等。

PercentRelativeLayout.LayoutParams在构造方法就通过PercentLayoutHelper对AttributeSet进行解析，解析的结果保存在自定义的数据结构PercentLayoutHelper.PercentLayoutInfo，里面包括了在概述里面说的所有属性。

```
private PercentLayoutHelper.PercentLayoutInfo mPercentLayoutInfo;

public LayoutParams(Context c, AttributeSet attrs) {
    super(c, attrs);
    mPercentLayoutInfo = PercentLayoutHelper.getPercentLayoutInfo(c, attrs);
}
```

另外，我们知道所有的LayoutParams都是继承ViewGoup.LayoutParams，里面有个方法是用来初始化View两个layout_width,layout_height:

```
protected void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
    width = a.getLayoutDimension(widthAttr, "layout_width");
    height = a.getLayoutDimension(heightAttr, "layout_height");
}
```
如果我们没有在布局文件里面声明这两个属性，那么在LayoutInflater初始化的就会抛UnsupportedOperationException。由于使用了百分比的属性，所以这个属性就可以不需要，为了让其不抛异常，必须重写这个方法。

PercentLayoutHelper#fetchWidthAndHeight就是让其在没有值的情况下让LayoutParams的width和height的值都为0。 

```
@Override
protected void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
    PercentLayoutHelper.fetchWidthAndHeight(this, a, widthAttr, heightAttr);
}
```

初始化布局的时候已经把所有需要的数据都保持在了PercentLayoutInfo里面，接下来就到了我们熟悉的三大流程了:onMeasure->onLayout->onDraw，由于是ViewGroup，所以只需要关注前面两个即可。先来看onMeasure:

```
@Override
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    mHelper.adjustChildren(widthMeasureSpec, heightMeasureSpec);
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    if (mHelper.handleMeasuredStateTooSmall()) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
```

adjustChildren的主要工作就是遍历所有的child view，通过child view的PercentLayoutHelper.LayoutParams的宽高百分比转换为实际的占用的像素宽高。并保存在对应child view的LayoutParams里，然后再调用RelativeLayout原有的onMeasure，就可以实现宽高的百分比转换。

我们在前面读书笔记中[View的工作原理measure的过程里面有提到](http://wujingchao.github.io/2016/01/10/art-of-android-development-notes-view-theory/#View的工作过程),有时候我们在测量View的时候，如果父View最大能够给我们的空间小于我们需要的空间，就会给测量结果的高两位加上相应的状态表示MEASURED_STATE_TOO_SMALL。

如果出现了这种情况，并且为layout_width和layout_height设置了wrap_content，就需要调用handleMeasuredStateTooSmall来处理，将宽或者高重新按照wrap_content的属性来测量。


然后就到了onLayout的阶段，基本什么也没做。如果在child view里面设置了layout_width，layout_height等属性，那么在onMeasure阶段就会调用adjustChildren将他们都保存起来，等onLayout结束之后再把他们给还原回去。

```
@Override
protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    mHelper.restoreOriginalParams();
}
```

#### 扩展

通过前面的分析，我们可以很容易地将我们现有的组件通过PercentLayoutHelper这个类让我们现有的组件支持百分比控制child view的宽高，比如LinearLayout。代码几乎都长得一样，可以具体看[Github上面的实现:)]()。

根据上面的原理分析，具体的实现步骤:

1.继承布局原有的LayoutParams，并实现PercentLayoutHelper.PercentLayoutParams接口并在构造方法里面调用getPercentLayoutInfo(Context, AttributeSet)解析layout_widthPercent等参数。

```
public static class LayoutParams extends LinearLayout.LayoutParams implements PercentLayoutHelper.PercentLayoutParams {
    public LayoutParams(Context c, AttributeSet attrs) {
        super(c, attrs);
        mPercentLayoutInfo = PercentLayoutHelper.getPercentLayoutInfo(c, attrs);
    }
}
```


2.重写在LayoutParams的setBaseAttributes(TypedArray, int, int) 方法，里面就加这一句代码：

```
@Override
protected void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
    PercentLayoutHelper.fetchWidthAndHeight(this, a, widthAttr, heightAttr);
}
```

3.重写布局的generateLayoutParams(AttributeSet)方法，新构造我们实现了LayoutParams。

4.在onMeasure(int, int)方法里面调用mHelper.adjustChildren进行百分比转换:

```
 protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
     mHelper.adjustChildren(widthMeasureSpec, heightMeasureSpec);
     super.onMeasure(widthMeasureSpec, heightMeasureSpec);
     if (mHelper.handleMeasuredStateTooSmall()) {
         super.onMeasure(widthMeasureSpec, heightMeasureSpec);
     }
 }
 ```

5.在onLayout方法里面调用mHelper.restoreOriginalParams()恢复默认的LayoutParams参数:

 ```
protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
 super.onLayout(changed, left, top, right, bottom);
 mHelper.restoreOriginalParams();
}

 ```
 
##### 参考资料:

[1.PercentLayoutHelper](https://developer.android.com/reference/android/support/percent/PercentLayoutHelper.html)
