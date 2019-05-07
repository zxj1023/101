卸载OTA 方法：
1、执行 adb shell pm list packages -f | find "ota" 命令，找到ota 的安装目录。
2、执行 adb root,adb remount 命令后进入到 ota安装目录
3、删除掉apk和优化的odex文件。

安装OTA的方法:
1、按照卸载OTA的方法，卸载OTA
2、将自己编译出来的OTA apk 包 push 到ota的安装目录。
3、机器重启。

注意:以上必须要在root过的环境中做，刷机请刷debug版本。

测试升级，修改版本检测访问地址
修改文件 HttpRequestGetData
测试暗码：
    内部存储根目录创建文件夹:ota/test ota/code444


发布版本TODO List
1、确认代码资源
2、确认版本号
3、确认自测

adb logcat | find  "tran.com.android.taplaota"
