### RecyclerView实现瀑布流

>RecyclerView is a flexible view for providing a limited window into a large data set.

RecyclerView可以说是ListView和GridView的升级版本，提供了ListView和GridView的基础功能，并且有良好的扩展性，比如可以控制列表的布局和动画。该组件定制型太强，所以相对于ListView提出了很多新概念，刚接触可能会觉得比较复杂。

先来看下效果图：

![瀑布流](http://o90rk2b64.bkt.clouddn.com/recyclerviewdemo.png)

与RecyclerView相关的重要基础类:

- RecyclerView.LayoutManager：控制布局,提供的实现有LinearLayoutManager,GridLayoutManager,StaggeredGridLayoutManager。LinearLayoutManager提供类似ListView的列表布局，并且可以控制列表的方向。GridLayoutManager提供了类似GridView的列表布局。Staggered的意思是错列的，StaggeredGridLayoutManager继承GridLayoutManager，允许宽高不相等，所以可以用来实现瀑布流的布局。

- ViewHolder:缓存的View Item，避免多次调用findViewById，类似我们自己在ListView设置的ViewHolder

- RecyclerView.Adapter:提供数据集合以及显示的View Item

- RecyclerView.ItemDectoration:绘制列表里面项与项之间的装饰，例如分割线 ApiDemo里面有分割线实现。

- RecyclerView.ItemAnimator:列表项添加，移除，重排序的动画效果

RecyclerView是android-support-library-v7里面的类，需要在gradle里面导入:

```
compile 'com.android.support:recyclerview-v7:23.3.0'
//或者 compile 'com.android.support:recyclerview-v7:+' 使用最新的版本
```

RecyclerView实现瀑布流的实现步骤如下，其实也就是通常使用RecyclerView的步骤：

1.在布局文件里面声明RecyclerView,findViewById获取实例，或者通过代码的方式将RecyclerView添加到布局里面。

这两种方式还是有区别的，xml的方式可以声明RecyclerView的ScrollBar，代码方式由于初始化的时候系统未初始化ScrollBar的相关属性，所以这种方式初始化的Recycler是没有ScrollBar的。[具体参考](http://stackoverflow.com/questions/27056379/is-there-any-way-to-enable-scrollbars-for-recyclerview-in-code/27298868#27298868)。

```
    <!-- 声明scrollbars -->
    <android.support.v7.widget.RecyclerView
        xmlns:android="http://schemas.android.com/apk/res/android"
        android:scrollbars="vertical"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```

2.初始化Recycler的数据适配器

在我们使用ListView的时候，我们会在Adapter#getView的时候使用ViewHolder来缓存我们的Sub View对象避免重复的findViewById，已到达优化ListView的目的，但是我们可以选择用与不用。但是在RecyclerView.Adapter中就必须使用，因为在创建View对象的使用明确地要求我们返回ViewHolder对象，并且在给View加载数据也是使用ViewHolder。

在实现RecyclerView.Adapter必须要实现的三个方法，我们的列表项只有一个ImageView所以比较好理解。并且当我们的数据源发生改变是可以通过notifyItemInserted(int index),notifyItemRemoved(int index),notifyItemRangeChanged(int start,int end)通知数据适配器更新数据，如果我们在给RecyclerView设置了ItemAnimator还会显示相应的动画效果。

```
 public class MyViewHolder extends RecyclerView.ViewHolder {

	    ImageView imageView;

	    public MyViewHolder(View itemView) {
	        super(itemView);
	        imageView = (ImageView) itemView.findViewById(R.id.iv);
	    }

	}


public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

    private List<Integer> resIds;

    public MyAdapter(List<Integer> resIds){
        this.resIds = resIds;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item,null);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
         holder.imageView.setImageResource(resIds.get(position));
    }

    @Override
    public int getItemCount() {
        return resIds.size();
    }
}
```

3.默认我们的列表项与项之间是没有间隙的，可以通过添加自定义的ItemDecorations来实现列表项之间的间隙。

我们可以通过重写ItemDecorations的getItemOffsets来控制项与项之间的偏移量来实现列表之间的间隙。

```
/**
 * 代码来自 http://stackoverflow.com/a/30701422
 */
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

  private int spanCount;
  private int spacing;
  private boolean includeEdge;

  public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
    this.spanCount = spanCount;
    this.spacing = spacing;
    this.includeEdge = includeEdge;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    int position = parent.getChildAdapterPosition(view); // item position
    int column = position % spanCount; // item column

    if (includeEdge) {
      outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
      outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

      if (position < spanCount) { // top edge
        outRect.top = spacing;
      }
      outRect.bottom = spacing; // item bottom
    } else {
      outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
      outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
      if (position >= spanCount) {
        outRect.top = spacing; // item top
      }
    }
  }
}
```

4.定义列表项增加，删除，重排序的动画，提供了默认的实现DefaultItemAnimator，效果为平移动画。

5.给列表项增加点击事件与长按事件，RecyclerView默认是没有提供的，可以通过在RecyclerView.Adapter中绑定数据的时候加上我们需要的事件，比如:

```
public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

    private List<Integer> resIds;

    public MyAdapter(List<Integer> resIds){
        this.resIds = resIds;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item,null);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
         holder.imageView.setImageResource(resIds.get(position));

         holder.imageView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(mOnItemClickListener != null) {
                     mOnItemClickListener.onClick(holder,position);
                 }
             }
         });

         holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
             @Override
             public boolean onLongClick(View v) {
                 if(mOnItemLongClickListener != null) {
                     mOnItemLongClickListener.onLongClick(holder,position);
                     return true;
                 }
                 return false;
             }
         });
    }

    @Override
    public int getItemCount() {
        return resIds.size();
    }

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }


    interface OnItemClickListener {
        void onClick(RecyclerView.ViewHolder VH ,int position);
    }

    interface OnItemLongClickListener {
        void onLongClick(RecyclerView.ViewHolder VH,int position);
    }

}
```

这样，就可以监听列表项的点击事件和长按事件了。

综合一下，实现瀑布流的RecyclerView的代码就如下:

```
    RecyclerView recyclerView = new RecyclerView(this);
    RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
    recyclerView.setLayoutManager(layoutManager);
    adapter = new MyAdapter(getData());
    recyclerView.setAdapter(adapter);
    recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,10,true));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    adapter.setOnItemClickListener(this);
```

完整的示例代码查看Github上面的源码:)


**参考资料**:[http://www.grokkingandroid.com/first-glance-androids-recyclerview/](http://www.grokkingandroid.com/first-glance-androids-recyclerview/)

