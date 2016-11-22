package com.hrsst.housekeeper.common.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JAContactDB {
	public static final String TABLE_NAME = "jacontact";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_ID_DATA_TYPE = "integer PRIMARY KEY AUTOINCREMENT";

	public static final String COLUMN_CONTACT_NAME = "gwid";
	public static final String COLUMN_CONTACT_NAME_DATA_TYPE = "varchar";

	public static final String COLUMN_CONTACT_ID = "jaid";
	public static final String COLUMN_CONTACT_ID_DATA_TYPE = "varchar";

	public static final String COLUMN_CONTACT_PASSWORD = "pwd";
	public static final String COLUMN_CONTACT_PASSWORD_DATA_TYPE = "varchar";

	public static final String COLUMN_CONTACT_USER = "username";
	public static final String COLUMN_CONTACT_USER_DATA_TYPE = "varchar";

	public static final String COLUMN_PORT = "port";
	public static final String COLUMN_PORT_DATA_TYPE = "integer";

	public static final String COLUMN_ACTIVE_USER = "activeUser";
	public static final String COLUMN_ACTIVE_USER_DATA_TYPE = "varchar";

	public static final String COLUMN_ACTIVE_USERPWD = "reserve";
	public static final String COLUMN_ACTIVE_USERPWD_DATA_TYPE = "varchar";
	
	public static final String COLUMN_CHANNEL = "channl";
	public static final String COLUMN_CHANNEL_DATA_TYPE = "integer";

	private SQLiteDatabase myDatabase;

	public JAContactDB(SQLiteDatabase myDatabase) {
		this.myDatabase = myDatabase;
	}

	public static String getDeleteTableSQLString() {
		return SqlHelper.formDeleteTableSqlString(TABLE_NAME);
	}

	public static String getCreateTableString() {
		HashMap<String, String> columnNameAndType = new HashMap<String, String>();
		columnNameAndType.put(COLUMN_ID, COLUMN_ID_DATA_TYPE);
		columnNameAndType.put(COLUMN_CONTACT_NAME,
				COLUMN_CONTACT_NAME_DATA_TYPE);
		columnNameAndType.put(COLUMN_CONTACT_ID, COLUMN_CONTACT_ID_DATA_TYPE);
		columnNameAndType.put(COLUMN_CONTACT_PASSWORD,
				COLUMN_CONTACT_PASSWORD_DATA_TYPE);
		columnNameAndType.put(COLUMN_CONTACT_USER,
				COLUMN_CONTACT_USER_DATA_TYPE);
		columnNameAndType.put(COLUMN_PORT, COLUMN_PORT_DATA_TYPE);
		columnNameAndType.put(COLUMN_ACTIVE_USER, COLUMN_ACTIVE_USER_DATA_TYPE);
		columnNameAndType.put(COLUMN_ACTIVE_USERPWD,
				COLUMN_ACTIVE_USERPWD_DATA_TYPE);
		columnNameAndType.put(COLUMN_CHANNEL,
				COLUMN_CHANNEL_DATA_TYPE);
		String mSQLCreateWeiboInfoTable = SqlHelper.formCreateTableSqlString(
				TABLE_NAME, columnNameAndType);
		return mSQLCreateWeiboInfoTable;
	}

	public long insert(JAContact contact) {
		long resultId = 0;
		if (contact != null) {
			ContentValues values = new ContentValues();
			values.put(COLUMN_CONTACT_NAME, contact.getGwid());
			values.put(COLUMN_CONTACT_ID, contact.getJaid());
			values.put(COLUMN_CONTACT_PASSWORD, contact.getPwd());
			values.put(COLUMN_CONTACT_USER, contact.getUser());
			values.put(COLUMN_PORT, contact.getPort());
			values.put(COLUMN_ACTIVE_USER, contact.getActiveUser());
			values.put(COLUMN_ACTIVE_USERPWD, "0");
			values.put(COLUMN_CHANNEL, contact.getChannl());
			try {
				resultId = myDatabase.insertOrThrow(TABLE_NAME, null, values);
			} catch (SQLiteConstraintException e) {
				e.printStackTrace();
			}
		}
		return resultId;
	}

	public void update(JAContact contact) {

		ContentValues values = new ContentValues();
		values.put(COLUMN_CONTACT_NAME, contact.getGwid());
		values.put(COLUMN_CONTACT_ID, contact.getJaid());
		values.put(COLUMN_CONTACT_PASSWORD, contact.getPwd());
		values.put(COLUMN_CONTACT_USER, contact.getUser());
		values.put(COLUMN_PORT, contact.getPort());
		values.put(COLUMN_ACTIVE_USER, contact.getActiveUser());
		values.put(COLUMN_ACTIVE_USERPWD, "0");
		values.put(COLUMN_CHANNEL, contact.getChannl());
		try {
			myDatabase.update(TABLE_NAME, values, COLUMN_ACTIVE_USER
					+ "=? AND " + COLUMN_CONTACT_ID + "=?", new String[] {
					contact.getActiveUser(), contact.getJaid() });
		} catch (SQLiteConstraintException e) {
			e.printStackTrace();
		}

	}

	public JAContact findByActiveUserIdAndContactId(String activeUserId,
			String ContactId) {
		List<JAContact> lists = new ArrayList<JAContact>();
		Cursor cursor = null;
		cursor = myDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "
				+ COLUMN_ACTIVE_USER + "=? AND " + COLUMN_CONTACT_NAME + "=?",
				new String[] { activeUserId, ContactId });
		if (cursor != null) {
			while (cursor.moveToNext()) {
				int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
				String contactName = cursor.getString(cursor
						.getColumnIndex(COLUMN_CONTACT_NAME));
				String contactId = cursor.getString(cursor
						.getColumnIndex(COLUMN_CONTACT_ID));
				String contactPassword = cursor.getString(cursor
						.getColumnIndex(COLUMN_CONTACT_PASSWORD));
				String contactType = cursor.getString(cursor
						.getColumnIndex(COLUMN_CONTACT_USER));
				int messageCount = cursor.getInt(cursor
						.getColumnIndex(COLUMN_PORT));
				String activeUser = cursor.getString(cursor
						.getColumnIndex(COLUMN_ACTIVE_USER));
				String userpwd = cursor.getString(cursor
						.getColumnIndex(COLUMN_ACTIVE_USERPWD));
				int channl = cursor.getInt(cursor
						.getColumnIndex(COLUMN_CHANNEL));
				JAContact data = new JAContact();
				data.setId(id);
				data.setGwid(contactName);
				data.setJaid(contactId);
				data.setPwd(contactPassword);
				data.setUser(contactType);
				data.setPort(messageCount) ;
				data.setActiveUser(activeUser);
				data.setChannl(channl);
				lists.add(data);
			}
			cursor.close();
		}
		if(lists.size()>0){
			return lists.get(0);
		}
		return null;
	}

	public int deleteById(int id) {
		return myDatabase.delete(TABLE_NAME, COLUMN_ID + "=?",
				new String[] { String.valueOf(id) });
	}

	public int deleteByActiveUserIdAndContactId(String activeUserId,
			String contactId) {
		return myDatabase.delete(TABLE_NAME, COLUMN_ACTIVE_USER + "=?"
				+ " AND " + COLUMN_CONTACT_ID + "=?", new String[] {
				activeUserId, contactId });
	}
}
