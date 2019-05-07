package tran.com.android.tapla.gamecenter.market.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import tran.com.android.tapla.gamecenter.datauiapi.bean.PushInfoItem;
import tran.com.android.tapla.gamecenter.datauiapi.bean.cacheitem;
import tran.com.android.tapla.gamecenter.market.util.FileLog;
import tran.com.android.tapla.gamecenter.market.util.SystemUtils;
import tran.com.android.tapla.gamecenter.market.util.TimeUtils;

public class PushDataAdapter extends DBAdapter {

	public static final String TAG = "PushDataAdapter";
	public static final String TABLE_NAME = "tbl_push";// 数据库表名
	public static final  String ID =  "id";         //应用ID，在服务器端数据库中存储的ID
	public static final  String  THEME = "theme";		//消息主题
	public static final  String  SUMMARY = "summary";		//消息主题
	public static final  String ACTION = "action";		//1跳游戏详情，2跳转专题，3跳WAP页面
	public static final  String TO = "linkTo";	//包名或者专题ID，或者wap url
	public static final  String STARTTIME = "startTime";	//开始时间时间
	public static final  String ENDTIME = "endTime";		//结束时间
	public static final  String FLAG = "flag";		//标记 0表示还木有推送，1表示已经推送。

	private DBOpenHelper mDBOpenHelper;
	private SQLiteDatabase mDb;
	private Context mContext;

	public PushDataAdapter(Context context) {
		this.mContext = context;
	}

	/**
	 * 空间不够存储的时候设为只读
	 * 
	 * @throws SQLiteException
	 */

	public void open() throws SQLiteException {
		mDBOpenHelper = new DBOpenHelper(mContext);
		try {
			mDb = mDBOpenHelper.getWritableDatabase();
		} catch (SQLiteException e) {
			mDb = mDBOpenHelper.getReadableDatabase();

			FileLog.e(TAG, e.toString());
		}
	}

	/**
	 * 
	 * 调用SQLiteDatabase对象的close()方法关闭数据库
	 */
	public void close() {
		if (mDb != null) {
			mDb.close();
			mDb = null;
		}
	}

	/**
	 * 保存缓存数据(多条)
	 * 
	 *            <DownloadResult> result
	 * 
	 */
	public void insert(List<PushInfoItem> result) {
		mDb.beginTransaction();

		try {
			for (PushInfoItem item : result) {
				if(!isExistPush(item.getId())){
					insert(item);
				}
			}
			mDb.setTransactionSuccessful();
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		} finally {
			mDb.endTransaction();
		}
	}

	/**
	 * 保存缓存数据(单条)
	 * 
	 *            result
	 * 
	 */
	public void insert(PushInfoItem result) {
		ContentValues value = new ContentValues();
		value.put(ID, result.getId());
		value.put(THEME, result.getTheme());
		value.put(SUMMARY, result.getSummary());
		value.put(ACTION, result.getAction());
		value.put(TO, result.getTo());
		value.put(FLAG, 0);
		value.put(STARTTIME, TimeUtils.getLongFromDateTime(result.getStarttime()));
		value.put(ENDTIME, TimeUtils.getLongFromDateTime(result.getEndtime()));

		mDb.insert(TABLE_NAME, null, value);
	}

	//获取要推送的内容
	public List<PushInfoItem> getPushDatas(Context context, long time) throws Exception {
		List<PushInfoItem> list = null;
		PushInfoItem item = null;
		deleteOutDatedData(time);
		Cursor cursor = mDb.query(TABLE_NAME,
				new String[] { ID,
						THEME,
						SUMMARY,
						ACTION,
						TO,
						STARTTIME,
						ENDTIME },
				PushDataAdapter.STARTTIME + " <= ? <= " + PushDataAdapter.ENDTIME, new String[]{time+""},
				null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			list = new ArrayList<PushInfoItem>();
			while (cursor.moveToNext()) {
				item = new PushInfoItem();
				item.setId(cursor.getInt(0));
				item.setTheme(cursor.getString(1));
				item.setSummary(cursor.getString(2));
				item.setAction(cursor.getInt(3));
				item.setTo(cursor.getString(4));
				list.add(item);
			}
			cursor.close();
		}
		return list;
	}

	private boolean isExistPush(int pushId){
		Cursor cursor = mDb.query(TABLE_NAME,
				new String[] { ID},
				PushDataAdapter.ID + " = ? ", new String[]{pushId+""},
				null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.close();
            return true;
		}
		return false;
	}

	//删除过时的数据
	private void deleteOutDatedData(long time) throws Exception {
		Cursor cursor = mDb.query(TABLE_NAME,
				new String[] {ID},
				PushDataAdapter.ENDTIME + " < ? ", new String[]{time+""},
				null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				delete(cursor.getInt(0));
			}
		}
	}

    //删除数据库相应记录
	public void delete(int id) {
		mDb.delete(TABLE_NAME,
				PushDataAdapter.ID+ "=?",
				new String[] { id + "" });
	}

    //删除所有数据
	public void deleteAll() {
		mDb.delete(TABLE_NAME, null,null);
	}


	public void updateFlag(int pushId, int flag) {
		ContentValues values = new ContentValues();
		values.put(PushDataAdapter.FLAG, flag);
		mDb.update(TABLE_NAME, values,
				PushDataAdapter.ID + "=?",
				new String[]{pushId + ""});
	}
}
