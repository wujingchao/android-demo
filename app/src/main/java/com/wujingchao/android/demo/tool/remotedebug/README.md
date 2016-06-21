### 打开ADB远程TCP调试工具

使用USB连接打开ADB远程方法如下:

在连接在命令行执行

```
adb tcpip 5555
adb connect 192.168.0.101:5555
```
当然，需要打开WIFI并且与PC在同一个网段。这种方式必须使用USB线，有时候会莫名其妙的连不上然后又再来一次。

其实是可以在代码里面执行相关操作的(**需要ROOT权限**)，就写了一个测试的APP专门用于打开/关闭远程调试，再也不用USB线与电脑连接才能调试APP了。

核心代码如下，当把端口设置为-1就可以恢复。

```
int port = 5555;
Runtime.getRuntime().exec("su");
Runtime.getRuntime().exec("setprop service.adb.tcp.port " + port);
Runtime.getRuntime().exec("stop adbd");
Runtime.getRuntime().exec("start adbd");
```

效果图：

![ADBRemoteDebug](http://o90rk2b64.bkt.clouddn.com/ADBRemoteDebug.png)