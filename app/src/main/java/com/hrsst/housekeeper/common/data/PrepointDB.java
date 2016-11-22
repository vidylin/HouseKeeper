package com.hrsst.housekeeper.common.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dxs on 2015/9/9.
 */
public class PrepointDB {

    public static final String TABLE_NAME = "prepoint";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ID_DATA_TYPE = "integer PRIMARY KEY AUTOINCREMENT";

    public static final String COLUMN_DEVICEID = "deviceId";
    public static final String COLUMN_DEVICEID_DATA_TYPE = "varchar";

    public static final String COLUMN_ACTIVE_USER = "activeUser";
    public static final String COLUMN_ACTIVE_USER_DATA_TYPE = "varchar";

    public static final String COLUMN_NICKNAME0 = "nickname0";
    public static final String COLUMN_NICKNAME0_DATA_TYPE = "varchar";
    public static final String COLUMN_NICKNAME1 = "nickname1";
    public static final String COLUMN_NICKNAME1_DATA_TYPE = "varchar";
    public static final String COLUMN_NICKNAME2 = "nickname2";
    public static final String COLUMN_NICKNAME2_DATA_TYPE = "varchar";
    public static final String COLUMN_NICKNAME3 = "nickname3";
    public static final String COLUMN_NICKNAME3_DATA_TYPE = "varchar";
    public static final String COLUMN_NICKNAME4 = "nickname4";
    public static final String COLUMN_NICKNAME4_DATA_TYPE = "varchar";


    // -------------------------------------
    private SQLiteDatabase myDatabase;

    public PrepointDB(SQLiteDatabase myDatabase) {
        this.myDatabase = myDatabase;
    }

    public static String getDeleteTableSQLString() {
        return SqlHelper.formDeleteTableSqlString(TABLE_NAME);
    }

    public static String getCreateTableString() {
        HashMap<String, String> columnNameAndType = new HashMap<String, String>();
        columnNameAndType.put(COLUMN_ID, COLUMN_ID_DATA_TYPE);
        columnNameAndType.put(COLUMN_DEVICEID, COLUMN_DEVICEID_DATA_TYPE);
        columnNameAndType.put(COLUMN_ACTIVE_USER, COLUMN_ACTIVE_USER_DATA_TYPE);

        columnNameAndType.put(COLUMN_NICKNAME0, COLUMN_NICKNAME0_DATA_TYPE);
        columnNameAndType.put(COLUMN_NICKNAME1, COLUMN_NICKNAME1_DATA_TYPE);
        columnNameAndType.put(COLUMN_NICKNAME2, COLUMN_NICKNAME2_DATA_TYPE);
        columnNameAndType.put(COLUMN_NICKNAME3, COLUMN_NICKNAME3_DATA_TYPE);
        columnNameAndType.put(COLUMN_NICKNAME4, COLUMN_NICKNAME4_DATA_TYPE);
        // ------------------------------
        String mSQLCreateWeiboInfoTable = SqlHelper.formCreateTableSqlString(
                TABLE_NAME, columnNameAndType);
        return mSQLCreateWeiboInfoTable;
    }

    /**
     * 插入一个预置位
     *
     * @param point
     * @return
     */
    public long insert(String activeUser, String deviceId,Prepoint point) {
        long isResut = -1;
        if (point != null) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ACTIVE_USER, activeUser);
            values.put(COLUMN_DEVICEID, deviceId);

            values.put(COLUMN_NICKNAME0, point.nickName0);
            values.put(COLUMN_NICKNAME1, point.nickName1);
            values.put(COLUMN_NICKNAME2, point.nickName2);
            values.put(COLUMN_NICKNAME3, point.nickName3);
            values.put(COLUMN_NICKNAME4, point.nickName4);

