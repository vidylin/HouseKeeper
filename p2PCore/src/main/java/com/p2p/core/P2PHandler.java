package com.p2p.core;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.p2p.core.P2PInterface.IP2P;
import com.p2p.core.P2PInterface.ISetting;
import com.p2p.core.global.Config;
import com.p2p.core.global.Constants;
import com.p2p.core.utils.DES;
import com.p2p.core.utils.MyUtils;
import com.p2p.core.utils.RtspThread;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * P2P浜や簰甯姪绫� 鍦ㄦ绫讳腑鍖呰闇�瑕佷笌璁惧閫氫俊鐨勬暟鎹紝閫氳繃JNI浼犵粰璁惧
 */
public class P2PHandler {
	String TAG = "SDK";

	private static int MSG_ID_SETTING_DEVICE_TIME = Constants.MsgSection.MSG_ID_SETTING_DEVICE_TIME;
	private static int MSG_ID_GETTING_DEVICE_TIME = Constants.MsgSection.MSG_ID_GETTING_DEVICE_TIME;

	private static int MSG_ID_GETTING_NPC_SETTINGS = Constants.MsgSection.MSG_ID_GETTING_NPC_SETTINGS;
	private static int MSG_ID_SET_REMOTE_DEFENCE = Constants.MsgSection.MSG_ID_SET_REMOTE_DEFENCE;
	private static int MSG_ID_SET_REMOTE_RECORD = Constants.MsgSection.MSG_ID_SET_REMOTE_RECORD;
	private static int MSG_ID_SETTING_NPC_SETTINGS_VIDEO_FORMAT = Constants.MsgSection.MSG_ID_SETTING_NPC_SETTINGS_VIDEO_FORMAT;
	private static int MSG_ID_SETTING_NPC_SETTINGS_VIDEO_VOLUME = Constants.MsgSection.MSG_ID_SETTING_NPC_SETTINGS_VIDEO_VOLUME;
	private static int MSG_ID_SETTING_NPC_SETTINGS_BUZZER = Constants.MsgSection.MSG_ID_SETTING_NPC_SETTINGS_BUZZER;
	private static int MSG_ID_SETTING_NPC_SETTINGS_MOTION = Constants.MsgSection.MSG_ID_SETTING_NPC_SETTINGS_MOTION;
	private static int MSG_ID_SETTING_NPC_SETTINGS_RECORD_TYPE = Constants.MsgSection.MSG_ID_SETTING_NPC_SETTINGS_RECORD_TYPE;
	private static int MSG_ID_SETTING_NPC_SETTINGS_RECORD_TIME = Constants.MsgSection.MSG_ID_SETTING_NPC_SETTINGS_RECORD_TIME;
	private static int MSG_ID_SETTING_NPC_SETTINGS_RECORD_PLAN_TIME = Constants.MsgSection.MSG_ID_SETTING_NPC_SETTINGS_RECORD_PLAN_TIME;
	private static int MSG_ID_SETTING_NPC_SETTINGS_NET_TYPE = Constants.MsgSection.MSG_ID_SETTING_NPC_SETTINGS_NET_TYPE;

	private static int MSG_ID_SETTING_ALARM_EMAIL = (int) (Constants.MsgSection.MSG_ID_SETTING_ALARM_EMAIL + Math
			.random() * 999);
	private static int MSG_ID_GETTING_ALARM_EMAIL = (int) (Constants.MsgSection.MSG_ID_GETTING_ALARM_EMAIL + Math
			.random() * 999);

	private static int MSG_ID_SETTING_ALARM_BIND_ID = Constants.MsgSection.MSG_ID_SETTING_ALARM_BIND_ID;
	private static int MSG_ID_GETTING_ALARM_BIND_ID = Constants.MsgSection.MSG_ID_GETTING_ALARM_BIND_ID;

	private static int MSG_ID_SETTING_INIT_PASSWORD = Constants.MsgSection.MSG_ID_SETTING_INIT_PASSWORD;
	private static int MSG_ID_SETTING_DEVICE_PASSWORD = Constants.MsgSection.MSG_ID_SETTING_DEVICE_PASSWORD;
	private static int MSG_ID_CHECK_DEVICE_PASSWORD = Constants.MsgSection.MSG_ID_CHECK_DEVICE_PASSWORD;

	private static int MSG_ID_SETTING_DEFENCEAREA = Constants.MsgSection.MSG_ID_SETTING_DEFENCEAREA;
	private static int MSG_ID_GETTING_DEFENCEAREA = Constants.MsgSection.MSG_ID_GETTING_DEFENCEAREA;

	private static int MSG_ID_SETTING_WIFI = Constants.MsgSection.MSG_ID_SETTING_WIFI;
	private static int MSG_ID_GETTING_WIFI_LIST = Constants.MsgSection.MSG_ID_GETTING_WIFI_LIST;

	private static int MSG_ID_GETTING_RECORD_FILE_LIST = Constants.MsgSection.MSG_ID_GETTING_RECORD_FILE_LIST;
	private static int MSG_ID_SEND_MESSAGE = Constants.MsgSection.MSG_ID_SEND_MESSAGE;
	private static int MSG_ID_SEND_CUSTOM_CMD = Constants.MsgSection.MSG_ID_SEND_CUSTOM_CMD;
	private static int MSG_ID_CHECK_DEVICE_UPDATE = Constants.MsgSection.MSG_ID_CHECK_DEVICE_UPDATE;
	private static int MSG_ID_CANCEL_DEVICE_UPDATE = Constants.MsgSection.MSG_ID_CANCEL_DEVICE_UPDATE;
	private static int MSG_ID_DO_DEVICE_UPDATE = Constants.MsgSection.MSG_ID_DO_DEVICE_UPDATE;
	private static int MSG_ID_GET_DEFENCE_STATE = Constants.MsgSection.MSG_ID_GET_DEFENCE_STATE;
	private static int MSG_ID_GET_DEVICE_VERSION = Constants.MsgSection.MSG_ID_GET_DEVICE_VERSION;
	private static int MSG_ID_CLEAR_DEFENCE_GROUP = Constants.MsgSection.MSG_ID_CLEAR_DEFENCE_GROUP;
	private static int MESG_ID_STTING_PIC_REVERSE = Constants.MsgSection.MESG_ID_STTING_PIC_REVERSE;
	private static int MESG_ID_STTING_IR_ALARM_EN = Constants.MsgSection.MESG_ID_STTING_IR_ALARM_EN;
	private static int MESG_STTING_ID_EXTLINE_ALARM_IN_EN = Constants.MsgSection.MESG_STTING_ID_EXTLINE_ALARM_IN_EN;
	private static int MESG_STTING_ID_EXTLINE_ALARM_OUT_EN = Constants.MsgSection.MESG_STTING_ID_EXTLINE_ALARM_OUT_EN;
	private static int MESG_STTING_ID_SECUPGDEV = Constants.MsgSection.MESG_STTING_ID_SECUPGDEV;
	private static int MESG_STTING_ID_GUEST_PASSWD = Constants.MsgSection.MESG_STTING_ID_GUEST_PASSWD;
	private static int MESG_STTING_ID_TIMEZONE = Constants.MsgSection.MESG_STTING_ID_TIMEZONE;
	private static int MESG_GET_SD_CARD_CAPACITY = Constants.MsgSection.MESG_GET_SD_CARD_CAPACITY;
	private static int MESG_SD_CARD_FORMAT = Constants.MsgSection.MESG_SD_CARD_FORMAT;
	private static int MESG_SET_GPIO = Constants.MsgSection.MESG_SET_GPIO;
	private static int MESG_SET_GPI1_0 = Constants.MsgSection.MESG_SET_GPI1_0;
	private static int MESG_SET_PRE_RECORD = Constants.MsgSection.MESG_SET_PRE_RECORD;
	private static int MESG_GET_DEFENCE_AREA_SWITCH = Constants.MsgSection.MESG_GET_DEFENCE_AREA_SWITCH;
	private static int MESG_SET_DEFENCE_AREA_SWITCH = Constants.MsgSection.MESG_SET_DEFENCE_AREA_SWITCH;
	private static int MESG_SET_LAMP = Constants.MsgSection.MESG_SET_LAMP;
	private static int MESG_GET_LAMP = Constants.MsgSection.MESG_GET_LAMP;
	private static int SET_USER_DEFINE_MESG = Constants.MsgSection.SET_USER_DEFINE_MESG;
	private static int MESG_PRESET_MOTOR_POS = Constants.MsgSection.MESG_PRESET_MOTOR_POS;
	private static int MESG_GET_PRESET_MOTOR_POS = Constants.MsgSection.MESG_GET_PRESET_MOTOR_POS;
	private static int MESG_SET_PRESET_MOTOR_POS = Constants.MsgSection.MESG_SET_PRESET_MOTOR_POS;
	private static int MESG_GET_ALARM_CENTER_PARAMETER = Constants.MsgSection.MESG_GET_ALARM_CENTER_PARAMETER;
	private static int MESG_SET_ALARM_CENTER_PARAMETER = Constants.MsgSection.MESG_SET_ALARM_CENTER_PARAMETER;
	private static int MESG_DELETE_ALARMID = Constants.MsgSection.MESG_DELETE_DEVICEALARMID;
	private static int MESG_SET_SYSTEM_MESSAGE_INDEX = Constants.MsgSection.MESG_SET_SYSTEM_MESSAGE_INDEX;
	private static int CONTROL_CAMERA = Constants.MsgSection.CONTROL_CAMERA;
	private static int MESG_SET_RECEIVE_DOOBELL = Constants.MsgSection.MESG_SET_RECEIVE_DOOBELL;
	private static int MESG_GET_LANGUEGE = Constants.MsgSection.MESG_GET_LANGUEGE;
	private static int MESG_SET_LANGUEGE = Constants.MsgSection.MESG_SET_LANGUEGE;
	private static int MESG_TYPE_GET_LAN_IPC_LIST = Constants.MsgSection.MESG_TYPE_GET_LAN_IPC_LIST;
	private static int MESG_TYPE_SET_AP_MODE_CHANGE = Constants.MsgSection.MESG_TYPE_SET_AP_MODE_CHANGE;
	private static int MESG_TYPE_GET_NVRINFO = Constants.MsgSection.MESG_TYPE_GET_NVRINFO;
	private static int MESG_TYPE_SET_ZOOM=Constants.MsgSection.MESG_TYPE_SET_ZOOM;
	private static int MESG_TYPE_GET_FOCUS_ZOOM=Constants.MsgSection.MESG_TYPE_GET_FOCUS_ZOOM;
	private static int MESG_TYPE_SET_FOCUS_ZOOM=Constants.MsgSection.MESG_TYPE_SET_FOCUS_ZOOM;
	private static P2PHandler manager = null;

	private static int IP_CONFIG = Constants.MsgSection.IP_CONFIG;
	
	private static int MESG_GET_GPIO=Constants.MsgSection.MESG_GET_GPIO;
	
	private static int MESG_GET_DEFENCE_WORK_GROUP=Constants.MsgSection.MESG_GET_DEFENCE_WORK_GROUP;
	private static int MESG_SET_DEFENCE_WORK_GROUP=Constants.MsgSection.MESG_SET_DEFENCE_WORK_GROUP;
	
	private static int MESG_GET_FTP_CONFIG_INFO=Constants.MsgSection.MESG_GET_FTP_CONFIG_INFO;
	private static int MESG_SET_FTP_CONFIG_INFO=Constants.MsgSection.MESG_SET_FTP_CONFIG_INFO;

	private P2PHandler() {
	}

	;

	public synchronized static P2PHandler getInstance() {
		if (null == manager) {
			synchronized (P2PHandler.class) {
				manager = new P2PHandler();
			}
		}
		return manager;
	}

	/**
	 * 鍒濆鍖�
	 */
	public void p2pInit(Context context, IP2P p2pListener,
			ISetting settingListener) {
		// new MediaPlayer();
		MediaPlayer.getInstance().setP2PInterface(p2pListener);
		MediaPlayer.getInstance().setSettingInterface(settingListener);
	}

