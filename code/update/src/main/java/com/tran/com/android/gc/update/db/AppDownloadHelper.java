package com.tran.com.android.gc.update.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDownloadHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "download";  //数据库名
    public static final int DATABASE_VERSION = 3;  //数据库版本

    public static final String DOWNLOAD_TABLE = "apkdownload";
    public static final String AUTOUPDATE_TABLE = "auto_update_download";

    public static final String ID = "id"; // id
    public static final String APK_ID = "apkId"; // APP的ID
    public static final String APPNAME = "appname"; // APP的名称
    public static final String DOWNLOAD_PATH = "downloadpath"; // 下载路径
    public static final String VERSION = "verison"; // 版本
    public static final String VCODE = "vcode"; // 版本号
    public static final String PACKAGENAME = "packageName"; // 包名
    public static final String STATUS = "status"; // 下载状态
    public static final String DOWN_LENGTH = "downlength"; // 已下载长度
    public static final String FILE_SIZE = "filesize"; // 文件大小(总)
    public static final String FILE_DIR = "filedir"; // 文件存放目录
    public static final String FILE_NAME = "filename"; // 文件名称
    public static final String CREATE_TIME = "createtime"; // 任务创建时间
    public static final String ICON_PATH = "iconpath"; // 图标路径
    public static final String INSTALLED = "installed"; // 是否已安装
    public static final String FINISH_TIME = "finishtime";    // 任务完成时间
    public static final String FORCE_UPDATE_FLAG = "forceupflag";    // 强制升级标识

    public AppDownloadHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS " + DOWNLOAD_TABLE + " (");
        sb.append(ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,");
        sb.append(APK_ID + " INTEGER ,");
        sb.append(APPNAME + " VARCHAR(100),");
        sb.append(DOWNLOAD_PATH + " VARCHAR(100),");
        sb.append(VERSION + " VARCHAR(50),");
        sb.append(VCODE + " INTEGER,");
        sb.append(PACKAGENAME + " VARCHAR(50),");
        sb.append(STATUS + " INTEGER,");
        sb.append(DOWN_LENGTH + " TEXT,");
        sb.append(FILE_SIZE + " TEXT,");
        sb.append(FILE_DIR + " VARCHAR(150),");
        sb.append(FILE_NAME + " VARCHAR(50),");
        sb.append(CREATE_TIME + " TEXT,");
        sb.append(ICON_PATH + " VARCHAR(100),");
        sb.append(INSTALLED + " INTEGER DEFAULT 0,");
        sb.append(FINISH_TIME + " TEXT,");
        sb.append(FORCE_UPDATE_FLAG + " INTEGER DEFAULT 0)");
        db.execSQL(sb.toString());

        sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS " + AUTOUPDATE_TABLE + " (");
        sb.append(APK_ID + " INTEGER ,");
        sb.append(APPNAME + " VARCHAR(100),");
        sb.append(DOWNLOAD_PATH + " VARCHAR(100),");
        sb.append(VERSION + " VARCHAR(50),");
        sb.append(VCODE + " INTEGER,");
        sb.append(PACKAGENAME + " VARCHAR(50),");
        sb.append(STATUS + " INTEGER,");
        sb.append(DOWN_LENGTH + " TEXT,");
        sb.append(FILE_SIZE + " TEXT,");
        sb.append(FILE_DIR + " VARCHAR(150),");
        sb.append(FILE_NAME + " VARCHAR(50),");
        sb.append(CREATE_TIME + " TEXT,");
        sb.append(ICON_PATH + " VARCHAR(100),");
        sb.append(INSTALLED + " INTEGER DEFAULT 0,");
        sb.append(FINISH_TIME + " TEXT,");
        sb.append(FORCE_UPDATE_FLAG + " INTEGER DEFAULT 0)");
        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DOWNLOAD_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + AUTOUPDATE_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DOWNLOAD_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + AUTOUPDATE_TABLE);
        onCreate(db);    }
}
