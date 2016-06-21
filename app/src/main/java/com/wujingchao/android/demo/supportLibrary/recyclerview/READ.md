![瀑布流1](http://o90rk2b64.bkt.clouddn.com/recyclerview1.png? 
imageView2/2/w/212/h/376)![瀑布流2](http://o90rk2b64.bkt.clouddn.com/recyclerview2.png?  
imageView2/2/w/212/h/376)

#### RecyclerView重要的类:

- Adapter:提供数据集合以及显示的View Item
- ViewHolder:列表项缓存的View对象
- LayoutManager：控制布局,默认实现有LinearLayoutManager GridLayoutManager StaggeredGridLayoutManager
- ItemDectoration:绘制列表里面项与项之间的装饰，例如分割线 ApiDemo里面有分割线实现
- ItemAnimator:列表项添加，移除，重排序的动画

该组件定制型太强，所以相对于ListView提出了很多新概念，刚接触可能会觉得比较复杂。

http://stackoverflow.com/a/27298868

由于在代码里面初始化的RecyclerView没有初始化ScrollBar的样式，是没有ScrollBar，需要在在xml文件里面声明

瀑布流，不规则的美。

设置点击事件比较不方便，只能在ViewHolder添加自定义回调。
