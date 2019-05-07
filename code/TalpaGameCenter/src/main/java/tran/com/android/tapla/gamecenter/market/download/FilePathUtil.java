package tran.com.android.tapla.gamecenter.market.download;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;

import java.io.File;
import java.util.List;

import tran.com.android.tapla.gamecenter.market.util.Globals;
import tran.com.android.tapla.gamecenter.market.util.Log;
import tran.com.android.tapla.gamecenter.market.util.StorageInfo;
import tran.com.android.tapla.gamecenter.market.util.StoragerUtil;
import tran.com.android.tapla.gamecenter.market.util.StringUtils;

public class FilePathUtil {

    /**
     * 获取存放软件的位置
     *
     * @return
     */
    public static String getAPKFilePath(Context ctx, long fileSize) {
//		if (FileUtil.isExistSDcard()) {
//			pathString = getSDcardSoftWarePath();
//		} else {
//			pathString = getDataSoftWarePath(ctx);
//		}
        String pathString = null;
        try {
            List<StorageInfo> storageList = StoragerUtil.getStorageVolumesPath(ctx);

            if (storageList.size() > 1) {
                pathString = PreferenceManager.getDefaultSharedPreferences(ctx).getString(Globals.DSIK_NAME, null);

            } else {
                pathString = storageList.get(0).getPath();
            }

            if (pathString != null) {
                pathString = pathString + Globals.GAMECENTER_DOWNLOAD;
                File temp = new File(pathString);
                if (!temp.exists()) {
                    temp.mkdirs();
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return pathString;
    }


    public static String setDownDir(Context ctx, long fileSize) {


        String exterSDpath = "";
        String interSDpath = "";
        try {
            //返回当前的内外置SD卡情况
            List<StorageInfo> storageList = StoragerUtil.getStorageVolumesPath(ctx);
            int sdcardSize = storageList.size();
            if (sdcardSize < 1 || storageList == null) {
                //TODO 没有存贮的情况下 return
                return Globals.STORAGE_ERROR;
            }
            int fsize = StringUtils.formatIntSize((long) fileSize);

            if (fsize < 50) {
                fsize = 50;
            }
            interSDpath = storageList.get(0).getPath();
            int remainingSize;
            //************预防不支持SD卡热插拔发生的crash*********
            if (sdcardSize > 1) {
                exterSDpath = storageList.get(1).getPath();
                try {
                    StatFs statFs = new StatFs(exterSDpath);
                } catch (Exception e) {
                    e.printStackTrace();
                    storageList.remove(1);
                }
            }
            //*******************热拔插end**********************
            if (sdcardSize > 1) {
                //插了外置SD卡的情况
                remainingSize = spaceSize(exterSDpath);
                if (remainingSize < fsize || remainingSize == -1) {
                    //外置SD卡剩余空间小于下载文件大小
                    remainingSize = spaceSize(interSDpath);
                    if (remainingSize < fsize || remainingSize == -1) {
                        //内置SD卡剩余空间也小于下载文件大小
                        //TODO 存储空间不足的情况下 return
                        return Globals.NO_SPACE;
                    } else {
                        return interSDpath;
                    }
                } else {
                    return exterSDpath;
                }
            } else {
                remainingSize = spaceSize(interSDpath);
                if (remainingSize < fsize || remainingSize == -1) {
                    //TODO 存储空间不足的情况下 return
                    return Globals.NO_SPACE;
                }
                return interSDpath;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return interSDpath;
    }

    /**
     * 获得该路径剩余可用空间的大小
     */
    private static int spaceSize(String path) {
        File downFile = new File(path);
        String exterSDdir = downFile.getAbsolutePath(); //获得sdcard真实路径
        try {
            StatFs sf = new StatFs(exterSDdir);
            long blockSize = sf.getBlockSizeLong();
            long availCount = sf.getAvailableBlocksLong();
            return StringUtils.formatIntSize(availCount * blockSize);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 设置下载路径
     */
    public static String setDownDir(Context mContext) {
        try {
            List<StorageInfo> storageList = StoragerUtil.getStorageVolumesPath(mContext);
            int sdcardSize = storageList.size();
            SharedPreferences preferences;
            if (sdcardSize < 1 || storageList == null) {
                return null;
            }

            if (sdcardSize > 1) {
                // 默认使用外置SD卡
                return storageList.get(1).getPath();
            } else {
                return storageList.get(0).getPath();
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取存放自动更新软件的位置
     *
     * @return
     */
    public static String getAutoUpdateFilePath(Context ctx) {
        String pathString = "";
        if (FileUtil.isExistSDcard()) {
            pathString = getSDcardAutoUpdatePath();
        } else {
            pathString = getDataAutoUpdatePath(ctx);
        }
        File temp = new File(pathString);
        if (!temp.exists()) {
            temp.mkdirs();
        }
        return pathString;
    }

    /**
     * 获取SD卡存放软件的位置
     *
     * @return
     */
    private static String getSDcardSoftWarePath() {
        return Environment.getExternalStorageDirectory()
                + "/GameCenter Download/apk/";
    }

    /**
     * 获取内部存储软件的位置
     *
     * @param ctx
     * @return
     */
    private static String getDataSoftWarePath(Context ctx) {
        return "/data/data/" + ctx.getPackageName() + "/cache/apk/";

    }

    /**
     * 获取SD卡存放自动更新软件的位置
     *
     * @return
     */
    private static String getSDcardAutoUpdatePath() {
        return Environment.getExternalStorageDirectory()
                + "/market/autoupdate/";
    }

    /**
     * 获取内部存储自动更新软件的位置
     *
     * @param ctx
     * @return
     */
    private static String getDataAutoUpdatePath(Context ctx) {
        return "/data/data/" + ctx.getPackageName() + "/cache/autoupdate/";

    }


}
