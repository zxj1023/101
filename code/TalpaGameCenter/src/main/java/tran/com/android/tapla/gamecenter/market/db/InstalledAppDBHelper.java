package tran.com.android.tapla.gamecenter.market.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InstalledAppDBHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "installAppDB"; // 数据库名
	public static final int DATABASE_VERSION = 2; // 数据库版本

	public static final String INSTALLED_TABLE = "installedTable";

	public static final String APPNAME = "appName"; // APP的名称
	public static final String ICONID = "iconId"; // APP的图标ID，要用packageManager来load
	public static final String VERSIONCODE = "versionCode"; // 版本号
	public static final String VERSION = "version"; // 版本名
	public static final String PACKAGENAME = "packageName"; // 包名
	public static final String APKPATH = "apkPath"; // APP安装路径
	public static final String FLAG = "flag"; // APP标识

	public InstalledAppDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS " + INSTALLED_TABLE + " (");
		sb.append(APPNAME + " VARCHAR(100) ,");
		sb.append(ICONID + " INTEGER,");
		sb.append(VERSIONCODE + " INTEGER,");
		sb.append(VERSION + " VARCHAR(50),");
		sb.append(PACKAGENAME + " VARCHAR(100),");
		sb.append(APKPATH + " VARCHAR(150),");
		sb.append(FLAG + " INTEGER)");
		db.execSQL(sb.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + INSTALLED_TABLE);
		onCreate(db);
	}

}
