package tran.com.android.tapla.gamecenter.market.util;

import android.content.Context;
import android.os.storage.StorageManager;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class StoragerUtil {

    private static final String TAG = "StoragerUtil";


    //获取手机的SD卡的路径,是否内置,是否挂载,三种状态
    public static List<StorageInfo> getStorageVolumesPath(Context context) throws IllegalAccessException {
        ArrayList storagges = new ArrayList();
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
            Class<?>[] paramClasses = {};
            Method getVolumeList = StorageManager.class.getMethod("getVolumeList", paramClasses);
            getVolumeList.setAccessible(true);
            Object[] params = {};
            Object[] invokes = (Object[]) getVolumeList.invoke(storageManager, params);
            if (invokes != null) {
                StorageInfo info;
                Log.v(TAG, "invokes.length--->" + invokes.length);
                String firPath = null;
                String verPath = null;
                if (invokes.length > 0) {
                    Object invoke = invokes[0];
                    verPath = (String) invoke.getClass().getMethod("getPath").invoke(invoke);
                }
                Log.v(TAG, "<---verPath!--->" + verPath);
                if (verPath != null) {
                    if (!verPath.equals("/storage/emulated/0")) {
                        Log.v(TAG, "<---in ShangHai!--->");
                        for (int i = invokes.length - 1; i >= 0; i--) {
                            Object obj = invokes[i];
                            //SD卡路径
                            Method getPath = obj.getClass().getMethod("getPath");
                            String path = (String) getPath.invoke(obj);
                            info = new StorageInfo(path);
                            if (i == 0) {
                                Log.v(TAG, "path第1次!!!" + info.path);
                                firPath = info.path;
                            } else if (i == 1) {
                                Log.v(TAG, "path第" + (i + 1) + "次!!!" + info.path);
                                if (info.path.equals(firPath)) {
                                    Log.v(TAG, "一样了,break!!!" + firPath + "<-->" + info.path);
                                    break;
                                }
                            } else {
                                Log.v(TAG, "path第" + (i + 1) + "次!!!" + info.path);
                            }
                            File file = new File(info.path);
                            if ((file.exists()) && (file.isDirectory()) && (file.canWrite())) {
                                //判断是内置还是外置
                                Method isRemovable = obj.getClass().getMethod("isRemovable");
                                String state;
                                try {
                                    //SD卡挂载状态
                                    Method getVolumeState = StorageManager.class.getMethod("getVolumeState", String.class);
                                    state = (String) getVolumeState.invoke(storageManager, info.path);
                                    info.state = state;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (info.isMounted()) {
                                    info.isRemoveable = (Boolean) isRemovable.invoke(obj);
                                    storagges.add(info);
                                }
                            }
                        }
                    } else {
                        Log.v(TAG, "<---in ShenZhen!--->");
                        for (int i = 0; i < invokes.length; i++) {
                            Object obj = invokes[i];
                            //SD卡路径
                            Method getPath = obj.getClass().getMethod("getPath");
                            String path = (String) getPath.invoke(obj);
                            info = new StorageInfo(path);
                            if (i == 0) {
                                Log.v(TAG, "path第1次!!!" + info.path);
                                firPath = info.path;
                            } else if (i == 1) {
                                Log.v(TAG, "path第" + (i + 1) + "次!!!" + info.path);
                                if (info.path.equals(firPath)) {
                                    Log.v(TAG, "一样了,break!!!" + firPath + "<-->" + info.path);
                                    break;
                                }
                            } else {
                                Log.v(TAG, "path第" + (i + 1) + "次!!!" + info.path);
                            }
                            File file = new File(info.path);
                            if ((file.exists()) && (file.isDirectory()) && (file.canWrite())) {
                                //判断是内置还是外置
                                Method isRemovable = obj.getClass().getMethod("isRemovable");
                                String state;
                                try {
                                    //SD卡挂载状态
                                    Method getVolumeState = StorageManager.class.getMethod("getVolumeState", String.class);
                                    state = (String) getVolumeState.invoke(storageManager, info.path);
                                    info.state = state;
                                    Log.v(TAG, "isRemoveable-->>" + state);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (info.isMounted()) {
                                    info.isRemoveable = (Boolean) isRemovable.invoke(obj);
                                    storagges.add(info);
                                }
                            } else {
                                Log.v(TAG, "storagePath.exists?" + file.exists() + ",isDir?" + file.isDirectory() + ",canWrite?" + file.canWrite());
                            }
                        }
                    }
                }
            }
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException e1) {
            e1.printStackTrace();
        }
        storagges.trimToSize();
        Log.v(TAG, "return storagges.size-->> " + storagges.size());
        return storagges;
    }
}
