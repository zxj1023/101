package tran.com.android.tapla.gamecenter.market.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import tran.com.android.tapla.gamecenter.datauiapi.bean.Categorie;
import tran.com.android.tapla.gamecenter.datauiapi.bean.PushInfoItem;
import tran.com.android.tapla.gamecenter.market.util.FileLog;
import tran.com.android.tapla.gamecenter.market.util.TimeUtils;

public class CategoryListDataAdapter extends DBAdapter {

	public static final String TAG = "CategoryListDataAdapter";
	public static final String TABLE_NAME = "tbl_categoryList";
	public static final  String ID =  "id";
	public static final  String  TITLE = "title";
	public static final  String  TYPE = "type";
	public static final  String ICON = "icon";

	private DBOpenHelper mDBOpenHelper;
	private SQLiteDatabase mDb;
	private Context mContext;

	public CategoryListDataAdapter(Context context) {
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


	public void insert(List<Categorie> result) {
		mDb.beginTransaction();

		try {
			for (Categorie item : result) {
				insert(item);
			}
			mDb.setTransactionSuccessful();
		} catch (Exception e) {
			FileLog.e(TAG, e.toString());
		} finally {
			mDb.endTransaction();
		}
	}


	public void insert(Categorie result) {
		ContentValues value = new ContentValues();
		value.put(ID, result.getId());
		value.put(TITLE, result.getTitle());

		//我们talpa暂时还木有用到这俩个字段值，所以不保存，需要用时在打开
//		value.put(TYPE, result.getType());
//		value.put(ICON, result.getIcon());

		mDb.insert(TABLE_NAME, null, value);
	}



    //删除数据库相应记录
	public void delete(int id) {
		mDb.delete(TABLE_NAME,
				CategoryListDataAdapter.ID+ "=?",
				new String[] { id + "" });
	}

    //删除所有数据
	public void deleteAll() {
		mDb.delete(TABLE_NAME, null,null);
	}

	public String getTitle(int categoryId) {
		Cursor cursor = mDb.query(TABLE_NAME,
				new String[]{
						CategoryListDataAdapter.ID,
						CategoryListDataAdapter.TITLE},
				CategoryListDataAdapter.ID + "=?",
				new String[]{categoryId + ""}, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			String title = cursor.getString(1);
			cursor.close();
			return title;
		}
		cursor.close();
		return "";
	}

	public boolean isHasData() {
		Cursor cursor = mDb.query(TABLE_NAME,
				new String[]{
						CategoryListDataAdapter.ID},
				null, null, null, null, null);
		if (cursor != null && cursor.getCount()>0) {
			return true;
		}
		cursor.close();
		return false;
	}

}
