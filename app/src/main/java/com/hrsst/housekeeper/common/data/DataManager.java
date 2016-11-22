package com.hrsst.housekeeper.common.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.hrsst.housekeeper.common.global.NpcCommon;


import java.util.ArrayList;
import java.util.List;

public class DataManager {
	public static final String TAG = "NpcData";
	public static final String DataBaseName = "NpcDatabase.db";
	public static final int DataBaseVersion = 27;

	/*
	 * MessageDB
	 */

	// 查找消息 parameter: 1.Context 2.当前登录用户 3. 对方ID
	public static synchronized List<Message> findMessageByActiveUserAndChatId(
			Context context, String activeUserId, String chatId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			MessageDB messageDB = new MessageDB(db);
			List<Message> list = messageDB.findMessageByActiveUserAndChatId(
					activeUserId, chatId);
			db.close();
			return list;
		}
	}

	// 插入消息 parameter: 1.Context 2.Message
	public static synchronized void insertMessage(Context context, Message msg) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			MessageDB messageDB = new MessageDB(db);
			messageDB.insert(msg);
			db.close();
		}
	}

	// 清空消息 parameter: 1.Context 2.当前登录用户 3.对方ID
	public static synchronized void clearMessageByActiveUserAndChatId(
			Context context, String activeUserId, String chatId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			MessageDB messageDB = new MessageDB(db);
			messageDB.deleteByActiveUserAndChatId(activeUserId, chatId);
			db.close();
		}
	}

	// 更新消息状态 parameter: 1.Context 2.消息临时标记 3.消息状态
	public static synchronized void updateMessageStateByFlag(Context context,
			String msgFlag, int msgState) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			MessageDB messageDB = new MessageDB(db);
			messageDB.updateStateByFlag(msgFlag, String.valueOf(msgState));
			db.close();
		}
	}

	/*
	 * SysMessageDB
	 */

	// 查找系统消息 parameter: 1.Context 2.当前登录用户
	public static synchronized List<SysMessage> findSysMessageByActiveUser(
			Context context, String activeUserId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			SysMessageDB sysMessageDB = new SysMessageDB(db);
			List<SysMessage> lists = sysMessageDB
					.findByActiveUserId(activeUserId);
			db.close();
			return lists;
		}
	}

	// 插入系统消息 parameter: 1.Context 2.SysMessage
	public static synchronized void insertSysMessage(Context context,
			SysMessage msg) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			SysMessageDB sysMessageDB = new SysMessageDB(db);
			sysMessageDB.insert(msg);
			db.close();
		}
	}

	// 删除系统消息 parameter: 1.Context 2.主键ID
	public static synchronized void deleteSysMessage(Context context, int id) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			SysMessageDB sysMessageDB = new SysMessageDB(db);
			sysMessageDB.delete(id);
			db.close();
		}
	}

	// 更新系统消息状态 parameter: 1.Context 2.主键ID 3.状态
	public static synchronized void updateSysMessageState(Context context,
			int id, int state) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			SysMessageDB sysMessageDB = new SysMessageDB(db);
			sysMessageDB.updateSysMsgState(id, state);
			db.close();
		}
	}

	/*
	 * AlarmMaskDB
	 */

	// 查找报警屏蔽账号 parameter: 1.Context 2.当前登录用户
	public static synchronized List<AlarmMask> findAlarmMaskByActiveUser(
			Context context, String activeUserId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			AlarmMaskDB alarmMaskDB = new AlarmMaskDB(db);
			List<AlarmMask> lists = alarmMaskDB
					.findByActiveUserId(activeUserId);
			db.close();
			return lists;
		}
	}

	// 插入报警屏蔽账号 parameter: 1.Context 2.AlarmMask
	public static synchronized void insertAlarmMask(Context context,
			AlarmMask alarmMask) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			AlarmMaskDB alarmMaskDB = new AlarmMaskDB(db);
			alarmMaskDB.insert(alarmMask);
			db.close();
		}
	}

	// 删除报警屏蔽账号 parameter: 1.Context 2.当前登录用户 3.设备ID
	public static synchronized void deleteAlarmMask(Context context,
			String activeUserId, String deviceId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			AlarmMaskDB alarmMaskDB = new AlarmMaskDB(db);
			alarmMaskDB.deleteByActiveUserAndDeviceId(activeUserId, deviceId);
			db.close();
		}
	}

	/*
	 * AlarmRecordDB
	 */

	// 查找报警记录 parameter: 1.Context 2.当前登录用户
	public static synchronized List<AlarmRecord> findAlarmRecordByActiveUser(
			Context context, String activeUserId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			AlarmRecordDB alarmRecordDB = new AlarmRecordDB(db);
			List<AlarmRecord> lists = alarmRecordDB
					.findByActiveUserId(activeUserId);
			db.close();
			return lists;
		}
	}

	// 插入报警记录 parameter: 1.Context 2.AlarmRecord
	public static synchronized void insertAlarmRecord(Context context,
			AlarmRecord alarmRecord) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			AlarmRecordDB alarmRecordDB = new AlarmRecordDB(db);
			alarmRecordDB.insert(alarmRecord);
			db.close();
		}
	}

	// 删除报警记录 parameter: 1.Context 2.主键id
	public static synchronized void deleteAlarmRecordById(Context context,
			int id) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			AlarmRecordDB alarmRecordDB = new AlarmRecordDB(db);
			alarmRecordDB.deleteById(id);
			db.close();
		}
	}

	// 清空报警记录 parameter: 1.Context 2.当前登录用户
	public static synchronized void clearAlarmRecord(Context context,
			String activeUserId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			AlarmRecordDB alarmRecordDB = new AlarmRecordDB(db);
			alarmRecordDB.deleteByActiveUser(activeUserId);
			db.close();
		}
	}

	// 根据报警ID，报警时间查询报警信息 parameter: 1.Context 2.AlarmRecord
	public static synchronized AlarmRecord findAlarmRecordByDeviceIdAndTime(Context context,
			String activeUserId,String deviceId,String alarmTime) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			AlarmRecordDB alarmRecordDB = new AlarmRecordDB(db);
			AlarmRecord alarmRecord=alarmRecordDB.findByActiveUserIdAndDeviceId(activeUserId, deviceId, alarmTime);
			db.close();
			return alarmRecord;
		}
	}
	//更新报警信息
	public static synchronized void updateAlarmRecord(Context context,
			AlarmRecord alarmRecord) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			AlarmRecordDB alarmDB = new AlarmRecordDB(db);
			alarmDB.update(alarmRecord);
			db.close();
		}
	}


	/*
	 * NearlyTellDB
	 */

	// 查找最近通话记录 parameter: 1.Context 2.当前登录用户
	public static synchronized List<NearlyTell> findNearlyTellByActiveUser(
			Context context, String activeUserId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			NearlyTellDB nearlyTellDB = new NearlyTellDB(db);
			List<NearlyTell> lists = nearlyTellDB
					.findByActiveUserId(activeUserId);
			db.close();
			return lists;
		}
	}

	// 查找最近通话记录 parameter: 1.Context 2.当前登录用户 3.通话ID
	public static synchronized List<NearlyTell> findNearlyTellByActiveUserAndTellId(
			Context context, String activeUserId, String tellId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			NearlyTellDB nearlyTellDB = new NearlyTellDB(db);
			List<NearlyTell> lists = nearlyTellDB.findByActiveUserIdAndTellId(
					activeUserId, tellId);
			db.close();
			return lists;
		}
	}

	// 插入最近通话记录 parameter: 1.Context 2.NearlyTell
	public static synchronized void insertNearlyTell(Context context,
			NearlyTell nearlyTell) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			NearlyTellDB nearlyTellDB = new NearlyTellDB(db);
			nearlyTellDB.insert(nearlyTell);
			db.close();
		}
	}

	// 删除最近通话记录 parameter: 1.Context 2.主键id
	public static synchronized void deleteNearlyTellById(Context context, int id) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			NearlyTellDB nearlyTellDB = new NearlyTellDB(db);
			nearlyTellDB.deleteById(id);
			db.close();
		}
	}

	// 删除最近通话记录 parameter: 1.Context 2.通话ID
	public static synchronized void deleteNearlyTellByTellId(Context context,
			String tellId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			NearlyTellDB nearlyTellDB = new NearlyTellDB(db);
			nearlyTellDB.deleteByTellId(tellId);
			db.close();
		}
	}

	// 清空最近通话记录 parameter: 1.Context 2.当前登录用户
	public static synchronized void clearNearlyTell(Context context,
			String activeUserId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			NearlyTellDB nearlyTellDB = new NearlyTellDB(db);
			nearlyTellDB.deleteByActiveUserId(activeUserId);
			db.close();
		}
	}

	/*
	 * ContactDB
	 */

	// 查找联系人 parameter: 1.Context 2.当前登录用户
	public static synchronized List<Contact> findContactByActiveUser(
			Context context, String activeUserId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContactDB contactDB = new ContactDB(db);
			List<Contact> lists = contactDB.findByActiveUserId(activeUserId);
			db.close();
			return lists;
		}
	}

	// 查找联系人 parameter: 1.Context 2.当前登录用户 3.联系人Id
	public static synchronized Contact findContactByActiveUserAndContactId(
			Context context, String activeUserId, String contactId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContactDB contactDB = new ContactDB(db);
			List<Contact> lists = contactDB.findByActiveUserIdAndContactId(
					activeUserId, contactId);
			db.close();
			if (lists.size() > 0) {
				return lists.get(0);
			} else {
				return null;
			}
		}
	}

	// 插入联系人 parameter: 1.Context 2.Contact
	public static synchronized void insertContact(Context context,
			Contact contact) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContactDB contactDB = new ContactDB(db);
			contactDB.insert(contact);
			db.close();
		}
	}

	// 更新联系人 parameter: 1.Context 2.Contact
	public static synchronized void updateContact(Context context,
			Contact contact) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContactDB contactDB = new ContactDB(db);
			contactDB.update(contact);
			db.close();
		}
	}

	// 删除联系人 parameter: 1.Context 2.当前登录用户 3.联系人ID
	public static synchronized void deleteContactByActiveUserAndContactId(
			Context context, String activeUserId, String contactId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContactDB contactDB = new ContactDB(db);
			contactDB.deleteByActiveUserIdAndContactId(activeUserId, contactId);
			db.close();
		}
	}

	// 删除联系人 parameter: 1.Context 2.主键id
	public static synchronized void deleteContactById(Context context, int id) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContactDB contactDB = new ContactDB(db);
			contactDB.deleteById(id);
			db.close();
		}
	}

	/*
	 * APContact
	 */

	// 查找联系人 parameter: 1.Context 2.当前登录用户 3.联系人Id
	public static synchronized APContact findAPContactByActiveUserAndContactId(
			Context context, String activeUserId, String contactId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			APContactDB apcontactDB = new APContactDB(db);
			APContact apcontact = apcontactDB.findAPContatctByAPname(
					activeUserId, contactId);
			db.close();
			return apcontact;
		}
	}

	// 插入联系人 parameter: 1.Context 2.Contact
	public static synchronized void insertAPContact(Context context,
			APContact apcontact) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			APContactDB apcontactDB = new APContactDB(db);
			apcontactDB.insert(apcontact);
			db.close();
		}
	}

	// 更新联系人 parameter: 1.Context 2.Contact
	public static synchronized void updateAPContact(Context context,
			APContact apcontact) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			APContactDB apcontactDB = new APContactDB(db);
			apcontactDB.update(apcontact);
			db.close();
		}
	}

	// 删除联系人 parameter: 1.Context 2.当前登录用户 3.联系人ID
	public static synchronized void deleteAPContactByActiveUserAndContactId(
			Context context, String activeUserId, String contactId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			APContactDB apcontactDB = new APContactDB(db);
			apcontactDB.deleteByActiveUserIdAndContactId(activeUserId,
					contactId);
			db.close();
		}
	}

	public static boolean isAPContactExist(Context context,
			String activeUserId, String contactId) {
		return findAPContactByActiveUserAndContactId(context, activeUserId,
				contactId) == null ? false : true;
	}

	/*
	 * JAContact
	 */
	// 查找GWID对应JAID
	public static synchronized JAContact findJAContactByActiveUserAndContactId(
			Context context, String activeUserId, String contactId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			JAContactDB apcontactDB = new JAContactDB(db);
			JAContact apcontact = apcontactDB.findByActiveUserIdAndContactId(
					activeUserId, contactId);
			db.close();
			return apcontact;
		}
	}

	// 插入GWID对应JAID
	public static synchronized void insertJAContact(Context context,
			JAContact apcontact) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			JAContactDB apcontactDB = new JAContactDB(db);
			long s = apcontactDB.insert(apcontact);
			db.close();
		}
	}

	// 更新GWID对应JAID
	public static synchronized void updateJAContact(Context context,
			JAContact apcontact) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			JAContactDB apcontactDB = new JAContactDB(db);
			apcontactDB.update(apcontact);
			db.close();
		}
	}

	// 删除联系人 parameter: 1.Context 2.当前登录用户 3.联系人ID
	public static synchronized void deleteJAContactByActiveUserAndContactId(
			Context context, String activeUserId, String contactId) {
		synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			JAContactDB apcontactDB = new JAContactDB(db);
			apcontactDB.deleteByActiveUserIdAndContactId(activeUserId,
					contactId);
			db.close();
		}
	}

	//查看是否登陆过
	public static synchronized boolean fingIsLoginByActiveUser(Context context,String userId){
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			IsLoginDB loginDB = new IsLoginDB(db);
			boolean isLogin=loginDB.getActiveUserIsLogin(userId);
			db.close();
			return isLogin;
	}
	//第一次登陆时，把登陆账号存起来
	public static synchronized void insertIsLogin(Context context,String userId){
		 synchronized (DataManager.class) {
			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
					DataBaseVersion);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			IsLoginDB loginDB = new IsLoginDB(db);
			loginDB.insert(userId);
			db.close();
		 }
   }
    //查询系统消息
		public static synchronized List<SystemMsg> findSystemMessgeByActiveUser(Context context,String userId){
			synchronized (DataManager.class) {
				List<SystemMsg> list=new ArrayList<SystemMsg>();
				DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
						DataBaseVersion);
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				SystemMessageDB messgeDB = new SystemMessageDB(db);
				list=messgeDB.getSystemMessageByActiveUser(userId);
				db.close();
				return list;
			}
	}
      //添加系统消息   
		public static synchronized void insertSystemMessage(Context context, SystemMsg msg){
			synchronized (DataManager.class) {
				DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
						DataBaseVersion);
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				SystemMessageDB messgeDB = new SystemMessageDB(db);
				messgeDB.insert(msg);
				db.close();
			}
		}
		public static synchronized String findLastMsgIdSystemMessage(Context context,String userId){
			synchronized (DataManager.class) {
				DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
						DataBaseVersion);
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				SystemMessageDB messgeDB = new SystemMessageDB(db);
				String msgId=messgeDB.findLastMsgIdByActiveUserId(userId);
				db.close();
				return msgId;
			}
		}
		//精品推荐信息标记已经读了的
		public static synchronized void UpdateSystemMessageIsRead(Context context,int id){
		  		DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
		  				DataBaseVersion);
		  		SQLiteDatabase db = dbHelper.getWritableDatabase();
		  		SystemMessageDB systemDB=new SystemMessageDB(db);
		  		systemDB.updateIsRead(id);
		  		db.close();
		}
		 // 删除精品推荐信息parameter: 1.Context 2.主键id
	 	public static synchronized void deleteSystemMessageById(Context context,
	 			int id) {
	 		synchronized (DataManager.class) {
	 			DBHelper dbHelper = new DBHelper(context, DataBaseName, null,
	 					DataBaseVersion);
	 			SQLiteDatabase db = dbHelper.getWritableDatabase();
	 			SystemMessageDB systemDB = new SystemMessageDB(db);
	 			systemDB.deleteSystemMessge(id);
	 			db.close();
	 		}
	 	}
	 	/**
		 * 插入预置位
		 * @param context
		 * @param point
		 */
		public static synchronized void insertPrepoint(Context context,String deviceId,Prepoint point) {
			synchronized (DataManager.class) {
				DBHelper dbHelper = new DBHelper(context, DataBaseName, null, DataBaseVersion);
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				PrepointDB prepointdo = new PrepointDB(db);
				prepointdo.insert(NpcCommon.mThreeNum,deviceId,point);
				db.close();
			}
		}

		/**
		 * 查找所有预置位
		 * @param context
		 * @param deviceID
		 * @return
		 */
		public static synchronized Prepoint findPrepointByDevice(Context context,String deviceID) {
			synchronized (DataManager.class) {
				DBHelper dbHelper = new DBHelper(context, DataBaseName, null, DataBaseVersion);
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				PrepointDB prepointdo = new PrepointDB(db);
				Prepoint lists = prepointdo.findAllPrePointByDeviceID(NpcCommon.mThreeNum,deviceID);
				db.close();
				return lists;
			}
		}

		/**
		 * 删除预置位
		 * @param context
		 * @param deviceID
		 * @return
		 */
		public static synchronized long deletePrepoint(Context context, String deviceID) {
			synchronized (DataManager.class) {
				DBHelper dbHelper = new DBHelper(context, DataBaseName, null, DataBaseVersion);
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				PrepointDB prepointdo = new PrepointDB(db);
				long result = prepointdo.deleteByDeviceIDAndPrepoint(NpcCommon.mThreeNum, deviceID);
				db.close();
				return result;
			}
		}

		/**
		 * 更新预置位
		 * @param context
		 * @param point 预置位
		 * @return
		 */
		public static synchronized long upDataPrepoint(Context context,String deviceID, Prepoint point) {
			synchronized (DataManager.class) {
				DBHelper dbHelper = new DBHelper(context, DataBaseName, null, DataBaseVersion);
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				PrepointDB prepointdo = new PrepointDB(db);
				long result = prepointdo.updataPrepoint(NpcCommon.mThreeNum, deviceID, point);
				db.close();
				return result;
			}
		}

		/**
		 * 更新预置位
		 * @param context
		 * @param point 预置位编号
		 * @return
		 */
		public static synchronized long upDataPrepointByCount(Context context,String deviceID, int point) {
			synchronized (DataManager.class) {
				DBHelper dbHelper = new DBHelper(context, DataBaseName, null, DataBaseVersion);
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				PrepointDB prepointdo = new PrepointDB(db);
				long result = prepointdo.updataPrepointByCount(NpcCommon.mThreeNum,deviceID,point);
				db.close();
				return result;
			}
		}
}
