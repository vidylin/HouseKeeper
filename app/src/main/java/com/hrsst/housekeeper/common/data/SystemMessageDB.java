package com.hrsst.housekeeper.common.data;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

public class SystemMessageDB {
	public static final String TABLE_NAME="system_msg";
	public static final String COLUMN_ID="id";
	public static final String COLUMN_ID_DATA_TYPE="integer PRIMARY KEY AUTOINCREMENT";
	public static final String COLUMN_MSGINDEX="msgindex";
	public static final String COLUMN_MSGINDEX_DATA_TYPE="varchar";
	public static final String COLUMN_TITLE="sys_title";
	public static final String COLUMN_TITLE_DATA_TYPE="varchar";
	public static final String COLUMN_COTENT="sys_content";
	public static final String COLUMN_COTENT_DATA_TYPE="varchar";
	public static final String COLUMN_TIME="sys_time";
	public static final String COLUMN_TIME_DATA_TYPE="varchar";
	public static final String COLUMN_PICTURE="sys_picture";
	public static final String COLUMN_PICTURE_DATA_TYPE="varchar";
	public static final String COLUMN_URL="sys_url";
	public static final String COLUMN_URL_DATA_TYPE="varchar"; 
	public static final String COLUMN_ACTIVE_USER = "activeUser";
	public static final String COLUMN_ACTIVE_USER_DATA_TYPE = "varchar";
	public static final String COLUMN_IS_READ = "isRead";
	public static final String COLUMN_IS_READ_DATA_TYPE ="integer";
	private SQLiteDatabase myDatabase;
	public SystemMessageDB(SQLiteDatabase myDatabase){
		this.myDatabase=myDatabase;
	}
	public static String getDeleteTableSQLString() {
		return SqlHelper.formDeleteTableSqlString(TABLE_NAME);
	}

	public static String getCreateTableString() {
		HashMap<String, String> columnNameAndType = new HashMap<String, String>();
		columnNameAndType.put(COLUMN_ID, COLUMN_ID_DATA_TYPE);
		columnNameAndType.put(COLUMN_MSGINDEX, COLUMN_MSGINDEX_DATA_TYPE);
		columnNameAndType.put(COLUMN_TITLE,COLUMN_TITLE_DATA_TYPE);
		columnNameAndType.put(COLUMN_COTENT, COLUMN_COTENT_DATA_TYPE);
		columnNameAndType.put(COLUMN_TIME, COLUMN_TIME_DATA_TYPE);
		columnNameAndType.put(COLUMN_PICTURE,COLUMN_PICTURE_DATA_TYPE);
		columnNameAndType.put(COLUMN_URL, COLUMN_URL_DATA_TYPE);
		columnNameAndType.put(COLUMN_ACTIVE_USER,COLUMN_ACTIVE_USER_DATA_TYPE);
		columnNameAndType.put(COLUMN_IS_READ,COLUMN_IS_READ_DATA_TYPE);
		String mSQLCreateWeiboInfoTable = SqlHelper.formCreateTableSqlString(
				TABLE_NAME, columnNameAndType);
		return mSQLCreateWeiboInfoTable;
	}

	public long insert(SystemMsg systemMessage) {
		long isResut = -1;
		if (systemMessage != null) {
			ContentValues values = new ContentValues();
			values.put(COLUMN_MSGINDEX, systemMessage.msgId);
			values.put(COLUMN_TITLE, systemMessage.title);
			values.put(COLUMN_COTENT, systemMessage.content);
			values.put(COLUMN_TIME, systemMessage.time);
			values.put(COLUMN_PICTURE, systemMessage.pictrue_url);
			values.put(COLUMN_URL, systemMessage.url);
			values.put(COLUMN_ACTIVE_USER, systemMessage.active_user);
			values.put(COLUMN_IS_READ, systemMessage.isRead);
			try {
				isResut = myDatabase.insertOrThrow(TABLE_NAME, null, values);
			} catch (SQLiteConstraintException e) {
				e.printStackTrace();
			}
		}
		return isResut;
	}
	public  List<SystemMsg> getSystemMessageByActiveUser(String activeUserId){
		//升序asc
		List<SystemMsg> lists = new ArrayList<SystemMsg>();
		Cursor cursor = null;
		cursor = myDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "
				+ COLUMN_ACTIVE_USER + "=?" + " order by "+COLUMN_TIME+" desc ", new String[] { activeUserId });
		if (cursor != null) {
			while (cursor.moveToNext()) {
				int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
				String msgId = cursor.getString(cursor
						.getColumnIndex(COLUMN_MSGINDEX));
				String title = cursor.getString(cursor
						.getColumnIndex(COLUMN_TITLE));
				String content = cursor.getString(cursor
						.getColumnIndex(COLUMN_COTENT));
				String time = cursor.getString(cursor
						.getColumnIndex(COLUMN_TIME));
				String pictrue = cursor.getString(cursor
						.getColumnIndex(COLUMN_PICTURE));
				String url = cursor.getString(cursor
						.getColumnIndex(COLUMN_URL));
				String active_user = cursor.getString(cursor
						.getColumnIndex(COLUMN_ACTIVE_USER));
				int isRead=cursor.getInt(cursor
						.getColumnIndex(COLUMN_IS_READ));
				SystemMsg data = new SystemMsg();
				data.id = id;
				data.msgId = msgId;
				data.title =title;
				data.content =content;
				data.time = time;
				data.pictrue_url = pictrue;
				data.url= url;
				data.active_user=active_user;
				data.isRead=isRead;
				lists.add(data);
			}
			cursor.close();
		}
		return lists;
	}
	public String findLastMsgIdByActiveUserId(String userId) {
		Cursor cursor = null;
		String msgId=null;
//		cursor = myDatabase.rawQuery("SELECT * FROM ("+"SELECT * FROM " + TABLE_NAME+ " order by " + COLUMN_TIME + " desc" + " WHERE "
//				+ COLUMN_ACTIVE_USER + "=?"+ ")" +" WHERE rownum=1" , new String[] { userId });
		cursor = myDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "
				+ COLUMN_ACTIVE_USER + "=?" + " order by "+COLUMN_TIME+" asc ", new String[] { userId });
		if (cursor != null) {
			    if(cursor.moveToLast()){
			    	msgId = cursor.getString(cursor.getColumnIndex(COLUMN_MSGINDEX));
			    }
			cursor.close();
		}
		return msgId;
	}
	public void updateIsRead(int id){
		  ContentValues values = new ContentValues();
          values.put(COLUMN_IS_READ, 1);
          try {
  			myDatabase.update(TABLE_NAME, values, COLUMN_ID + "=?",
  					new String[] { String.valueOf(id) });
  		 } catch (SQLiteConstraintException e) {
  			e.printStackTrace();
  		 }
	}
	public String findMsgIdByPictrueUrl(String pictureUrl){
		Cursor cursor = null;
		String msgId=null;
		cursor = myDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "
				+ COLUMN_PICTURE + "=?", new String[] { pictureUrl });
		if (cursor != null) {
			    if(cursor.moveToNext()){
			    	msgId = cursor.getString(cursor.getColumnIndex(COLUMN_MSGINDEX));
			    }
			cursor.close();
		}
		return msgId;
	}
	public int deleteSystemMessge(int id){
		return myDatabase.delete(TABLE_NAME, COLUMN_ID + "=?",
				new String[] { String.valueOf(id) });
	}
}