            try {
                isResut = myDatabase.insertOrThrow(TABLE_NAME, null, values);
            } catch (SQLiteConstraintException e) {
                e.printStackTrace();
            }
        }
        return isResut;
    }

    /**
     * 查找设备的预置位
     *
     * @param activeUser
     * @param deviceId
     * @return
     */
    public Prepoint findAllPrePointByDeviceID(String activeUser, String deviceId) {
        List<Prepoint> prepoints = new ArrayList<Prepoint>();
        String[] whereArgs = {activeUser, deviceId};
        Cursor cursor = null;
        cursor = myDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "
                + COLUMN_ACTIVE_USER + "=? and " + COLUMN_DEVICEID + "=?", whereArgs);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));

                String name0 = cursor.getString(cursor
                        .getColumnIndex(COLUMN_NICKNAME0));
                String name1 = cursor.getString(cursor
                        .getColumnIndex(COLUMN_NICKNAME1));
                String name2 = cursor.getString(cursor
                        .getColumnIndex(COLUMN_NICKNAME2));
                String name3 = cursor.getString(cursor
                        .getColumnIndex(COLUMN_NICKNAME3));
                String name4 = cursor.getString(cursor
                        .getColumnIndex(COLUMN_NICKNAME4));

                Prepoint data = new Prepoint();
                data.id = id;
                data.nickName0 = name0;
                data.nickName1 = name1;
                data.nickName2 = name2;
                data.nickName3 = name3;
                data.nickName4 = name4;
                Log.i("dxsprepoint",data.nickName0+"--"+data.nickName1+"--"+data.nickName2+"--"+data.nickName3+"--"+data.nickName4);
                prepoints.add(data);
            }
            cursor.close();
        }
        return prepoints.size()>0?prepoints.get(0):null;
    }

    /**
     * 更新预置位名称
     *
     * @param point
     * @return
     */
    public Long updataPrepointByCount(String activeUser, String deviceId,int point) {
        long isResut = -1;
        if (point>=0&&point<5) {
            ContentValues values = new ContentValues();
            Prepoint po=new Prepoint();
            String[] whereArgs = {activeUser, deviceId};
            switch (point){
                case 0:
                    values.put(COLUMN_NICKNAME0,po.getName(point));
                    break;
                case 1:
                    values.put(COLUMN_NICKNAME1,po.getName(point));
                    break;
                case 2:
                    values.put(COLUMN_NICKNAME2,po.getName(point));
                    break;
                case 3:
                    values.put(COLUMN_NICKNAME3,po.getName(point));
                    break;
                case 4:
                    values.put(COLUMN_NICKNAME4,po.getName(point));
                    break;
            }
            try {
                isResut = myDatabase.update(TABLE_NAME, values,
                        COLUMN_ACTIVE_USER + "=? and " + COLUMN_DEVICEID
                                + "=?", whereArgs);
            } catch (SQLiteConstraintException e) {
                e.printStackTrace();
            }
        }
        return isResut;
    }

    /**
     * 更新预置位名称
     *
     * @param point
     * @return
     */
    public Long updataPrepoint(String activeUser, String deviceId,Prepoint point) {
        long isResut = -1;
        if (point != null) {
            ContentValues values = new ContentValues();
            String[] whereArgs = {activeUser, deviceId};
            values.put(COLUMN_NICKNAME0, point.nickName0);
            values.put(COLUMN_NICKNAME1, point.nickName1);
            values.put(COLUMN_NICKNAME2, point.nickName2);
            values.put(COLUMN_NICKNAME3, point.nickName3);
            values.put(COLUMN_NICKNAME4, point.nickName4);
            try {
                isResut = myDatabase.update(TABLE_NAME, values,
                        COLUMN_ACTIVE_USER + "=? and " + COLUMN_DEVICEID
                                + "=?", whereArgs);
            } catch (SQLiteConstraintException e) {
                e.printStackTrace();
            }
        }
        return isResut;
    }

    /**
     * 删除一个预置位
     *
     * @param activeUser
     * @param deviceID
     * @return
     */
    public int deleteByDeviceIDAndPrepoint(String activeUser, String deviceID) {
        return myDatabase.delete(TABLE_NAME, COLUMN_ACTIVE_USER + "=? and " + COLUMN_DEVICEID
                        + "=?",
                new String[]{activeUser, deviceID});
    }
}
