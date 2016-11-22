package com.hrsst.housekeeper.common.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;

public class IsLoginDB {
	public static final String TABLE_NAME="is_login";
	public static final String COLUMN_ID="id";
	public static final String COLUMN_ID_DATA_TYPE="integer PRIMARY KEY AUTOINCREMENT";
	public static final String COLUMN_ACTIVE_USER = "activeUser";
	public static final String COLUMN_ACTIVE_USER_DATA_TYPE = "varchar";
	private SQLiteDatabase myDatabase;
	public IsLoginDB(SQLiteDatabase myDatabase){
		this.myDatabase=myDatabase;
	}
	public static String getDeleteTableSQLString() {
		return SqlHelper.formDeleteTableSqlString(TABLE_NAME);
	}

	public static String getCreateTableString() {
		HashMap<String, String> columnNameAndType = new HashMap<String, String>();
		columnNameAndType.put(COLUMN_ID, COLUMN_ID_DATA_TYPE);
		columnNameAndType.put(COLUMN_ACTIVE_USER,COLUMN_ACTIVE_USER_DATA_TYPE);
		String mSQLCreateWeiboInfoTable = SqlHelper.formCreateTableSqlString(
				TABLE_NAME, columnNameAndType);
		return mSQLCreateWeiboInfoTable;
	}
	public long insert(String active_user) {
		long isResut = -1;
		if (active_user != null&&!active_user.equals("")) {
			ContentValues values = new ContentValues();
			values.put(COLUMN_ACTIVE_USER,active_user);
			try {
				isResut = myDatabase.insertOrThrow(TABLE_NAME, null, values);
			} catch (SQLiteConstraintException e) {
				e.printStackTrace();
			}
		}
		return isResut;
	}
	public boolean getActiveUserIsLogin(String activeUserId){
		//升序asc
		boolean isLogin=false;
		Cursor cursor = null;
		cursor = myDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "
				+ COLUMN_ACTIVE_USER + "=?", new String[] { activeUserId });
		if (cursor != null) {
			while (cursor.moveToNext()) {
				int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
				String active_user= cursor.getString(cursor
						.getColumnIndex(COLUMN_ACTIVE_USER));
				isLogin=true;
			}
			cursor.close();
		}
		return isLogin;
	}

}
