adb shell pm clear tran.com.android.taplagame
adb uninstall tran.com.android.taplagame
adb install ..\TalpaGameCenter\build\outputs\apk\gamecenter-debug.apk
adb shell am start tran.com.android.taplagame/tran.com.android.tapla.gamecenter.market.MarketMainActivity