	/**
	 * p2p杩炴帴
	 */
	public boolean p2pConnect(String activeUser, int codeStr1, int codeStr2) {
		// final String cHostName="|6sci.com|6sci.com.cn";
		// final String cHostName =
		// "|cloudlinks.cn|2cu.co|gwelltimes.com|cloud-links.net";
		final String cHostName = "|p2p1.cloudlinks.cn|p2p3.cloud-links.net|p2p2.cloudlinks.cn|p2p4.cloud-links.net|p2p5.cloudlinks.cn|p2p6.cloudlinks.cn|p2p7.cloudlinks.cn|p2p8.cloudlinks.cn|p2p9.cloudlinks.cn|p2p10.cloudlinks.cn";
//		final String cHostName = "|104.250.135.115";
		//		final String cHostName = "|p2p1.cloudlinks.cn|p2p3.cloud-links.net|p2p2.cloudlinks.cn|p2p4.cloud-links.net";
		// final String cHostName = "|videoipcamera.cn|videoipcamera.com";
		// if (MediaPlayer.getInstance().native_p2p_connect(
		// Integer.parseInt(activeUser)|0x80000000, 886976412, codeStr1,
		// codeStr2,cHostName.getBytes() ) == 1) {
		// return true;
		// } else {
		// return false;
		// }
		int connect;
		int[] customers = Config.AppConfig.CustomerIDs;
		int[] customer_ids = new int[10];
		if (customers.length < 10) {
			for (int i = 0; i < customers.length; i++) {
				customer_ids[i] = customers[i];
			}
			for (int j = customers.length; j < 10; j++) {
				customer_ids[j] = 0;
			}
		} else {
			customer_ids = customers;
		}
		if (activeUser.equals("517401")) {
			connect = MediaPlayer.getInstance().native_p2p_connect(
					Integer.parseInt(activeUser), 886976412, codeStr1,
					codeStr2, cHostName.getBytes(), customer_ids);
		} else {
			connect = MediaPlayer.getInstance().native_p2p_connect(
					Integer.parseInt(activeUser) | 0x80000000, 886976412,
					codeStr1, codeStr2, cHostName.getBytes(), customer_ids);
		}
		if (connect == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * p2p鏂紑
	 */
	public void p2pDisconnect() {
		Log.e("leleTest", "p2pDisconnect");
		MediaPlayer.getInstance().native_p2p_disconnect();
		Log.e("leleTest", "p2pDisconnect after");
	}

	/**
	 * 鑾峰彇WIFI鍒楄〃
	 */
	public void getWifiList(String contactId, String password) {
		Log.e(TAG, "P2PHANDLER:getWifiList");
		if (this.MSG_ID_GETTING_WIFI_LIST >= (Constants.MsgSection.MSG_ID_GETTING_WIFI_LIST)) {
			this.MSG_ID_GETTING_WIFI_LIST = Constants.MsgSection.MSG_ID_GETTING_WIFI_LIST - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iGetNPCWifiList(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MSG_ID_GETTING_WIFI_LIST);
		this.MSG_ID_GETTING_WIFI_LIST++;
	}

	/**
	 * 璁剧疆WIFI
	 */
	public void setWifi(String contactId, String password, int type,
			String name, String wifiPassword) {
		Log.e(TAG, "P2PHANDLER:setWifi");
		String s = null;
		byte[] bt;
		try {
			bt = name.getBytes("UTF-8");
			for (int i = 0; i < bt.length; i++) {
				s = s + "  " + bt[i];
			}
			Log.e("setwifiname", "--" + s);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (this.MSG_ID_SETTING_WIFI >= (Constants.MsgSection.MSG_ID_SETTING_WIFI)) {
			this.MSG_ID_SETTING_WIFI = Constants.MsgSection.MSG_ID_SETTING_WIFI - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iSetNPCWifi(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MSG_ID_SETTING_WIFI, type,
				name.getBytes(), name.length(), wifiPassword.getBytes(),
				wifiPassword.length());
		this.MSG_ID_SETTING_WIFI++;
	}

	/**
	 * 鑾峰彇NPC鍚勭璁剧疆
	 */
	public void getNpcSettings(String contactId, String password) {
		Log.e(TAG, "P2PHANDLER:getNpcSettings");
		String passwordStr = EntryPassword(password);
		int iPassword = Integer.MAX_VALUE;
		try {
			iPassword = Integer.parseInt(passwordStr);
		} catch (Exception e) {
			iPassword = Integer.MAX_VALUE;
		}

		if (this.MSG_ID_GETTING_NPC_SETTINGS >= (Constants.MsgSection.MSG_ID_GETTING_NPC_SETTINGS)) {
			this.MSG_ID_GETTING_NPC_SETTINGS = Constants.MsgSection.MSG_ID_GETTING_NPC_SETTINGS - 1000;
		}

		MediaPlayer.iGetNPCSettings(Integer.parseInt(contactId), iPassword,
				this.MSG_ID_GETTING_NPC_SETTINGS);
		this.MSG_ID_GETTING_NPC_SETTINGS++;
	}

	/**
	 * 鑾峰彇甯冩斁鐘舵��
	 */
	public void getDefenceStates(String contactId, String password) {
		Log.e(TAG, "P2PHANDLER:getDefenceStates");
		String passwordStr = EntryPassword(password);
		int iPassword = Integer.MAX_VALUE;
		try {
			iPassword = Integer.parseInt(passwordStr);
		} catch (Exception e) {
			iPassword = Integer.MAX_VALUE;
		}
		Log.e("defence", Integer.parseInt(contactId) + "--" + iPassword
				+ "---------------");
		if (this.MSG_ID_GET_DEFENCE_STATE >= (Constants.MsgSection.MSG_ID_GET_DEFENCE_STATE)) {
			this.MSG_ID_GET_DEFENCE_STATE = Constants.MsgSection.MSG_ID_GET_DEFENCE_STATE - 1000;
		}

		MediaPlayer.iGetNPCSettings(Integer.parseInt(contactId), iPassword,
				this.MSG_ID_GET_DEFENCE_STATE);
		this.MSG_ID_GET_DEFENCE_STATE++;
	}

	/**
	 * 妫�鏌ュ瘑鐮�
	 */
	public void checkPassword(String contactId, String password) {
		Log.e(TAG, "P2PHANDLER:checkPassword");
		if (this.MSG_ID_CHECK_DEVICE_PASSWORD >= (Constants.MsgSection.MSG_ID_CHECK_DEVICE_PASSWORD)) {
			this.MSG_ID_CHECK_DEVICE_PASSWORD = Constants.MsgSection.MSG_ID_CHECK_DEVICE_PASSWORD - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iGetNPCSettings(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MSG_ID_CHECK_DEVICE_PASSWORD);
		this.MSG_ID_CHECK_DEVICE_PASSWORD++;
	}

	/**
	 * 鑾峰彇闃插尯璁剧疆
	 */
	public void getDefenceArea(String contactId, String password) {
		Log.e(TAG, "P2PHANDLER:getDefenceArea");
		String passwordStr = EntryPassword(password);
		if (this.MSG_ID_GETTING_DEFENCEAREA >= (Constants.MsgSection.MSG_ID_GETTING_DEFENCEAREA)) {
			this.MSG_ID_GETTING_DEFENCEAREA = Constants.MsgSection.MSG_ID_GETTING_DEFENCEAREA - 1000;
		}

		MediaPlayer.iGetAlarmCodeStatus(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MSG_ID_GETTING_DEFENCEAREA);
		this.MSG_ID_GETTING_DEFENCEAREA++;
	}

	/**
	 * 璁剧疆杩滅▼甯冮槻
	 */
	public void setRemoteDefence(String contactId, String password, int value) {
		Log.e(TAG, "P2PHANDLER:setRemoteDefence");
		String passwordStr = EntryPassword(password);
		if (this.MSG_ID_SET_REMOTE_DEFENCE >= (Constants.MsgSection.MSG_ID_SET_REMOTE_DEFENCE)) {
			this.MSG_ID_SET_REMOTE_DEFENCE = Constants.MsgSection.MSG_ID_SET_REMOTE_DEFENCE - 1000;
		}
		MediaPlayer.iSetNPCSettings(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MSG_ID_SET_REMOTE_DEFENCE,
				Constants.P2P_SETTING.SETTING_TYPE.SETTING_REMOTE_DEFENCE,
				value);
		this.MSG_ID_SET_REMOTE_DEFENCE++;
	}

	// public void SetSystemMessageIndex(int iSystemMessageType,
	// int iSystemMessageIndex) {
	// MediaPlayer.SetSystemMessageIndex(iSystemMessageType,
	// iSystemMessageIndex);
	// }

	public void vSendWiFiCmd(int iType, byte[] SSID, int iSSIDLen,
			byte[] Password, int iPasswordLen) {
		MediaPlayer.vSendWiFiCmd(iType, SSID, iSSIDLen, Password, iPasswordLen);
	}

	/**
	 * 璁剧疆杩滅▼褰曞儚
	 */
	public void setRemoteRecord(String contactId, String password, int value) {
		Log.e(TAG, "P2PHANDLER:setRemoteRecord");
		if (this.MSG_ID_SET_REMOTE_RECORD >= (Constants.MsgSection.MSG_ID_SET_REMOTE_RECORD)) {
			this.MSG_ID_SET_REMOTE_RECORD = Constants.MsgSection.MSG_ID_SET_REMOTE_RECORD - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer
				.iSetNPCSettings(
						Integer.parseInt(contactId),
						Integer.parseInt(passwordStr),
						this.MSG_ID_SET_REMOTE_RECORD,
						Constants.P2P_SETTING.SETTING_TYPE.SETTING_REMOTE_RECORD,
						value);
		this.MSG_ID_SET_REMOTE_RECORD++;
	}

	/**
	 * 璁剧疆璁惧鏃堕棿
	 */
	public void setDeviceTime(String contactId, String password, String time) {
		Log.e(TAG, "P2PHANDLER:setDeviceTime");
		if (this.MSG_ID_SETTING_DEVICE_TIME >= (Constants.MsgSection.MSG_ID_SETTING_DEVICE_TIME)) {
			this.MSG_ID_SETTING_DEVICE_TIME = Constants.MsgSection.MSG_ID_SETTING_DEVICE_TIME - 1000;
		}
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		Date date = null;
		try {
			date = df.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int parseTime = 0;

		if (null != date) {

			if (time.substring(11, 13).equals("12")) {
				parseTime = ((calendar.get(Calendar.YEAR) - 2000) << 24)
						| (((calendar.get(Calendar.MONTH) + 1) << 18))
						| (calendar.get(Calendar.DAY_OF_MONTH) << 12)
						| (12 << 6) | (calendar.get(Calendar.MINUTE) << 0);
			} else {
				parseTime = ((calendar.get(Calendar.YEAR) - 2000) << 24)
						| (((calendar.get(Calendar.MONTH) + 1) << 18))
						| (calendar.get(Calendar.DAY_OF_MONTH) << 12)
						| (calendar.get(Calendar.HOUR_OF_DAY) << 6)
						| (calendar.get(Calendar.MINUTE) << 0);
			}

		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iSetNPCDateTime(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MSG_ID_SETTING_DEVICE_TIME,
				parseTime);
		this.MSG_ID_SETTING_DEVICE_TIME++;
	}

	/**
	 * 鑾峰彇璁惧鏃堕棿
	 */
	public void getDeviceTime(String contactId, String password) {
		Log.e(TAG, "P2PHANDLER:getDeviceTime");
		if (this.MSG_ID_GETTING_DEVICE_TIME >= (Constants.MsgSection.MSG_ID_GETTING_DEVICE_TIME)) {
			this.MSG_ID_GETTING_DEVICE_TIME = Constants.MsgSection.MSG_ID_GETTING_DEVICE_TIME - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iGetNPCDateTime(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MSG_ID_GETTING_DEVICE_TIME);
		this.MSG_ID_GETTING_DEVICE_TIME++;
	}

	/**
	 * 璁剧疆璁惧闊抽噺
	 */
	public void setVideoVolume(String contactId, String password, int value) {
		Log.e(TAG, "P2PHANDLER:setVideoVolume");
		if (this.MSG_ID_SETTING_NPC_SETTINGS_VIDEO_VOLUME >= (Constants.MsgSection.MSG_ID_SETTING_NPC_SETTINGS_VIDEO_VOLUME)) {
			this.MSG_ID_SETTING_NPC_SETTINGS_VIDEO_VOLUME = Constants.MsgSection.MSG_ID_SETTING_NPC_SETTINGS_VIDEO_VOLUME - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iSetNPCSettings(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr),
				this.MSG_ID_SETTING_NPC_SETTINGS_VIDEO_VOLUME,
				Constants.P2P_SETTING.SETTING_TYPE.SETTING_VOLUME, value);
		this.MSG_ID_SETTING_NPC_SETTINGS_VIDEO_VOLUME++;
	}

	/**
	 * 璁剧疆瑙嗛鏍煎紡
	 */
	public void setVideoFormat(String contactId, String password, int value) {
		Log.e(TAG, "P2PHANDLER:setVideoFormat");
		if (this.MSG_ID_SETTING_NPC_SETTINGS_VIDEO_FORMAT >= (Constants.MsgSection.MSG_ID_SETTING_NPC_SETTINGS_VIDEO_FORMAT)) {
			this.MSG_ID_SETTING_NPC_SETTINGS_VIDEO_FORMAT = Constants.MsgSection.MSG_ID_SETTING_NPC_SETTINGS_VIDEO_FORMAT - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iSetNPCSettings(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr),
				this.MSG_ID_SETTING_NPC_SETTINGS_VIDEO_FORMAT,
				Constants.P2P_SETTING.SETTING_TYPE.SETTING_VIDEO_FORMAT, value);
		this.MSG_ID_SETTING_NPC_SETTINGS_VIDEO_FORMAT++;
	}

	/**
	 * 璁剧疆褰曞儚绫诲瀷
	 */
	public void setRecordType(String contactId, String password, int type) {
		Log.e(TAG, "P2PHANDLER:setRecordType");
		if (this.MSG_ID_SETTING_NPC_SETTINGS_RECORD_TYPE >= (Constants.MsgSection.MSG_ID_SETTING_NPC_SETTINGS_RECORD_TYPE)) {
			this.MSG_ID_SETTING_NPC_SETTINGS_RECORD_TYPE = Constants.MsgSection.MSG_ID_SETTING_NPC_SETTINGS_RECORD_TYPE - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iSetNPCSettings(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr),
				this.MSG_ID_SETTING_NPC_SETTINGS_RECORD_TYPE,
				Constants.P2P_SETTING.SETTING_TYPE.SETTING_RECORD_TYPE, type);
		this.MSG_ID_SETTING_NPC_SETTINGS_RECORD_TYPE++;
	}

	/**
	 * 璁剧疆褰曞儚鏃堕棿
	 */
	public void setRecordTime(String contactId, String password, int time) {
		Log.e(TAG, "P2PHANDLER:setRecordTime");
		if (this.MSG_ID_SETTING_NPC_SETTINGS_RECORD_TIME >= (Constants.MsgSection.MSG_ID_SETTING_NPC_SETTINGS_RECORD_TIME)) {
			this.MSG_ID_SETTING_NPC_SETTINGS_RECORD_TIME = Constants.MsgSection.MSG_ID_SETTING_NPC_SETTINGS_RECORD_TIME - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iSetNPCSettings(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr),
				this.MSG_ID_SETTING_NPC_SETTINGS_RECORD_TIME,
				Constants.P2P_SETTING.SETTING_TYPE.SETTING_RECORD_TIME, time);
		this.MSG_ID_SETTING_NPC_SETTINGS_RECORD_TIME++;
	}

	/**
	 * 璁剧疆褰曞儚璁″垝鏃堕棿
	 */
	public void setRecordPlanTime(String contactId, String password, String time) {
		Log.e(TAG, "P2PHANDLER:setRecordPlanTime");
		if (this.MSG_ID_SETTING_NPC_SETTINGS_RECORD_PLAN_TIME >= (Constants.MsgSection.MSG_ID_SETTING_NPC_SETTINGS_RECORD_PLAN_TIME)) {
			this.MSG_ID_SETTING_NPC_SETTINGS_RECORD_PLAN_TIME = Constants.MsgSection.MSG_ID_SETTING_NPC_SETTINGS_RECORD_PLAN_TIME - 1000;
		}
		String passwordStr = EntryPassword(password);
		int iTime = MyUtils.convertPlanTime(time);
		MediaPlayer.iSetNPCSettings(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr),
				this.MSG_ID_SETTING_NPC_SETTINGS_RECORD_PLAN_TIME,
				Constants.P2P_SETTING.SETTING_TYPE.SETTING_RECORD_PLAN_TIME,
				iTime);
		this.MSG_ID_SETTING_NPC_SETTINGS_RECORD_PLAN_TIME++;
	}

	/**
	 * 璁剧疆闃插尯鐘舵��
	 */
	public void setDefenceAreaState(String contactId, String password,
			int group, int item, int type) {
		Log.e(TAG, "P2PHANDLER:setDefenceAreaState");
		if (this.MSG_ID_SETTING_DEFENCEAREA >= (Constants.MsgSection.MSG_ID_SETTING_DEFENCEAREA)) {
			this.MSG_ID_SETTING_DEFENCEAREA = Constants.MsgSection.MSG_ID_SETTING_DEFENCEAREA - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iSetAlarmCodeStatus(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MSG_ID_SETTING_DEFENCEAREA, 1,
				type, new int[] { group }, new int[] { item });
		this.MSG_ID_SETTING_DEFENCEAREA++;
	}

	/**
	 * 娓呯┖闃插尯鐘舵��
	 */
	public void clearDefenceAreaState(String contactId, String password,
			int group) {
		Log.e(TAG, "P2PHANDLER:setDefenceAreaState");
		if (this.MSG_ID_CLEAR_DEFENCE_GROUP >= (Constants.MsgSection.MSG_ID_CLEAR_DEFENCE_GROUP)) {
			this.MSG_ID_CLEAR_DEFENCE_GROUP = Constants.MsgSection.MSG_ID_CLEAR_DEFENCE_GROUP - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iClearAlarmCodeGroup(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MSG_ID_CLEAR_DEFENCE_GROUP,
				group);

		this.MSG_ID_CLEAR_DEFENCE_GROUP++;
	}

	/**
	 * 璁剧疆缃戠粶绫诲瀷
	 */
	public void setNetType(String contactId, String password, int type) {
		Log.e(TAG, "P2PHANDLER:setNetType");
		if (this.MSG_ID_SETTING_NPC_SETTINGS_NET_TYPE >= (Constants.MsgSection.MSG_ID_SETTING_NPC_SETTINGS_NET_TYPE)) {
			this.MSG_ID_SETTING_NPC_SETTINGS_NET_TYPE = Constants.MsgSection.MSG_ID_SETTING_NPC_SETTINGS_NET_TYPE - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iSetNPCSettings(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr),
				this.MSG_ID_SETTING_NPC_SETTINGS_NET_TYPE,
				Constants.P2P_SETTING.SETTING_TYPE.SETTING_NET_TYPE, type);
		this.MSG_ID_SETTING_NPC_SETTINGS_NET_TYPE++;
	}

	/**
	 * 璁剧疆缁戝畾鎶ヨID
	 */
	public void setBindAlarmId(String contactId, String password, int count,
			String[] datas) {
		Log.e(TAG, "P2PHANDLER:setBindAlarmId");
		if (this.MSG_ID_SETTING_ALARM_BIND_ID >= (Constants.MsgSection.MSG_ID_SETTING_ALARM_BIND_ID)) {
			this.MSG_ID_SETTING_ALARM_BIND_ID = Constants.MsgSection.MSG_ID_SETTING_ALARM_BIND_ID - 1000;
		}
		int[] iData = new int[datas.length];
		try {

			for (int i = 0; i < datas.length; i++) {
				iData[i] = Integer.parseInt(datas[i]);
			}
		} catch (Exception e) {
			iData = new int[] { 0 };
			count = 1;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iSetBindAlarmId(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MSG_ID_SETTING_ALARM_BIND_ID,
				count, iData);
		this.MSG_ID_SETTING_ALARM_BIND_ID++;
	}

	/**
	 * 鑾峰彇缁戝畾鎶ヨID
	 */
	public void getBindAlarmId(String contactId, String password) {
		Log.e(TAG, "P2PHANDLER:getBindAlarmId");
		if (this.MSG_ID_GETTING_ALARM_BIND_ID >= (Constants.MsgSection.MSG_ID_GETTING_ALARM_BIND_ID)) {
			this.MSG_ID_GETTING_ALARM_BIND_ID = Constants.MsgSection.MSG_ID_GETTING_ALARM_BIND_ID - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iGetBindAlarmId(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MSG_ID_GETTING_ALARM_BIND_ID);
		this.MSG_ID_GETTING_ALARM_BIND_ID++;
	}

	/**
	 * 璁剧疆鎶ヨ閭
	 */
	public void setAlarmEmail(String contactId, String password, String email) {
		Log.e(TAG, "P2PHANDLER:setAlarmEmail");
		if (this.MSG_ID_SETTING_ALARM_EMAIL >= (Constants.MsgSection.MSG_ID_SETTING_ALARM_EMAIL)) {
			this.MSG_ID_SETTING_ALARM_EMAIL = Constants.MsgSection.MSG_ID_SETTING_ALARM_EMAIL - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iSetNPCEmail(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MSG_ID_SETTING_ALARM_EMAIL,
				email.getBytes(), email.length());
		this.MSG_ID_SETTING_ALARM_EMAIL++;
	}

	/**
	 * 璁剧疆鎶ヨ閭甯MTP鍙傛暟锛堟坊鍔�##缁撴潫鏍囪骞剁敤0琛ラ綈8鐨勬暣鏁颁綅锛�
	 * 
	 * @param contactId
	 * @param password
	 * @param boption
	 *            鎿嶄綔鏍囪
	 * @param emailaddress
	 *            鏀朵欢浜洪偖绠卞湴鍧�
	 * @param port
	 *            鍙戜欢浜虹殑閭眬SMTP绔彛鍙傛暟
	 * @param server
	 *            鍙戜欢浜虹殑閭眬SMTP鏈嶅姟鍙傛暟
	 * @param user
	 *            鍙戜欢浜洪偖绠�
	 * @param pwd
	 *            鍙戜欢浜洪偖绠卞瘑鐮�
	 * @param subject
	 *            鍙戜欢涓婚
	 * @param content
	 *            鍙戜欢閭欢鍐呭
	 */
	public void setAlarmEmailWithSMTP(String contactId, String password,
			byte boption, String emailaddress, int port, String server,
			String user, String pwd, String subject, String content,
			byte Entry, byte reserve1, int reserve2) {
		Log.e(TAG, "P2PHANDLER:setAlarmEmail");
		if (this.MSG_ID_SETTING_ALARM_EMAIL >= (Constants.MsgSection.MSG_ID_SETTING_ALARM_EMAIL)) {
			this.MSG_ID_SETTING_ALARM_EMAIL = Constants.MsgSection.MSG_ID_SETTING_ALARM_EMAIL - 1000;
		}
		String pwds = pwd + "##";
		int m = pwds.length();
		int k = 8 - m % 8;
		for (int i = 0; i < k; i++) {
			pwds = pwds + "0";
		}
		byte[] ppp = null;
		try {
			ppp = DES.des(pwds.getBytes(), 0);
		} catch (Exception e) {
			ppp = new byte[] { 0 };
			e.printStackTrace();
		}
		String passwordStr = EntryPassword(password);

		MediaPlayer.SetRobortEmailNew(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MSG_ID_SETTING_ALARM_EMAIL,
				boption, emailaddress, port, server, user, ppp, subject,
				content, Entry, (byte) reserve1, reserve2, ppp.length);
		this.MSG_ID_SETTING_ALARM_EMAIL++;
	}

	/**
	 * 鑾峰彇鎶ヨ閭
	 */
	public void getAlarmEmail(String contactId, String password) {
		Log.e(TAG, "P2PHANDLER:getAlarmEmail");
		if (this.MSG_ID_GETTING_ALARM_EMAIL >= (Constants.MsgSection.MSG_ID_GETTING_ALARM_EMAIL)) {
			this.MSG_ID_GETTING_ALARM_EMAIL = Constants.MsgSection.MSG_ID_GETTING_ALARM_EMAIL - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iGetNPCEmail(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MSG_ID_GETTING_ALARM_EMAIL);
		this.MSG_ID_GETTING_ALARM_EMAIL++;
	}

	/**
	 * 璁剧疆铚傞福鍣�
	 */
	public void setBuzzer(String contactId, String password, int value) {
		Log.e(TAG, "P2PHANDLER:setBuzzer");
		if (this.MSG_ID_SETTING_NPC_SETTINGS_BUZZER >= (Constants.MsgSection.MSG_ID_SETTING_NPC_SETTINGS_BUZZER)) {
			this.MSG_ID_SETTING_NPC_SETTINGS_BUZZER = Constants.MsgSection.MSG_ID_SETTING_NPC_SETTINGS_BUZZER - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iSetNPCSettings(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr),
				this.MSG_ID_SETTING_NPC_SETTINGS_BUZZER,
				Constants.P2P_SETTING.SETTING_TYPE.SETTING_BUZZER, value);
		this.MSG_ID_SETTING_NPC_SETTINGS_BUZZER++;
	}

	/**
	 * 璁剧疆绉诲姩渚︽祴
	 */
	public void setMotion(String contactId, String password, int value) {
		Log.e(TAG, "P2PHANDLER:setMotion");
		if (this.MSG_ID_SETTING_NPC_SETTINGS_MOTION >= (Constants.MsgSection.MSG_ID_SETTING_NPC_SETTINGS_MOTION)) {
			this.MSG_ID_SETTING_NPC_SETTINGS_MOTION = Constants.MsgSection.MSG_ID_SETTING_NPC_SETTINGS_MOTION - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iSetNPCSettings(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr),
				this.MSG_ID_SETTING_NPC_SETTINGS_MOTION,
				Constants.P2P_SETTING.SETTING_TYPE.SETTING_MOTION_DECT, value);
		this.MSG_ID_SETTING_NPC_SETTINGS_MOTION++;
	}

	/**
	 * 璁剧疆鍒濆瀵嗙爜
	 */
	public void setInitPassword(String contactId, String password,String userPassword) {
		Log.e(TAG, "P2PHANDLER:setInitPassword");
		if (this.MSG_ID_SETTING_INIT_PASSWORD >= (Constants.MsgSection.MSG_ID_SETTING_INIT_PASSWORD)) {
			this.MSG_ID_SETTING_INIT_PASSWORD = Constants.MsgSection.MSG_ID_SETTING_INIT_PASSWORD - 1000;
		}
		int pwdLen=userPassword.length();
		byte[] EntryPwd=getPwdBytes(userPassword);
		String passwordStr = EntryPassword(password);
		MediaPlayer.iSetInitPassword(Integer.parseInt(contactId), 0,
				this.MSG_ID_SETTING_INIT_PASSWORD, Integer.parseInt(passwordStr),Integer.parseInt(contactId),pwdLen,EntryPwd);
		this.MSG_ID_SETTING_INIT_PASSWORD++;
	}

	/**
	 * 璁剧疆璁惧瀵嗙爜
	 */
	public void setDevicePassword(String contactId, String oldPassword,
			String newPassword) {
		Log.e(TAG, "P2PHANDLER:setDevicePassword");
		if (this.MSG_ID_SETTING_DEVICE_PASSWORD >= (Constants.MsgSection.MSG_ID_SETTING_DEVICE_PASSWORD)) {
			this.MSG_ID_SETTING_DEVICE_PASSWORD = Constants.MsgSection.MSG_ID_SETTING_DEVICE_PASSWORD - 1000;
		}
		String oldPasswordStr = EntryPassword(oldPassword);
		String newPasswordStr = EntryPassword(newPassword);
		MediaPlayer.iSetNPCSettings(Integer.parseInt(contactId),
				Integer.parseInt(oldPasswordStr),
				this.MSG_ID_SETTING_DEVICE_PASSWORD,
				Constants.P2P_SETTING.SETTING_TYPE.SETTING_DEVICE_PWD,
				Integer.parseInt(newPasswordStr));
		this.MSG_ID_SETTING_DEVICE_PASSWORD++;
	}
	
	/**
	 * 璁剧疆璁惧瀵嗙爜,NVR涓撶敤
	 */
	public void setDevicePassword(String contactId, String oldPassword,
			String newPassword,String userPwd) {
		Log.e(TAG, "P2PHANDLER:setDevicePassword");
		String oldPasswordStr = EntryPassword(oldPassword);
		String newPasswordStr = EntryPassword(newPassword);
		if (this.MSG_ID_SETTING_DEVICE_PASSWORD >= (Constants.MsgSection.MSG_ID_SETTING_DEVICE_PASSWORD)) {
			this.MSG_ID_SETTING_DEVICE_PASSWORD = Constants.MsgSection.MSG_ID_SETTING_DEVICE_PASSWORD - 1000;
		}
		byte[] tt=new byte[32];
		int len=userPwd.length();
		byte[] ss=userPwd.getBytes();
		System.arraycopy(ss, 0, tt, 0, ss.length);
		for(int i=0;i<tt.length/8;i++){
			byte[] de=new byte[8];
			System.arraycopy(tt, i*8, de, 0, de.length);
			byte[] entry=P2PEntryPassword(de);
			System.arraycopy(entry, 0, tt, i*8, entry.length);
		}
		MediaPlayer.iSetDevicePwd(Integer.parseInt(contactId),
				Integer.parseInt(oldPasswordStr),
				this.MSG_ID_SETTING_DEVICE_PASSWORD,
				Constants.P2P_SETTING.SETTING_TYPE.SETTING_DEVICE_PWD,
				Integer.parseInt(newPasswordStr),len,tt);
		this.MSG_ID_SETTING_DEVICE_PASSWORD++;
	}

	/**
	 * 璁剧疆璁垮瀵嗙爜
	 */
	public void setDeviceVisitorPassword(String contactId, String oldPassword,
			String visitorPassword) {
		Log.e(TAG, "P2PHANDLER:setDevicePassword");
		String oldPasswordStr = EntryPassword(oldPassword);
		String newPasswordStr = EntryPassword(visitorPassword);
		if (this.MESG_STTING_ID_GUEST_PASSWD >= (Constants.MsgSection.MESG_STTING_ID_GUEST_PASSWD)) {
			this.MESG_STTING_ID_GUEST_PASSWD = Constants.MsgSection.MESG_STTING_ID_GUEST_PASSWD - 1000;
		}

		MediaPlayer.iSetNPCSettings(Integer.parseInt(contactId),
				Integer.parseInt(oldPasswordStr),
				this.MESG_STTING_ID_GUEST_PASSWD,
				Constants.P2P_SETTING.SETTING_TYPE.SETTING_ID_GUEST_PASSWD,
				Integer.parseInt(newPasswordStr));
//		MediaPlayer.iSetNPCSettings(Integer.parseInt(contactId),
//				Integer.parseInt(oldPassword),
//				this.MESG_STTING_ID_GUEST_PASSWD,
//				Constants.P2P_SETTING.SETTING_TYPE.SETTING_ID_VISITORPWD,
//				Integer.parseInt(visitorPassword));
		this.MESG_STTING_ID_GUEST_PASSWD++;
	}

	/**
	 * 鑾峰彇濂藉弸鐘舵��
	 */

	public void getFriendStatus(String[] contactIds) {
		Log.e(TAG, "P2PHANDLER:getFriendStatus");
		int[] friends = new int[contactIds.length];
		for (int i = 0; i < contactIds.length; i++) {
			if (contactIds[i].substring(0, 1).equals("0")) {
				friends[i] = Integer.parseInt(contactIds[i]) | 0x80000000;
			} else {
				friends[i] = Integer.parseInt(contactIds[i]);
			}
		}
		MediaPlayer.iGetFriendsStatus(friends, friends.length);
	}

	/**
	 * 鑾峰彇褰曞儚鍒楄〃
	 */

	public void getRecordFiles(String contactId, String password,
			int timeInterval) {
		Log.e(TAG, "P2PHANDLER:getRecordFiles");
		if (this.MSG_ID_GETTING_RECORD_FILE_LIST >= (Constants.MsgSection.MSG_ID_GETTING_RECORD_FILE_LIST)) {
			this.MSG_ID_GETTING_RECORD_FILE_LIST = Constants.MsgSection.MSG_ID_GETTING_RECORD_FILE_LIST - 1000;
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		int i_start;
		if (now.getDate() < timeInterval) {
			i_start = ((now.getYear() - 100) << 24)
					| (((now.getMonth()) << 18))
					| ((timeInterval - now.getDate()) << 12)
					| ((now.getHours()) << 6) | ((now.getMinutes()) << 0);
		} else {
			i_start = ((now.getYear() - 100) << 24)
					| (((now.getMonth() + 1) << 18))
					| ((now.getDate() - timeInterval) << 12)
					| ((now.getHours()) << 6) | ((now.getMinutes()) << 0);
		}
		int i_end = ((now.getYear() - 100) << 24)
				| (((now.getMonth() + 1) << 18)) | ((now.getDate()) << 12)
				| ((now.getHours()) << 6) | ((now.getMinutes()) << 0);
		String passwordStr = EntryPassword(password);
		MediaPlayer.iGetRecFiles(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr),
				this.MSG_ID_GETTING_RECORD_FILE_LIST, i_start, i_end);
		this.MSG_ID_GETTING_RECORD_FILE_LIST++;
	}

	/**
	 * 鑾峰彇褰曞儚鍒楄〃
	 */

	public void getRecordFiles(String contactId, String password, Date start,
			Date end) {
		Log.e(TAG, "P2PHANDLER:getRecordFiles");
		if (this.MSG_ID_GETTING_RECORD_FILE_LIST >= (Constants.MsgSection.MSG_ID_GETTING_RECORD_FILE_LIST)) {
			this.MSG_ID_GETTING_RECORD_FILE_LIST = Constants.MsgSection.MSG_ID_GETTING_RECORD_FILE_LIST - 1000;
		}

		int i_start = ((start.getYear() - 100) << 24)
				| (((start.getMonth() + 1) << 18)) | ((start.getDate()) << 12)
				| ((start.getHours()) << 6) | ((start.getMinutes()) << 0);
		int i_end = ((end.getYear() - 100) << 24)
				| (((end.getMonth() + 1) << 18)) | ((end.getDate()) << 12)
				| ((end.getHours()) << 6) | ((end.getMinutes()) << 0);
		String passwordStr = EntryPassword(password);
		MediaPlayer.iGetRecFiles(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr),
				this.MSG_ID_GETTING_RECORD_FILE_LIST, i_start, i_end);
		this.MSG_ID_GETTING_RECORD_FILE_LIST++;
	}

	/**
	 * 鍙戦�佺娑堟伅
	 */
	public String sendMessage(String contactId, String msg) {
		Log.e(TAG, "P2PHANDLER:sendMessage");
		if (this.MSG_ID_SEND_MESSAGE >= (Constants.MsgSection.MSG_ID_SEND_MESSAGE)) {
			this.MSG_ID_SEND_MESSAGE = Constants.MsgSection.MSG_ID_SEND_MESSAGE - 1000;
		}
		int iId = Integer.parseInt(contactId) | 0x80000000;
		MediaPlayer.iSendMesgToFriend(iId, this.MSG_ID_SEND_MESSAGE,
				msg.getBytes(), msg.getBytes().length);
		this.MSG_ID_SEND_MESSAGE++;
		return String.valueOf(this.MSG_ID_SEND_MESSAGE - 1);
	}

	/**
	 * 鍙戦�佽嚜瀹氫箟鍛戒护
	 */
	public String sendCustomCmd(String contactId, String password, String msg) {
		Log.e(TAG, "P2PHANDLER:sendCustomCmd");
		if (this.MSG_ID_SEND_CUSTOM_CMD >= (Constants.MsgSection.MSG_ID_SEND_CUSTOM_CMD)) {
			this.MSG_ID_SEND_CUSTOM_CMD = Constants.MsgSection.MSG_ID_SEND_CUSTOM_CMD - 1000;
		}
		String passwordStr = EntryPassword(password);
		int iId = Integer.parseInt(contactId);
		MediaPlayer.iSendCmdToFriend(iId, Integer.parseInt(passwordStr),
				this.MSG_ID_SEND_CUSTOM_CMD, msg.getBytes(),
				msg.getBytes().length);
		this.MSG_ID_SEND_CUSTOM_CMD++;
		return String.valueOf(this.MSG_ID_SEND_CUSTOM_CMD - 1);
	}

	/**
	 * 鎵撳紑闊抽璁惧骞跺噯澶囨挱鏀�
	 * 
	 * @param callType
	 *            鐩戞帶绫诲瀷
	 */
	public void openAudioAndStartPlaying(int callType) {
		try {
			MediaPlayer.getInstance().openAudioTrack();
			MediaPlayer.getInstance()._StartPlaying(
					Constants.P2P_WINDOW.P2P_SURFACE_START_PLAYING_WIDTH,
					Constants.P2P_WINDOW.P2P_SURFACE_START_PLAYING_HEIGHT,
					callType);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 鎸傛柇p2p
	 */
	public synchronized void reject() {
		synchronized (P2PHandler.this) {
			MediaPlayer.getInstance().native_p2p_hungup();
		}

	}

	/**
	 * 鎺ュ惉
	 */
	public void accept() {
		MediaPlayer.getInstance().native_p2p_accpet();
	}

	/**
	 * RTSP
	 */
	public void RTSPConnect(final String contactId, final String password,
			final boolean isOutCall, final int callType, final String callId,
			final String ipFlag, final String pushMsg, String ipAddress,
			int VideoMode, Handler rtspHandler,String headerID) {
		String passwordStr = EntryPassword(password);
		new RtspThread(ipAddress, rtspHandler, contactId, passwordStr, isOutCall,
				callType, callId, ipFlag, pushMsg, VideoMode,headerID).start();
		
	}

	/**
	 * 鍛煎彨锛堢洃鎺э級
	 * @param contactId APPID
	 * @param password 瀵嗙爜
	 * @param isOutCall 
	 * @param callType 
	 * @param callId 鍛煎彨ID
	 * @param ipFlag IP鏍囪
	 * @param pushMsg 闄勫甫娑堟伅
	 * @param VideoMode 瑙嗗睆鍒嗚鲸鐜�
	 * @param headerID 澶村儚鎴浘鍛藉悕ID
	 * @return
	 */

	public boolean call(final String contactId, final String password,
			final boolean isOutCall, final int callType, final String callId,
			final String ipFlag, final String pushMsg, int VideoMode,String headerID) {
		boolean result = false;
		long headerId=Long.parseLong(headerID);
		byte[] byt = new byte[8];
		try {
			if (isOutCall) {
				String parseNum = callId;
				if (parseNum.contains("+")) {
					boolean isPhone = false;
					for (int i = 0; i < regionCode.length; i++) {
						int cLength = String.valueOf(regionCode[i]).length();
						parseNum = parseNum.replace("+", "");
						String hight = parseNum.substring(0, cLength);
						String low = parseNum.substring(cLength,
								parseNum.length());
						if (Integer.parseInt(hight) == regionCode[i]) {
							long num = (Long.parseLong(hight) << 48 | Long
									.parseLong(low));
							parseNum = String.valueOf(num);
							isPhone = true;
							break;
						}
					}

					if (!isPhone) {
						return result;
					}
				}

				long id = Long.parseLong(parseNum);
				if (parseNum.charAt(0) == '0') {
					id = 0 - id;
				}

				int pwd = 0;

				int isMonitor = 0;
				if (callType == Constants.P2P_TYPE.P2P_TYPE_MONITOR) {
					isMonitor = 1;
					String passwordStr = EntryPassword(password);
					pwd = Integer.parseInt(passwordStr);
				}

				int x = 0;
				if (null != ipFlag && !ipFlag.equals("")
						&& MyUtils.isNumeric(ipFlag)) {
					x = MediaPlayer.getInstance().native_p2p_call(
							Integer.parseInt(ipFlag), isMonitor, pwd, -1,
							VideoMode, byt, pushMsg.getBytes("utf-8"), "gwell",headerId);
				} else {
					x = MediaPlayer.getInstance().native_p2p_call(id,
							isMonitor, pwd, -1, VideoMode, byt,
							pushMsg.getBytes("utf-8"), "gwell",headerId);
				}

				if (x == 1) {
					result = true;
					Log.i("tag", "p2p call success");
				} else {
					Log.e("tag", "p2p call fail");
				}

			}
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;

		// new DelayThread(1000, new DelayThread.OnRunListener() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// try {
		// synchronized (P2PHandler.this) {
		// call_alter(contactId, password, isOutCall, callType,
		// callId, ipFlag, pushMsg);
		// }
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// }
		// }).start();
	}

	private static int[] regionCode = { 1264, 1268, 1242, 1246, 1441, 1284,
			1345, 1767, 1809, 1473, 1876, 1664, 1787, 1869, 1758, 1784, 1868,
			1649, 1340, 1671, 1670, 210, 211, 212, 213, 214, 215, 216, 217,
			218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230,
			231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243,
			244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255, 256,
			257, 258, 259, 260, 261, 262, 263, 264, 265, 266, 267, 268, 269,
			290, 291, 292, 293, 294, 295, 296, 297, 298, 299, 350, 351, 352,
			353, 354, 355, 356, 357, 358, 359, 370, 371, 372, 373, 374, 375,
			376, 377, 378, 379, 380, 381, 382, 383, 384, 385, 386, 387, 388,
			389, 420, 421, 422, 423, 424, 425, 426, 427, 428, 429, 500, 501,
			502, 503, 504, 505, 506, 507, 508, 509, 590, 591, 592, 593, 594,
			595, 596, 597, 598, 599, 670, 671, 672, 673, 674, 675, 676, 677,
			678, 679, 680, 681, 682, 683, 684, 685, 686, 687, 688, 689, 690,
			691, 692, 693, 694, 695, 696, 697, 698, 699, 800, 801, 802, 803,
			804, 805, 806, 807, 808, 809, 850, 851, 852, 853, 854, 855, 856,
			857, 858, 859, 870, 871, 872, 873, 874, 875, 876, 877, 878, 879,
			880, 881, 882, 883, 884, 885, 886, 887, 888, 889, 960, 961, 962,
			963, 964, 965, 966, 967, 968, 969, 970, 971, 972, 973, 974, 975,
			976, 977, 978, 979, 990, 991, 992, 993, 994, 995, 996, 997, 998,
			999, 20, 27, 28, 30, 31, 32, 33, 34, 36, 37, 38, 39, 40, 41, 42,
			43, 44, 45, 46, 47, 48, 49, 51, 52, 53, 54, 55, 56, 57, 58, 60, 61,
			62, 63, 64, 65, 66, 81, 82, 83, 84, 86, 90, 91, 92, 93, 94, 95, 98,
			1, 7 };

	private void call_alter(String threeNumber, String password,
			boolean isOutCall, int callType, String callId, String ipFlag,
			String pushMsg, int VideoMode,String headerID) throws UnsupportedEncodingException {
		byte[] byt = new byte[8];
		long headerId=Long.parseLong(headerID);
		if (isOutCall) {
			String parseNum = callId;
			if (parseNum.contains("+")) {
				boolean isPhone = false;
				try {
					for (int i = 0; i < regionCode.length; i++) {
						int cLength = String.valueOf(regionCode[i]).length();
						parseNum = parseNum.replace("+", "");
						String hight = parseNum.substring(0, cLength);
						String low = parseNum.substring(cLength,
								parseNum.length());
						if (Integer.parseInt(hight) == regionCode[i]) {
							long num = (Long.parseLong(hight) << 48 | Long
									.parseLong(low));
							parseNum = String.valueOf(num);
							isPhone = true;
							break;
						}
					}
				} catch (Exception e) {
					// onCallResult(Constants.P2P_CALL.CALL_RESULT.CALL_PHONE_FORMAT_ERROR);
				}

				if (!isPhone) {
					// onCallResult(Constants.P2P_CALL.CALL_RESULT.CALL_PHONE_FORMAT_ERROR);
					return;
				}
			}

			long id = Long.parseLong(parseNum);
			if (parseNum.charAt(0) == '0') {
				id = 0 - id;
			}

			int pwd = 0;

			int isMonitor = 0;
			if (callType == Constants.P2P_TYPE.P2P_TYPE_MONITOR) {
				isMonitor = 1;
				String passwordStr = EntryPassword(password);
				if (MyUtils.isNumeric(passwordStr)) {
					pwd = Integer.parseInt(passwordStr);
				}
			}

			int result = 0;
			if (null != ipFlag && !ipFlag.equals("")
					&& MyUtils.isNumeric(ipFlag)) {
				result = MediaPlayer.getInstance().native_p2p_call(
						Integer.parseInt(ipFlag), isMonitor, pwd, -1,
						VideoMode, byt, pushMsg.getBytes("utf-8"), "gwell",headerId);
			} else {
				result = MediaPlayer.getInstance().native_p2p_call(id,
						isMonitor, pwd, -1, VideoMode, byt,
						pushMsg.getBytes("utf-8"), "gwell",headerId);
			}

			if (result == 1) {
				Log.i("tag", "p2p call success");
			} else {
				Log.e("tag", "p2p call fail");
			}
		}
	}

	/**
	 * 褰曞儚鍥炴斁杩炴帴
	 */
	public void playbackConnect(String contactId, String password,
			String filename, int recordFilePosition, int VideoMode) {
		byte[] byt = filename.getBytes();
		String passwordStr = EntryPassword(password);
		MediaPlayer.getInstance().native_p2p_call(Integer.parseInt(contactId),
				Constants.P2P_TYPE.P2P_TYPE_PLAYBACK,
				Integer.parseInt(passwordStr), recordFilePosition, VideoMode, byt,
				"".getBytes(), "gwell",Integer.parseInt(contactId));
	}

	/**
	 * 璁剧疆瑙嗛妯″紡
	 */

	public int setVideoMode(int type) {
		Log.e("dxsMode","type----->"+type);
		return MediaPlayer.getInstance().iSetVideoMode(type);
	}

	public void checkDeviceUpdate(String contactId, String password) {
		String passwordStr = EntryPassword(password);
		Log.e(TAG, "P2PHANDLER:checkDeviceUpdate");
		if (this.MSG_ID_CHECK_DEVICE_UPDATE >= (Constants.MsgSection.MSG_ID_CHECK_DEVICE_UPDATE)) {
			this.MSG_ID_CHECK_DEVICE_UPDATE = Constants.MsgSection.MSG_ID_CHECK_DEVICE_UPDATE - 1000;
		}

		MediaPlayer.getInstance().checkDeviceUpdate(
				Integer.parseInt(contactId), Integer.parseInt(passwordStr),
				this.MSG_ID_CHECK_DEVICE_UPDATE);
		this.MSG_ID_CHECK_DEVICE_UPDATE++;
	}
	

	/**
	 * 璁惧鍥轰欢鏇存柊
	 * 
	 * @param contactId
	 * @param password
	 */
	public void doDeviceUpdate(String contactId, String password) {

		Log.e(TAG, "P2PHANDLER:doDeviceUpdate");
		if (this.MSG_ID_DO_DEVICE_UPDATE >= (Constants.MsgSection.MSG_ID_DO_DEVICE_UPDATE)) {
			this.MSG_ID_DO_DEVICE_UPDATE = Constants.MsgSection.MSG_ID_DO_DEVICE_UPDATE - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.getInstance().doDeviceUpdate(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MSG_ID_DO_DEVICE_UPDATE);
		this.MSG_ID_DO_DEVICE_UPDATE++;

	}

	/**
	 * 鍙栨秷璁惧鍥轰欢鏇存柊
	 * 
	 * @param contactId
	 * @param password
	 */
	public void cancelDeviceUpdate(String contactId, String password) {

		Log.e(TAG, "P2PHANDLER:cancelDeviceUpdate");
		if (this.MSG_ID_CANCEL_DEVICE_UPDATE >= (Constants.MsgSection.MSG_ID_CANCEL_DEVICE_UPDATE)) {
			this.MSG_ID_CANCEL_DEVICE_UPDATE = Constants.MsgSection.MSG_ID_CANCEL_DEVICE_UPDATE - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.getInstance().cancelDeviceUpdate(
				Integer.parseInt(contactId), Integer.parseInt(passwordStr),
				this.MSG_ID_CANCEL_DEVICE_UPDATE);
		this.MSG_ID_CANCEL_DEVICE_UPDATE++;

	}

	/**
	 * 鑾峰緱鍥轰欢鐗堟湰
	 * 
	 * @param contactId
	 * @param password
	 */
	public void getDeviceVersion(String contactId, String password) {

		Log.e(TAG, "P2PHANDLER:getDeviceVersion");
		if (this.MSG_ID_GET_DEVICE_VERSION >= (Constants.MsgSection.MSG_ID_GET_DEVICE_VERSION)) {
			this.MSG_ID_GET_DEVICE_VERSION = Constants.MsgSection.MSG_ID_GET_DEVICE_VERSION - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.getInstance().getDeviceVersion(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MSG_ID_GET_DEVICE_VERSION);
		this.MSG_ID_GET_DEVICE_VERSION++;
	}

	public boolean sendCtlCmd(int cmd, int option) {
		if (MediaPlayer.iSendCtlCmd(cmd, option) == 1) {
			return true;
		} else {
			return false;
		}
	}

	public void setBindFlag(int flag) {
		MediaPlayer.setBindFlag(flag);
	}

	/**
	 * 璁剧疆鏄惁鎺ユ敹闊宠棰戞暟鎹湪楂樼2CU涓崟鐙缃棤鏁�
	 * 
	 * @param fgEn
	 */
	public void setRecvAVDataEnable(boolean fgEn) {
		MediaPlayer.getInstance()._SetRecvAVDataEnable(fgEn);
	}

	public void setImageReverse(String contactId, String password, int value) {
		Log.e(TAG, "P2PHANDLER:setImageReverse");
		if ((this.MESG_ID_STTING_PIC_REVERSE) >= (Constants.MsgSection.MESG_ID_STTING_PIC_REVERSE)) {
			this.MESG_ID_STTING_PIC_REVERSE = Constants.MsgSection.MESG_ID_STTING_PIC_REVERSE - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer
				.iSetNPCSettings(
						Integer.parseInt(contactId),
						Integer.parseInt(passwordStr),
						this.MESG_ID_STTING_PIC_REVERSE,
						Constants.P2P_SETTING.SETTING_TYPE.SETTING_IMAGE_REVERSE,
						value);
		this.MESG_ID_STTING_PIC_REVERSE++;
	}

	public void setInfraredSwitch(String contactId, String password, int value) {
		if ((this.MESG_ID_STTING_IR_ALARM_EN) >= (Constants.MsgSection.MESG_ID_STTING_IR_ALARM_EN)) {
			this.MESG_ID_STTING_IR_ALARM_EN = Constants.MsgSection.MESG_ID_STTING_IR_ALARM_EN - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iSetNPCSettings(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MESG_ID_STTING_IR_ALARM_EN,
				Constants.P2P_SETTING.SETTING_TYPE.SETTING_ID_IR_ALARM_EN,
				value);
		this.MESG_ID_STTING_IR_ALARM_EN++;
	}

	public void setWiredAlarmInput(String contactId, String password, int value) {
		if ((this.MESG_STTING_ID_EXTLINE_ALARM_IN_EN) >= (Constants.MsgSection.MESG_STTING_ID_EXTLINE_ALARM_IN_EN)) {
			this.MESG_STTING_ID_EXTLINE_ALARM_IN_EN = Constants.MsgSection.MESG_STTING_ID_EXTLINE_ALARM_IN_EN - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer
				.iSetNPCSettings(
						Integer.parseInt(contactId),
						Integer.parseInt(passwordStr),
						this.MESG_STTING_ID_EXTLINE_ALARM_IN_EN,
						Constants.P2P_SETTING.SETTING_TYPE.SETTING_ID_EXTLINE_ALARM_IN_EN,
						value);
		this.MESG_STTING_ID_EXTLINE_ALARM_IN_EN++;
	}

	public void setWiredAlarmOut(String contactId, String password, int value) {
		if ((this.MESG_STTING_ID_EXTLINE_ALARM_OUT_EN) >= (Constants.MsgSection.MESG_STTING_ID_EXTLINE_ALARM_OUT_EN)) {
			this.MESG_STTING_ID_EXTLINE_ALARM_OUT_EN = Constants.MsgSection.MESG_STTING_ID_EXTLINE_ALARM_OUT_EN - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer
				.iSetNPCSettings(
						Integer.parseInt(contactId),
						Integer.parseInt(passwordStr),
						this.MESG_STTING_ID_EXTLINE_ALARM_OUT_EN,
						Constants.P2P_SETTING.SETTING_TYPE.SETTING_ID_EXTLINE_ALARM_OUT_EN,
						value);
		this.MESG_STTING_ID_EXTLINE_ALARM_OUT_EN++;
	}

	public void setAutomaticUpgrade(String contactId, String password, int value) {
		if ((this.MESG_STTING_ID_SECUPGDEV) >= (Constants.MsgSection.MESG_STTING_ID_SECUPGDEV)) {
			this.MESG_STTING_ID_SECUPGDEV = Constants.MsgSection.MESG_STTING_ID_SECUPGDEV - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iSetNPCSettings(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MESG_STTING_ID_SECUPGDEV,
				Constants.P2P_SETTING.SETTING_TYPE.SETTING_ID_SECUPGDEV, value);
		this.MESG_STTING_ID_SECUPGDEV++;
	}

	/**
	 * 璁剧疆鏃跺尯
	 * 
	 * @param contactId
	 * @param password
	 * @param value
	 */
	public void setTimeZone(String contactId, String password, int value) {
		if ((this.MESG_STTING_ID_TIMEZONE) >= (Constants.MsgSection.MESG_STTING_ID_TIMEZONE)) {
			this.MESG_STTING_ID_TIMEZONE = Constants.MsgSection.MESG_STTING_ID_TIMEZONE - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iSetNPCSettings(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MESG_STTING_ID_TIMEZONE,
				Constants.P2P_SETTING.SETTING_TYPE.SETTING_ID_TIMEZONE, value);
		this.MESG_STTING_ID_TIMEZONE++;
	}

	public void getSdCardCapacity(String contactId, String password, String data) {
		if ((this.MESG_GET_SD_CARD_CAPACITY) >= (Constants.MsgSection.MESG_GET_SD_CARD_CAPACITY)) {
			this.MESG_GET_SD_CARD_CAPACITY = Constants.MsgSection.MESG_GET_SD_CARD_CAPACITY - 1000;
		}
		byte[] datas = new byte[16];
		datas[0] = 80;
		datas[1] = 0;
		datas[2] = 0;
		datas[3] = 0;
		String passwordStr = EntryPassword(password);
		MediaPlayer.iExtendedCmd(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MESG_GET_SD_CARD_CAPACITY,
				datas, 4);
		this.MESG_GET_SD_CARD_CAPACITY++;
	}

	/**
	 * SD鍗℃牸寮忓寲
	 * 
	 * @param contactId
	 * @param password
	 * @param SDcardID
	 */
	public void setSdFormat(String contactId, String password, int SDcardID) {
		if ((this.MESG_SD_CARD_FORMAT) >= (Constants.MsgSection.MESG_SD_CARD_FORMAT)) {
			this.MESG_SD_CARD_FORMAT = Constants.MsgSection.MESG_SD_CARD_FORMAT - 1000;
		}
		byte[] datas = new byte[16];
		datas[0] = 81;
		datas[1] = 0;
		datas[2] = 0;
		datas[3] = 0;
		datas[4] = (byte) SDcardID;
		Log.e("id", "id:" + datas[4]);
		String passwordStr = EntryPassword(password);
		MediaPlayer.iExtendedCmd(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MESG_SD_CARD_FORMAT, datas, 5);
		this.MESG_SD_CARD_FORMAT++;
	}

	public void setGPIO(String contactId, String password, int group, int pin) {
		if ((this.MESG_SET_GPIO) >= (Constants.MsgSection.MESG_SET_GPIO)) {
			this.MESG_SET_GPIO = Constants.MsgSection.MESG_SET_GPIO - 1000;
		}
		byte[] datas = new byte[37];
		datas[0] = 95;
		datas[1] = 0;
		datas[2] = (byte) group;
		datas[3] = (byte) pin;
		datas[4] = 5;
		datas[5] = (byte) (-15 & 0xFF);
		datas[6] = (byte) (-15 >> 8 & 0xFF);
		datas[7] = (byte) (-15 >> 16 & 0xFF);
		datas[8] = (byte) (-15 >> 24 & 0xFF);
		datas[9] = (byte) (1000 & 0xFF);
		datas[10] = (byte) (1000 >> 8 & 0xFF);
		datas[11] = (byte) (1000 >> 16 & 0xFF);
		datas[12] = (byte) (1000 >> 24 & 0xFF);
		datas[13] = (byte) (-1000 & 0xFF);
		datas[14] = (byte) (-1000 >> 8 & 0xFF);
		datas[15] = (byte) (-1000 >> 16 & 0xFF);
		datas[16] = (byte) (-1000 >> 24 & 0xFF);
		datas[17] = (byte) (1000 & 0xFF);
		datas[18] = (byte) (1000 >> 8 & 0xFF);
		datas[19] = (byte) (1000 >> 16 & 0xFF);
		datas[20] = (byte) (1000 >> 24 & 0xFF);
		datas[21] = (byte) (-1000 & 0xFF);
		datas[22] = (byte) (-1000 >> 8 & 0xFF);
		datas[23] = (byte) (-1000 >> 16 & 0xFF);
		datas[24] = (byte) (-1000 >> 24 & 0xFF);
		for (int i = 25; i < datas.length; i++) {
			datas[i] = 0;
		}
		String s = "";
		for (int j = 0; j < datas.length; j++) {
			s = s + " " + datas[j];
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iExtendedCmd(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MESG_SET_GPIO, datas,
				datas.length);
		this.MESG_SET_GPIO++;
	}

	public void setGPIO1_0(String contactId, String password) {
		if ((this.MESG_SET_GPI1_0) >= (Constants.MsgSection.MESG_SET_GPI1_0)) {
			this.MESG_SET_GPI1_0 = Constants.MsgSection.MESG_SET_GPI1_0 - 1000;
		}
		byte[] datas = new byte[37];
		datas[0] = 95;
		datas[1] = 0;
		datas[2] = 1;
		datas[3] = 0;
		datas[4] = 3;
		datas[5] = (byte) (-15 & 0xFF);
		datas[6] = (byte) (-15 >> 8 & 0xFF);
		datas[7] = (byte) (-15 >> 16 & 0xFF);
		datas[8] = (byte) (-15 >> 24 & 0xFF);
		datas[9] = (byte) (6000 & 0xFF);
		datas[10] = (byte) (6000 >> 8 & 0xFF);
		datas[11] = (byte) (6000 >> 16 & 0xFF);
		datas[12] = (byte) (6000 >> 24 & 0xFF);
		datas[13] = (byte) (-6000 & 0xFF);
		datas[14] = (byte) (-6000 >> 8 & 0xFF);
		datas[15] = (byte) (-6000 >> 16 & 0xFF);
		datas[16] = (byte) (-6000 >> 24 & 0xFF);
		for (int i = 17; i < datas.length; i++) {
			datas[i] = 0;
		}
		String s = "";
		for (int j = 0; j < datas.length; j++) {
			s = s + " " + datas[j];
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iExtendedCmd(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MESG_SET_GPI1_0, datas,
				datas.length);
		this.MESG_SET_GPI1_0++;
	}

	/**
	 * 璁剧疆棰勫綍鍍�
	 * 
	 * @param contactId
	 * @param password
	 * @param value
	 */
	public void setPreRecord(String contactId, String password, int value) {
		if ((this.MESG_SET_PRE_RECORD) >= (Constants.MsgSection.MESG_SET_PRE_RECORD)) {
			this.MESG_SET_PRE_RECORD = Constants.MsgSection.MESG_SET_PRE_RECORD - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iSetNPCSettings(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MESG_SET_PRE_RECORD,
				Constants.P2P_SETTING.SETTING_TYPE.SETTING_ID_PRERECORD, value);
		this.MESG_SET_PRE_RECORD++;
	}

	/**
	 * 鑾峰緱闃插尯鎶ヨ寮�鍏崇姸鎬侊紝闇�瑕佽澶囬厤缃�
	 * 
	 * @param contactId
	 * @param password
	 */
	public void getDefenceAreaAlarmSwitch(String contactId, String password) {
		if ((this.MESG_GET_DEFENCE_AREA_SWITCH) >= (Constants.MsgSection.MESG_GET_DEFENCE_AREA_SWITCH)) {
			this.MESG_GET_DEFENCE_AREA_SWITCH = Constants.MsgSection.MESG_GET_DEFENCE_AREA_SWITCH - 1000;
		}
		byte[] datas = new byte[12];
		datas[0] = 82;
		datas[1] = 0;
		datas[2] = 8;
		datas[3] = 0;
		datas[4] = 0;
		datas[5] = 0;
		datas[6] = 0;
		datas[7] = 0;
		datas[8] = 0;
		datas[9] = 0;
		datas[10] = 0;
		datas[11] = 0;
		String passwordStr = EntryPassword(password);
		MediaPlayer.iExtendedCmd(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MESG_GET_DEFENCE_AREA_SWITCH,
				datas, datas.length);
		this.MESG_GET_DEFENCE_AREA_SWITCH++;
	}

	/**
	 * 璁剧疆闃插尯鎶ヨ寮�鍏�
	 * 
	 * @param contactId
	 * @param password
	 * @param state
	 *            寮�鍏崇姸鎬�
	 * @param group
	 *            闃插尯
	 * @param item
	 *            閫氶亾
	 */
	public void setDefenceAreaAlarmSwitch(String contactId, String password,
			int state, int group, int item) {
		if ((this.MESG_SET_DEFENCE_AREA_SWITCH) >= (Constants.MsgSection.MESG_SET_DEFENCE_AREA_SWITCH)) {
			this.MESG_SET_DEFENCE_AREA_SWITCH = Constants.MsgSection.MESG_SET_DEFENCE_AREA_SWITCH - 1000;
		}
		byte[] datas = new byte[12];
		datas[0] = 83;
		datas[1] = 0;
		datas[2] = (byte) state;
		datas[3] = 1;
		datas[4] = (byte) (group & 0xFF);
		datas[5] = (byte) (group >> 8 & 0xFF);
		datas[6] = (byte) (group >> 16 & 0xFF);
		datas[7] = (byte) (group >> 24 & 0xFF);
		datas[8] = (byte) (item & 0xFF);
		datas[9] = (byte) (item >> 8 & 0xFF);
		datas[10] = (byte) (item >> 16 & 0xFF);
		datas[11] = (byte) (item >> 24 & 0xFF);
		String passwordStr = EntryPassword(password);
		MediaPlayer.iExtendedCmd(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MESG_SET_DEFENCE_AREA_SWITCH,
				datas, datas.length);
		this.MESG_SET_DEFENCE_AREA_SWITCH++;
	}

	/**
	 * 鑾峰緱鐏殑鐘舵��
	 * 
	 * @param contactId
	 * @param password
	 */
	public void vgetLampStatus(String contactId, String password) {
		if (this.MESG_GET_LAMP >= (Constants.MsgSection.MESG_GET_LAMP)) {
			this.MESG_GET_LAMP = Constants.MsgSection.MESG_GET_LAMP - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iGetNPCSettings(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MESG_GET_LAMP);
		this.MESG_GET_LAMP++;
	}

	/**
	 * 璁剧疆鐏殑鐘舵��
	 * 
	 * @param contactId
	 * @param password
	 */
	public void vsetLampStatus(String contactId, String password, int LampStatus) {
		if (this.MESG_SET_LAMP >= (Constants.MsgSection.MESG_SET_LAMP)) {
			this.MESG_SET_LAMP = Constants.MsgSection.MESG_SET_LAMP - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iSetNPCSettings(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MESG_SET_LAMP, 34, LampStatus);
		this.MESG_SET_LAMP++;
	}

	/**
	 * 鑷畾涔変覆鍙ｆ秷鎭�
	 * 
	 * @param contactId
	 * @param password
	 * @param dataBuffer
	 *            鐢ㄦ埛杈撳叆鐨勪覆鍙ｆ秷鎭�
	 * @param len
	 *            娑堟伅闀垮害
	 * @param appID
	 */
	public void vSendDataToURAT(String contactId, String password,
			byte[] dataBuffer, int len, boolean appID) {
		if (this.SET_USER_DEFINE_MESG >= (Constants.MsgSection.SET_USER_DEFINE_MESG)) {
			this.SET_USER_DEFINE_MESG = Constants.MsgSection.SET_USER_DEFINE_MESG - 1000;
		}
		int iId = Integer.parseInt(contactId);
		String fgMesg = "";
		if (appID) {
			fgMesg = "IPC1";
		} else {
			fgMesg = "IPC2";
		}
		byte[] fgme = null;
		fgme = fgMesg.getBytes();
		int lens = fgme.length;
		byte[] datas = new byte[fgme.length + dataBuffer.length];
		System.arraycopy(fgme, 0, datas, 0, fgme.length);
		System.arraycopy(dataBuffer, 0, datas, fgme.length, dataBuffer.length);
		String passwordStr = EntryPassword(password);
		MediaPlayer.iSendCmdToFriend(iId, Integer.parseInt(passwordStr),
				this.SET_USER_DEFINE_MESG, datas, datas.length);
		this.SET_USER_DEFINE_MESG++;
	}

	/**
	 * 鑾峰彇鎶ヨ绫诲瀷鎴栭槻鍖洪�氶亾瀵瑰簲鐨勬憚鍍忓ご棰勭疆浣嶇疆
	 * 
	 * @param contactId
	 * @param password
	 * @param data
	 */
	public void sMesgGetAlarmPresetMotorPos(String contactId, String password,
			byte[] data) {
		if ((this.MESG_GET_PRESET_MOTOR_POS) >= (Constants.MsgSection.MESG_GET_PRESET_MOTOR_POS)) {
			this.MESG_GET_PRESET_MOTOR_POS = Constants.MsgSection.MESG_GET_PRESET_MOTOR_POS - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iExtendedCmd(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MESG_GET_PRESET_MOTOR_POS,
				data, 7);

		this.MESG_GET_PRESET_MOTOR_POS++;
	}

	/**
	 * 璁剧疆鎶ヨ绫诲瀷鎴栭槻鍖洪�氶亾瀵瑰簲鐨勬憚鍍忓ご棰勭疆浣嶇疆
	 * 
	 * @param contactId
	 * @param password
	 * @param data
	 */
	public void sMesgSetAlarmPresetMotorPos(String contactId, String password,
			byte[] data) {
		if ((this.MESG_SET_PRESET_MOTOR_POS) >= (Constants.MsgSection.MESG_SET_PRESET_MOTOR_POS)) {
			this.MESG_SET_PRESET_MOTOR_POS = Constants.MsgSection.MESG_SET_PRESET_MOTOR_POS - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iExtendedCmd(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MESG_SET_PRESET_MOTOR_POS,
				data, 7);

		this.MESG_SET_PRESET_MOTOR_POS++;
	}

	/**
	 * 璁剧疆/鏌ョ湅鎽勫儚澶撮缃綅缃�
	 * 
	 * @param contactId
	 * @param password
	 * @param data
	 */
	public void sMesgPresetMotorPos(String contactId, String password,
			byte[] data) {
		if ((this.MESG_PRESET_MOTOR_POS) >= (Constants.MsgSection.MESG_PRESET_MOTOR_POS)) {
			this.MESG_PRESET_MOTOR_POS = Constants.MsgSection.MESG_PRESET_MOTOR_POS - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer
				.iExtendedCmd(Integer.parseInt(contactId),
						Integer.parseInt(passwordStr), this.MESG_PRESET_MOTOR_POS,
						data, 7);
		this.MESG_PRESET_MOTOR_POS++;
	}

	/**
	 * 鑾峰彇璁惧IP鐩稿叧淇℃伅
	 * 
	 * @param contactId
	 * @param password
	 * @param data
	 */
	public void sIpConfig(String contactId, String password, byte[] data) {
		if ((this.IP_CONFIG) >= (Constants.MsgSection.IP_CONFIG)) {
			this.IP_CONFIG = Constants.MsgSection.IP_CONFIG - 1000;
		}
		String passwordStr = EntryPassword(password);
		MediaPlayer.iExtendedCmd(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.IP_CONFIG, data, 8);
		this.IP_CONFIG++;
	}

	public void getAlarmCenterParameters(String contactId, String password) {
		if ((this.MESG_GET_ALARM_CENTER_PARAMETER) >= (Constants.MsgSection.MESG_GET_ALARM_CENTER_PARAMETER)) {
			this.MESG_GET_ALARM_CENTER_PARAMETER = Constants.MsgSection.MESG_GET_ALARM_CENTER_PARAMETER - 1000;
		}
		byte[] datas = new byte[20];
		datas[0] = 100;
		datas[1] = 0;
		datas[2] = 0;
		datas[3] = 20;
		for (int i = 4; i < datas.length; i++) {
			datas[i] = 0;
		}
		String s = "";
		for (int j = 0; j < datas.length; j++) {
			s = s + "  " + datas[j];
		}
		Log.e("alarm_center", s + "lenght=" + datas.length);
		Log.e("alarmcenter", "MESG_GET_ALARM_CENTER_PARAMETER="
				+ this.MESG_GET_ALARM_CENTER_PARAMETER + "  " + "length="
				+ datas.length);
		String passwordStr = EntryPassword(password);
		MediaPlayer.iExtendedCmd(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr),
				this.MESG_GET_ALARM_CENTER_PARAMETER, datas, datas.length);
		this.MESG_GET_ALARM_CENTER_PARAMETER++;
	}

	/**
	 * 璁剧疆鎶ヨ涓績鍙傛暟
	 * 
	 * @param contactId
	 * @param password
	 * @param port
	 * @param ipdress
	 * @param userId
	 * @param state
	 */
	public void setAlarmCenterParameters(String contactId, String password,
			int port, String ipdress, String userId, int state) {
		if ((this.MESG_SET_ALARM_CENTER_PARAMETER) >= (Constants.MsgSection.MESG_SET_ALARM_CENTER_PARAMETER)) {
			this.MESG_SET_ALARM_CENTER_PARAMETER = Constants.MsgSection.MESG_SET_ALARM_CENTER_PARAMETER - 1000;
		}
		byte[] datas = new byte[20];
		datas[0] = 101;
		datas[1] = 0;
		datas[2] = 0;
		datas[3] = 20;
		datas[4] = (byte) state;
		datas[5] = 0;
		datas[6] = 0;
		datas[7] = 0;
		Log.e("ipdesss_length", ipdress);
		String[] ipdesss = ipdress.split("\\.");
		Log.e("ipdesss_length", "lenght=" + ipdesss.length);
		int[] ips = { Integer.parseInt(ipdesss[0]),
				Integer.parseInt(ipdesss[1]), Integer.parseInt(ipdesss[2]),
				Integer.parseInt(ipdesss[3]) };
		int user_id2 = Integer.parseInt(userId, 16);
		datas[8] = (byte) (ips[3]);
		datas[9] = (byte) (ips[2]);
		datas[10] = (byte) (ips[1]);
		datas[11] = (byte) ips[0];
		datas[12] = (byte) (port & 0xFF);
		datas[13] = (byte) (port >> 8 & 0xFF);
		datas[14] = (byte) (port >> 16 & 0xFF);
		datas[15] = (byte) (port >> 24 & 0xFF);
		datas[16] = (byte) (user_id2 & 0xFF);
		datas[17] = (byte) (user_id2 >> 8 & 0xFF);
		datas[18] = (byte) (user_id2 >> 16 & 0xFF);
		datas[19] = (byte) (user_id2 >> 24 & 0xFF);
		// datas[20]=(byte)(user_id2>>32&0xFF);
		// datas[21]=(byte)(user_id2>>40&0xFF);
		// datas[22]=(byte)(user_id2>>48&0xFF);
		// datas[23]=(byte)(user_id2>>56&0xFF);

		for (int i = 0; i < datas.length; i++) {
			Log.e("alarm_center", "datas[i]" + datas[i]);
		}
		Log.e("alarmcenter", "MESG_SET_ALARM_CENTER_PARAMETER="
				+ this.MESG_SET_ALARM_CENTER_PARAMETER + "  " + "length="
				+ datas.length);
		String passwordStr = EntryPassword(password);
		MediaPlayer.iExtendedCmd(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr),
				this.MESG_SET_ALARM_CENTER_PARAMETER, datas, datas.length);
		this.MESG_SET_ALARM_CENTER_PARAMETER++;
	}

	public void DeleteDeviceAlarmId(String contactId) {
		if ((this.MESG_DELETE_ALARMID) >= (Constants.MsgSection.MESG_DELETE_DEVICEALARMID)) {
			this.MESG_DELETE_ALARMID = Constants.MsgSection.MESG_DELETE_DEVICEALARMID - 1000;
		}
		byte[] datas = new byte[16];
		datas[0] = 127;
		datas[3] = 16;
		datas[8] = 1;
		MediaPlayer.iExtendedCmd(Integer.parseInt(contactId), 0,
				this.MESG_DELETE_ALARMID, datas, datas.length);
		Log.e("vRetExtenedCmd", "datas-->" + Arrays.toString(datas));
		Log.e("leledeleteAlarm", "DeleteDeviceAlarmId");
		this.MESG_DELETE_ALARMID++;
	}

	public void setSystemMessageIndex(int iSystemMessageType,
			int iSystemMessageIndex) {
		if ((this.MESG_SET_SYSTEM_MESSAGE_INDEX) >= (Constants.MsgSection.MESG_SET_SYSTEM_MESSAGE_INDEX)) {
			this.MESG_SET_SYSTEM_MESSAGE_INDEX = Constants.MsgSection.MESG_SET_SYSTEM_MESSAGE_INDEX - 1000;
		}

		MediaPlayer.SetSystemMessageIndex(iSystemMessageType,
				iSystemMessageIndex);
		this.MESG_SET_SYSTEM_MESSAGE_INDEX++;
	}

	/**
	 * 鑷畾涔夋埅鍥捐矾寰�
	 * 
	 * @param path
	 *            璺緞锛堝繀椤荤渷鐣dcard璺緞锛墌/11/22/33/10234.jpg鏃� path=/11/22/33
	 * @param name
	 *            name=1023.jpg
	 * @return 鍒涘缓鏂囦欢澶规槸鍚︽垚鍔� 0鎴愬姛 !0澶辫触
	 */
	public int setScreenShotpath(String path, String name) {
		return MediaPlayer.getInstance().SetScreenShotPath(path, name);
	}

	public void controlCamera(String contactId, String password, byte[] data) {
		if (this.CONTROL_CAMERA >= (Constants.MsgSection.CONTROL_CAMERA)) {
			this.CONTROL_CAMERA = Constants.MsgSection.CONTROL_CAMERA - 1000;
		}
		byte[] sendData = new byte[68];
		for (int i = 0; i < data.length; i++) {
			sendData[i] = data[i];
		}
		if (data.length < 68) {
			for (int j = data.length; j < sendData.length; j++) {
				sendData[j] = 0;
			}
		}
		String passwordStr = EntryPassword(password);
		int iId = Integer.parseInt(contactId);
		MediaPlayer.iExtendedCmd(iId, Integer.parseInt(passwordStr),
				this.CONTROL_CAMERA, sendData, sendData.length);
		this.CONTROL_CAMERA++;

	}

	/**
	 * 鏀跺埌闂ㄩ搩
	 */
	public void setReceiveDoorBell(String contactId) {
		Log.e(TAG, "P2PHANDLER:setRemoteDefence");
		if (this.MESG_SET_RECEIVE_DOOBELL >= (Constants.MsgSection.MESG_SET_RECEIVE_DOOBELL)) {
			this.MESG_SET_RECEIVE_DOOBELL = Constants.MsgSection.MESG_SET_RECEIVE_DOOBELL - 1000;
		}
		byte[] datas = new byte[16];
		datas[0] = 127;
		datas[3] = 16;
		datas[8] = 3;
		datas[12] = 1;

		MediaPlayer.iExtendedCmd(Integer.parseInt(contactId), 0,
				this.MESG_SET_RECEIVE_DOOBELL, datas, datas.length);
		Log.e("vRetExtenedCmd", "datas-->" + Arrays.toString(datas));
		Log.e("leledeleteAlarm", "setReceiveDoorBell");
		this.MESG_SET_RECEIVE_DOOBELL++;
	}

	public void getDeviceLanguage(String contactId, String password) {
		if ((this.MESG_GET_LANGUEGE) >= (Constants.MsgSection.MESG_GET_LANGUEGE)) {
			this.MESG_GET_LANGUEGE = Constants.MsgSection.MESG_GET_LANGUEGE - 1000;
		}
		byte[] datas = new byte[7];
		datas[0] = (byte) 211;
		String passwordStr = EntryPassword(password);
		MediaPlayer.iExtendedCmd(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MESG_GET_LANGUEGE, datas,
				datas.length);
		this.MESG_GET_LANGUEGE++;
	}

	public void setDevieceLanguege(String contactId, String password,
			int curLanguege) {
		if ((this.MESG_SET_LANGUEGE) >= (Constants.MsgSection.MESG_SET_LANGUEGE)) {
			this.MESG_SET_LANGUEGE = Constants.MsgSection.MESG_SET_LANGUEGE - 1000;
		}
		byte[] datas = new byte[7];
		datas[0] = (byte) 212;
		datas[5] = (byte) curLanguege;
		String passwordStr = EntryPassword(password);
		MediaPlayer.iExtendedCmd(Integer.parseInt(contactId),
				Integer.parseInt(passwordStr), this.MESG_SET_LANGUEGE, datas,
				datas.length);
		this.MESG_SET_LANGUEGE++;
	}

	public void setFocus(byte state) {
		byte[] data = new byte[5];
		data[0] = (byte) state;
		MediaPlayer.SendUserData(9, 1, data, data.length);
		Log.e("ptz", "focus--" + Arrays.toString(data));
	}

	public void setZoom(byte state) {
		byte[] data = new byte[5];
		data[0] = (byte) state;
		MediaPlayer.SendUserData(9, 2, data, data.length);
		Log.e("ptz", "zoom--" + Arrays.toString(data));
	}

//	public String EntryPassword(String password) {
//		String passwordStr = EntryPassword(password);
//		if (passwordStr.length()==0||(MyUtils.isNumeric(passwordStr) && passwordStr.length() < 10&&(passwordStr.charAt(0)!='0'))) {
//			return passwordStr;
//		} else {
//			return String.valueOf(MediaPlayer.EntryPwd(passwordStr));
//		}
//	}
	public String EntryPassword(String password){
		if(MyUtils.isNumeric(password)&&password.length()<10){
			return password;
		}else{
			return String.valueOf(MediaPlayer.EntryPwd(password));
		}
	}

	/**
	 * 鑾峰彇鎶ヨ鍥惧儚
	 *
	 * @param filename
	 *            鍥剧墖鏂囦欢鍚嶅畬鏁磋矾寰�
	 * @param localName
	 *            瀛樻斁鏂囦欢鍚嶅畬鏁磋矾寰�
	 * @return
	 */
	public int GetAllarmImage(String id, String password, String filename,
			String localName) {
		String passwordStr = EntryPassword(password);
		return MediaPlayer.GetAllarmImage(Integer.parseInt(id),
				Integer.parseInt(passwordStr), filename, localName);
	}

	/**
	 * 鑾峰彇鍥剧墖鐨勮繘搴�
	 * 
	 * @return
	 */
	public int GetAllarmImageProgress() {
		return MediaPlayer.GetFileProgress();
	}

	/**
	 * 鍙栨秷鑾峰彇鍥剧墖
	 */
	public void CancelGetAllarmImage() {
		MediaPlayer.CancelGetRemoteFile();
	}

	public static byte[] intToByte2(int i) {
		byte[] targets = new byte[2];
		targets[0] = (byte) (i & 0xFF);
		targets[1] = (byte) (i >> 8 & 0xFF);
		return targets;
	}

	public static byte[] intToByte4(int i) {
		byte[] targets = new byte[4];
		targets[0] = (byte) (i & 0xFF);
		targets[1] = (byte) (i >> 8 & 0xFF);
		targets[2] = (byte) (i >> 16 & 0xFF);
		targets[3] = (byte) (i >> 24 & 0xFF);
		return targets;
	}
	
	public void setFishEye(int contactId, int password, int msgId, byte[] datas) {
		MediaPlayer.iExtendedCmd(contactId, password, msgId, datas,
				datas.length);
	}

	public void getNvrIpcList(String nvrId, String password) {
		if (this.MESG_TYPE_GET_LAN_IPC_LIST >= (Constants.MsgSection.MESG_TYPE_GET_LAN_IPC_LIST)) {
			this.MESG_TYPE_GET_LAN_IPC_LIST = Constants.MsgSection.MESG_TYPE_GET_LAN_IPC_LIST - 1000;
		}
		byte[] data = new byte[20];
		data[0] = (byte) 129;
		MediaPlayer.iExtendedCmd(Integer.parseInt(nvrId),
				Integer.parseInt(EntryPassword(password)), this.MESG_TYPE_GET_LAN_IPC_LIST,
				data, data.length);
		Log.e("nvr_list", "nvrId=" + nvrId + "--" + "password=" + password
				+ "data=" + Arrays.toString(data));
		this.MESG_TYPE_GET_LAN_IPC_LIST++;

	}

	/**
	 * 鍏煎RTSP瀵嗙爜鍔犲瘑
	 * 
	 * @param password
	 * @return
	 */
	public String RTSPPwd(String password) {
		if (password != null && password.length() > 0) {
			return MediaPlayer.RTSPEntry(password);
		} else {
			return "error";
		}
	}

	/**
	 * 鏈嶅姟鍣ㄤ繚瀛樼殑璁惧瀵嗙爜鐨勮В瀵嗗嚱鏁帮紙搴旂敤鍦烘櫙锛氫粠鏈嶅姟鍣ㄨ幏鍙栬澶囧垪琛級
	 *
	 * @return 杩斿洖鐨別rror鍙兘鏄敊璇� 涔熷彲鑳芥槸缂撳啿鍖洪暱搴︿笉瓒�
	 */
	public String HTTPDecrypt(String userID, String password, int backLen) {
		if (password != null && password.length() > 0) {
			return MediaPlayer.HTTPDecrypt(userID, password, backLen);
		} else {
			return "error";
		}
	}

	/**
	 * 鏈嶅姟鍣ㄤ繚瀛樼殑璁惧瀵嗙爜鐨勫姞瀵嗗嚱鏁帮紙搴旂敤鍦烘櫙锛氫笂浼犺澶囧垪琛ㄥ埌鏈嶅姟鍣級
	 *
	 * @return 杩斿洖鐨別rror鍙兘鏄敊璇� 涔熷彲鑳芥槸缂撳啿鍖洪暱搴︿笉瓒�
	 */
	public String HTTPEncrypt(String userID, String password, int backLen) {
		if (password != null && password.length() > 0) {
			return MediaPlayer.HTTPEncrypt(userID, password, backLen);
		} else {
			return "error";
		}
	}

	/**
	 * 瀵嗙爜鍔犲瘑锛岀敤浜嶱2P
	 * 
	 * @param pwd
	 * @return
	 */
	public byte[] P2PEntryPassword(byte[] pwd) {
		return MediaPlayer.P2PEntryPassword(pwd);
	}
	
	/**
	 * 璁剧疆AP妯″紡鍒囨彌  1鍒囨彌
	 */
	public void setAPModeChange(String contactId, String password, int value) {
		if (this.MESG_TYPE_SET_AP_MODE_CHANGE >= (Constants.MsgSection.MESG_TYPE_SET_AP_MODE_CHANGE)) {
			this.MESG_TYPE_SET_AP_MODE_CHANGE = Constants.MsgSection.MESG_TYPE_SET_AP_MODE_CHANGE - 1000;
		}
		MediaPlayer.iSetNPCSettings(Integer.parseInt(contactId),
				Integer.parseInt(EntryPassword(password)), this.MESG_TYPE_SET_AP_MODE_CHANGE,
				Constants.P2P_SETTING.SETTING_TYPE.SETTING_ID_SET_WIFI_WORK_MODE,
				value);
		this.MESG_TYPE_SET_AP_MODE_CHANGE++;
	}
	public void setZoom(String contactId, String password, int state) {
		Log.e(TAG, "P2PHANDLER:setRecordType");
		if (this.MESG_TYPE_SET_ZOOM >= (Constants.MsgSection.MESG_TYPE_SET_ZOOM)) {
			this.MESG_TYPE_SET_ZOOM = Constants.MsgSection.MESG_TYPE_SET_ZOOM - 1000;
		}

		MediaPlayer.iSetNPCSettings(Integer.parseInt(contactId),
				Integer.parseInt(EntryPassword(password)),
				this.MESG_TYPE_SET_ZOOM,
				Constants.P2P_SETTING.SETTING_TYPE.SETTING_ID_ZOOM, state);
		this.MESG_TYPE_SET_ZOOM++;
	}
	/**
	 * 璁剧疆AP妯″紡鍒囨彌  1鍒囨彌
	 */
	public void getNVRInfo(String contactId, String password) {
		if (this.MESG_TYPE_GET_NVRINFO >= (Constants.MsgSection.MESG_TYPE_GET_NVRINFO)) {
			this.MESG_TYPE_GET_NVRINFO = Constants.MsgSection.MESG_TYPE_GET_NVRINFO - 1000;
		}
		byte[] data = new byte[94];
		data[0] = (byte) 131;
		MediaPlayer.iExtendedCmd(Integer.parseInt(contactId),
				Integer.parseInt(EntryPassword(password)), this.MESG_TYPE_GET_NVRINFO,
				data, data.length);
		this.MESG_TYPE_GET_NVRINFO++;
	}
	//
	public void getFocusZoom(String contactId, String password){
		if (this.MESG_TYPE_GET_FOCUS_ZOOM >= (Constants.MsgSection.MESG_TYPE_GET_FOCUS_ZOOM)) {
			this.MESG_TYPE_GET_FOCUS_ZOOM = Constants.MsgSection.MESG_TYPE_GET_FOCUS_ZOOM - 1000;
		}
		byte[] data = new byte[4];
		data[0] = (byte) 224;
		MediaPlayer.iExtendedCmd(Integer.parseInt(contactId),
				Integer.parseInt(EntryPassword(password)), this.MESG_TYPE_GET_FOCUS_ZOOM,
				data, data.length);
		this.MESG_TYPE_GET_FOCUS_ZOOM++;
	}
	public void setFocusZoom(String contactId, String password,int value){
		if (this.MESG_TYPE_SET_FOCUS_ZOOM >= (Constants.MsgSection.MESG_TYPE_SET_FOCUS_ZOOM)) {
			this.MESG_TYPE_SET_FOCUS_ZOOM = Constants.MsgSection.MESG_TYPE_SET_FOCUS_ZOOM - 1000;
		}
		byte[] data = new byte[4];
		data[0] = (byte) 224;
		data[1]=(byte)1;
		data[3]=(byte) value;
		Log.e("focus_zoom", "focus_zoom="+value);
		MediaPlayer.iExtendedCmd(Integer.parseInt(contactId),
				Integer.parseInt(EntryPassword(password)), this.MESG_TYPE_SET_FOCUS_ZOOM,
				data, data.length);
		this.MESG_TYPE_SET_FOCUS_ZOOM++;
	}
	
	public byte[] getPwdBytes(String pwd){
		int m = pwd.length();
		int k = 8 - m % 8;
		for (int i = 0; i < k; i++) {
			pwd = pwd + "0";
		}
		byte[] ppp = null;
		try {
			ppp = DES.des(pwd.getBytes(), 0);
		} catch (Exception e) {
			ppp = new byte[] { 0 };
			e.printStackTrace();
		}
		return ppp;
	}
	
	public void setGPIO(String contactId, String password, int group, int pin,int[] data){
		if ((this.MESG_SET_GPIO) >= (Constants.MsgSection.MESG_SET_GPIO)) {
			this.MESG_SET_GPIO = Constants.MsgSection.MESG_SET_GPIO - 1000;
		}
		byte[] datas = new byte[37];
		datas[0] = 95;
		datas[1] = 0;
		datas[2] = (byte) group;
		datas[3] = (byte) pin;
		datas[4]=(byte) Math.min(8, data.length);
		for (int i = 0; i < datas[4]; i++) {
			byte[] io=intToByte4(data[i]);
			System.arraycopy(io, 0, datas, 5+i*io.length, io.length);
		}
		MediaPlayer.iExtendedCmd(Integer.parseInt(contactId),
				Integer.parseInt(EntryPassword(password)), this.MESG_SET_GPIO, datas,
				datas.length);
		this.MESG_SET_GPIO++;
	}
	public void getGPIO(String contactId, String password, int group, int pin,int[] data){
		if ((this.MESG_GET_GPIO) >= (Constants.MsgSection.MESG_GET_GPIO)) {
			this.MESG_GET_GPIO = Constants.MsgSection.MESG_GET_GPIO - 1000;
		}
		byte[] datas = new byte[37];
		datas[0] = 95;
		datas[1] = 0;
		datas[2] = (byte) group;
		datas[3] = (byte) pin;
		datas[4]=(byte)0xFF;
		MediaPlayer.iExtendedCmd(Integer.parseInt(contactId),
				Integer.parseInt(EntryPassword(password)), this.MESG_GET_GPIO, datas,
				datas.length);
		this.MESG_GET_GPIO++;
	}
	/**
	 * 获取定时布防信息
	 * @param contactId
	 * @param password
	 */
	public void getDefenceWorkGroup(String contactId, String password){
		if ((this.MESG_GET_DEFENCE_WORK_GROUP) >= (Constants.MsgSection.MESG_GET_DEFENCE_WORK_GROUP)) {
			this.MESG_GET_DEFENCE_WORK_GROUP = Constants.MsgSection.MESG_GET_DEFENCE_WORK_GROUP - 1000;
		}
		byte[] datas=new byte[64];
		datas[0]=(byte)214;
		datas[1]=0;
		MediaPlayer.iExtendedCmd(Integer.parseInt(contactId),
				Integer.parseInt(EntryPassword(password)), this.MESG_GET_DEFENCE_WORK_GROUP, datas, datas.length);
		this.MESG_GET_DEFENCE_WORK_GROUP++;
	}
	/**
	 * 设置定时布防信息
	 * @param contactId
	 * @param password
	 * @param groupCount
	 * @param data
	 */
	public void setDefenceWorkGroup(String contactId, String password,short groupCount,byte[] data){
		if ((this.MESG_SET_DEFENCE_WORK_GROUP) >= (Constants.MsgSection.MESG_SET_DEFENCE_WORK_GROUP)) {
			this.MESG_SET_DEFENCE_WORK_GROUP = Constants.MsgSection.MESG_SET_DEFENCE_WORK_GROUP - 1000;
		}
		byte[] datas=new byte[64];
		datas[0]=(byte)215;
		datas[1]=0;
		if(groupCount>10||data.length>60)return;
		byte[] count=MyUtils.shortToByte2(groupCount);
		System.arraycopy(count, 0, datas, 2, count.length);
		System.arraycopy(data, 0, datas, 4, data.length);
		Log.e("wxy", "p2p:"+Arrays.toString(datas));
		MediaPlayer.iExtendedCmd(Integer.parseInt(contactId),
				Integer.parseInt(EntryPassword(password)), this.MESG_SET_DEFENCE_WORK_GROUP, datas, datas.length);
		this.MESG_SET_DEFENCE_WORK_GROUP++;
	}
	
	public void getFTPConfigInfo(String contactId, String password){
		if ((this.MESG_GET_FTP_CONFIG_INFO) >= (Constants.MsgSection.MESG_GET_FTP_CONFIG_INFO)) {
			this.MESG_GET_FTP_CONFIG_INFO = Constants.MsgSection.MESG_GET_FTP_CONFIG_INFO - 1000;
		}
		byte[] data=new byte[102];
		data[0]=(byte)225;
		data[1]=0;
		MediaPlayer.iExtendedCmd(Integer.parseInt(contactId),
				Integer.parseInt(EntryPassword(password)), this.MESG_GET_FTP_CONFIG_INFO, data, data.length);
		this.MESG_GET_FTP_CONFIG_INFO++;
		
	}
	public void setFTPConfigInfo(String contactId, String password,String hostname,String username,String pass,short svrport,short usrflag){
		if ((this.MESG_SET_FTP_CONFIG_INFO) >= (Constants.MsgSection.MESG_SET_FTP_CONFIG_INFO)) {
			this.MESG_SET_FTP_CONFIG_INFO = Constants.MsgSection.MESG_SET_FTP_CONFIG_INFO - 1000;
		}
		byte[] data=new byte[102];
		data[0]=(byte)226;
		data[1]=0;
		byte[] hostnames=hostname.getBytes();
		byte[] usernames=username.getBytes();
		byte[] passwords=pass.getBytes();
		byte[] svrports=MyUtils.shortToByte2(svrport);
		byte[] usrflags=MyUtils.shortToByte2(usrflag);
		System.arraycopy(hostnames, 0, data, 2, hostnames.length);
		System.arraycopy(usernames, 0, data, 34, usernames.length);
		System.arraycopy(passwords, 0, data, 66, passwords.length);
		System.arraycopy(svrports, 0, data, 98, svrports.length);
		System.arraycopy(usrflags, 0, data, 100, usrflags.length);
		MediaPlayer.iExtendedCmd(Integer.parseInt(contactId),
				Integer.parseInt(EntryPassword(password)), this.MESG_GET_FTP_CONFIG_INFO, data, data.length);
		this.MESG_GET_FTP_CONFIG_INFO++;
	}
	
}
