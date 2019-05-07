package com.tran.com.android.gc.update.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.tran.com.android.gc.update.db.AutoUpdateDao;

public class ForceUpProvider extends ContentProvider {

    AutoUpdateDao forceUpdateDao;

    @Override
    public int delete(Uri arg0, String arg1, String[] arg2) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getType(Uri arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri arg0, ContentValues arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean onCreate() {
        final Context context = getContext();
        forceUpdateDao = new AutoUpdateDao(context);

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor cursor = forceUpdateDao.query(null,
                "packageName=?", selectionArgs, null);

        if (cursor.moveToNext()) {
            if (cursor.getInt(15) == 1) {
                return cursor;
            }
        }
        return null;
    }

    @Override
    public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
        return 0;
    }

}
