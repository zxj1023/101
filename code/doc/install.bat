adb root
adb remount
adb shell pm clear com.aurora.market
adb shell rm -rf /system/priv-app/TalpaGC/*
adb shell rm -rf /system/priv-app/TalpaGC/*
adb shell rm -rf /data/data/tran.com.android.taplagame
adb push ..\TalpaGameCenter\build\outputs\apk\TalpaGameCenter-release.apk  /system/priv-app/TalpaGC
adb reboot