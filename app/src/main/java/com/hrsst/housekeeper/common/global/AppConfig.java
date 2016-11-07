package com.hrsst.housekeeper.common.global;

import android.os.Environment;

import com.p2p.core.global.Config;

import java.io.File;

/**
 * Created by dxs on 2015/8/28.
 */
public class AppConfig {
	 /**
     * 分辨率
     */
    public static int VideoMode=2;//0:流畅 1:高清 2标清
    /**
     * 调试相关参数，正式版记得改为合理值
     */
    public static class DeBug{
        public static final boolean isWrightAllLog=true;//是否写所有日志到SD卡
        public static final boolean isWrightErroLog=true;//是否记录错误日志到SD卡
    }

	/**
	 * 正式版参数
	 */
	public static class Relese {
		public static final String VERSION = Config.AppConfig.VERSION;
		public static final String APTAG = "GW_IPC_";
		public static final String PREPOINTPATH = Environment
				.getExternalStorageDirectory().getPath()
				+ File.separator
				+ "prepoint" + File.separator + NpcCommon.mThreeNum;
		public static final String SCREENSHORT = Environment
				.getExternalStorageDirectory().getPath()
				+ File.separator
				+ "screenshot";
		 /**报错日志文件夹名**/
        public static final String CRASHFILENAME="GwellErrorLog";    //根据实际APP更改文件夹名
        /**Crash路径**/
        public static final String CRASHPATH=Environment.getExternalStorageDirectory().getPath()+File.separator+CRASHFILENAME+ File.separator;
        /**Crash备份路径**/
        public static final String BACKUPLOGPATH=Environment.getExternalStorageDirectory().getPath()+File.separator+CRASHFILENAME+"_temp"+ File.separator;
        /**邮箱**/
        public static final byte[] EMAIL=new byte[]{-122, -36, 96, -112, 11, -20, -5, -101, -58, -122, -61, 122, -116, 40, -82, -8, -117, 63, -4, -32, 127, 49, 88, 41};
        /**密码**/
        public static final byte[] EMAILPWD=new byte[]{-48, -10, 75, -101, -57, 26, -91, -119, 61, 72, -121, -69, -30, 78, -1, 127};
        /**smtp端口号**/
        public static final String SMTP="smtp.exmail.qq.com";
        /**是否发送邮箱**/
        public static final boolean ifSendEmail=true;
	}

}
