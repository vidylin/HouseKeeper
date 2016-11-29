package com.hrsst.housekeeper.common.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hrsst.housekeeper.AppApplication;
import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.adapter.SelectorDialogAdapter;
import com.hrsst.housekeeper.common.data.Contact;
import com.hrsst.housekeeper.common.global.Constants;
import com.hrsst.housekeeper.common.utils.Utils;
import com.hrsst.housekeeper.mvp.defenceList.DefenceListPresenter;
import com.p2p.core.P2PHandler;
import com.p2p.core.update.UpdateManager;
import com.p2p.core.utils.MyUtils;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class NormalDialog {
	Context context;
	String[] list_data = new String[] {};
	String title_str, content_str, btn1_str, btn2_str,btn3_str;
	AlertDialog dialog;
	private int loadingMark;
	private OnButtonOkListener onButtonOkListener;
	private OnButtonDeleteListener onButtonDeleteListener;
	private OnButtonCancelListener onButtonCancelListener;
	private OnCancelListener onCancelListener;
	private OnCustomCancelListner customCancelListner;
	private Contact mContact;
	private String message;
	private String urlStr;
	private OnDismissListener onDismissListener = new OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface dialog) {
			if (mTimer != null) {
				mTimer.cancel();
			}
		}
	};
	private OnItemClickListener onItemClickListener;
	private int style = 999;

	public static final int DIALOG_STYLE_NORMAL = 1;
	public static final int DIALOG_STYLE_LOADING = 2;
	public static final int DIALOG_STYLE_UPDATE = 3;
	public static final int DIALOG_STYLE_DOWNLOAD = 4;
	public static final int DIALOG_STYLE_PROMPT = 5;

	public NormalDialog(Context context, String title, String content,
			String btn1, String btn2) {
		this.context = context;
		this.title_str = title;
		this.content_str = content;
		this.btn1_str = btn1;
		this.btn2_str = btn2;
	}

	public NormalDialog(Context context) {
		this.context = context;
		this.title_str = "";
		this.content_str = "";
		this.btn1_str = "";
		this.btn2_str = "";
	}
	public NormalDialog(Context context,String message, final String urlStr) {
		this.context = context;
		this.message = message;
		this.urlStr = urlStr;
	}


	public void showDialog() {
		switch (style) {
		case DIALOG_STYLE_NORMAL:
			showNormalDialog();
			break;
		case DIALOG_STYLE_PROMPT:
			showPromptDialog();
			break;
		case DIALOG_STYLE_LOADING:
			showLoadingDialog();
			break;
		default:
			showNormalDialog();
			break;
		}
	}

	// 保存/查看预置位位置范围： 0 ~ 4
	public void yzwDialog(final byte bPresetNum, final String callId, final String password){
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View myLoginView = layoutInflater.inflate(R.layout.dialog_yuzhiwei, null);
		Button szyzw = (Button) myLoginView.findViewById(R.id.szyzw_btn);
		Button ckyzw = (Button) myLoginView.findViewById(R.id.ckyzw_btn);
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
		dialog = alertDialog.create();
		dialog.show();
		dialog.setContentView(myLoginView);
		dialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		//设置预置位
		szyzw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				byte[] data = new byte[20];
				data[0] = 87;
				data[1] = 0;
				data[2] = 1;
				data[3] = bPresetNum;
				P2PHandler.getInstance().sMesgPresetMotorPos(callId, password, data);
				dialog.dismiss();
			}
		});
		//查看预置位
		ckyzw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				byte[] data = new byte[20];
				data[0] = 87;
				data[1] = 0;
				data[2] = 0;
				data[3] = bPresetNum;
				P2PHandler.getInstance().sMesgPresetMotorPos(callId, password, data);
				dialog.dismiss();
			}
		});

	}

	public void showVersionDialog(){
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_update, null);
		TextView title = (TextView) view.findViewById(R.id.title_text);
		WebView content = (WebView) view.findViewById(R.id.content_text);
		TextView button1 = (TextView) view
				.findViewById(R.id.button1_text);
		TextView button2 = (TextView) view
				.findViewById(R.id.button2_text);
		ImageView minddle_image = (ImageView) view
				.findViewById(R.id.minddle_image);
		RelativeLayout cancel_rela_dialog = (RelativeLayout) view
				.findViewById(R.id.cancel_rela_dialog);
		title.setText("更新");
		if(urlStr==null){
			content.setBackgroundColor(context.getResources().getColor(R.color.update_message)); // 设置背景色
			content.getBackground().setAlpha(255); // 设置填充透明度 范围：0-255
			content.loadDataWithBaseURL(null, "已是最新版本！", "text/html", "utf-8",
					null);
			minddle_image.setVisibility(View.GONE);
			cancel_rela_dialog.setVisibility(View.GONE);
			button2.setText("确定");
			button2.setTextColor(Color.BLACK);
			button2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (null != dialog) {
						dialog.cancel();
					}
				}
			});
		}else{
			content.setBackgroundColor(Color.WHITE); // 设置背景色
			content.getBackground().setAlpha(100); // 设置填充透明度 范围：0-255
			content.loadDataWithBaseURL(null, message, "text/html", "utf-8",
					null);
			button1.setText("立即更新");
			button2.setText("下次再说");
			button1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (null != dialog) {
						dialog.dismiss();
						dialog = null;
					}
					if (UpdateManager.getInstance().getIsDowning()) {
						return;
					}
					AppApplication.context.showDownNotification(
							UpdateManager.HANDLE_MSG_DOWNING, 0);
					new Thread() {
						public void run() {
							UpdateManager.getInstance().downloadApk(getHandler(), Constants.Update.SAVE_PATH,
									Constants.Update.FILE_NAME, urlStr);
						}
					}.start();
				}
			});
			button2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (null != dialog) {
						dialog.cancel();
					}
				}
			});
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog = builder.create();
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		dialog.setContentView(view);
	}

	private Handler getHandler(){
		Handler handler = new Handler() {
			long last_time;

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				int value = msg.arg1;
				switch (msg.what) {
					case UpdateManager.HANDLE_MSG_DOWNING:
						if ((System.currentTimeMillis() - last_time) > 1000) {
							AppApplication.context.showDownNotification(
									UpdateManager.HANDLE_MSG_DOWNING, value);
							last_time = System.currentTimeMillis();
						}
						break;
					case UpdateManager.HANDLE_MSG_DOWN_SUCCESS:
						AppApplication.context.hideDownNotification();
						Intent intent = new Intent(Intent.ACTION_VIEW);
						File file = new File(Environment.getExternalStorageDirectory()
								+ "/" + Constants.Update.SAVE_PATH + "/"
								+ Constants.Update.FILE_NAME);
						if (!file.exists()) {
							return;
						}
						intent.setDataAndType(Uri.fromFile(file),
								Constants.Update.INSTALL_APK);
						context.startActivity(intent);
						break;
					case UpdateManager.HANDLE_MSG_DOWN_FAULT:
						break;
				}
			}
		};
		return handler;
	}

	public void showAlarmDialog(final boolean surrportDelete,final String id){
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_alarm, null);
		TextView content = (TextView) view.findViewById(R.id.content_text);
		TextView button1 = (TextView) view.findViewById(R.id.button1_text);
		TextView button2 = (TextView) view.findViewById(R.id.button2_text);
		TextView btnDelete = (TextView) view.findViewById(R.id.button3_text);
		ImageView iv=(ImageView) view.findViewById(R.id.iv_line_shu);
		content.setText(content_str);
		button1.setText(btn1_str);
		button2.setText(btn2_str);
		if(surrportDelete){
			btnDelete.setVisibility(View.VISIBLE);
			iv.setVisibility(View.VISIBLE);
			btnDelete.setText(btn3_str);
		}else{
			btnDelete.setVisibility(View.GONE);
			iv.setVisibility(View.GONE);
		}

		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(AlarmClickListner!=null){
					AlarmClickListner.onOkClick(id, surrportDelete, dialog);
				}else{
					if (null != dialog) {
						dialog.cancel();
					}
				}
			}
		});
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(AlarmClickListner!=null){
					AlarmClickListner.onCancelClick(id, surrportDelete, dialog);
				}else{
					if (null != dialog) {
						dialog.cancel();
					}
				}
			}
		});
		btnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(AlarmClickListner!=null){
					AlarmClickListner.onDeleteClick(id, surrportDelete, dialog);
				}else{
					if (null != dialog) {
						dialog.cancel();
					}
				}

			}
		});
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog = builder.create();
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
		dialog.show();
		dialog.setContentView(view);
		FrameLayout.LayoutParams layout = (LayoutParams) view.getLayoutParams();
		layout.width = (int) context.getResources().getDimension(
				R.dimen.normal_dialog_width);
		view.setLayoutParams(layout);
		dialog.setCanceledOnTouchOutside(true);
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialog_normal);
	}

	public void showLoadingDialog() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_loading, null);
		TextView title = (TextView) view.findViewById(R.id.title_text);
		title.setText(title_str);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog = builder.create();
		dialog.show();
		dialog.setContentView(view);
		LayoutParams layout = (LayoutParams) view.getLayoutParams();
		layout.width = (int) context.getResources().getDimension(
				R.dimen.Loading_dialog_width);
		view.setLayoutParams(layout);
		dialog.setOnCancelListener(onCancelListener);
		dialog.setOnDismissListener(onDismissListener);
		dialog.setCanceledOnTouchOutside(false);
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialog_normal);
	}

	public void showAboutDialog() {
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_about,
				null);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (null != dialog) {
					dialog.dismiss();
				}
			}

		});
		TextView txVersion = (TextView) view.findViewById(R.id.tv_about);
		txVersion.setText(MyUtils.getVersion());
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog = builder.create();
		dialog.show();
		dialog.setContentView(view);
		LayoutParams layout = (LayoutParams) view.getLayoutParams();
		layout.width = (int) context.getResources().getDimension(
				R.dimen.about_dialog_width);
		view.setLayoutParams(layout);
		dialog.setOnCancelListener(onCancelListener);
		dialog.setCanceledOnTouchOutside(true);
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialog_normal);
	}

	public void showDeviceInfoDialog(String curversion, String uBootVersion,
			String kernelVersion, String rootfsVersion) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_device_info, null);
		TextView text_curversion = (TextView) view
				.findViewById(R.id.text_curversion);
		TextView text_uBootVersion = (TextView) view
				.findViewById(R.id.text_uBootVersion);
		TextView text_kernelVersion = (TextView) view
				.findViewById(R.id.text_kernelVersion);
		TextView text_rootfsVersion = (TextView) view
				.findViewById(R.id.text_rootfsVersion);
		text_curversion.setText(curversion);
		text_uBootVersion.setText(uBootVersion);
		text_kernelVersion.setText(kernelVersion);
		text_rootfsVersion.setText(rootfsVersion);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (null != dialog) {
					dialog.dismiss();
				}
			}

		});
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog = builder.create();
		dialog.show();
		dialog.setContentView(view);
		LayoutParams layout = (LayoutParams) view.getLayoutParams();
		layout.width = (int) context.getResources().getDimension(
				R.dimen.device_info_dialog_width);
		view.setLayoutParams(layout);
		dialog.setOnCancelListener(onCancelListener);
		dialog.setCanceledOnTouchOutside(true);
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialog_normal);
	}

	public void showLoadingDialog2() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_loading2, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog = builder.create();
		dialog.show();
		dialog.setContentView(view);
		LayoutParams layout = (LayoutParams) view.getLayoutParams();
		layout.width = (int) context.getResources().getDimension(
				R.dimen.Loading_dialog2_width);
		view.setLayoutParams(layout);
		dialog.setOnCancelListener(onCancelListener);
		dialog.setCanceledOnTouchOutside(false);
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialog_normal);
	}

	public void deleteCamera(){

	}

	public void showNormalDialog() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_normal, null);
		TextView title = (TextView) view.findViewById(R.id.title_text);
		TextView content = (TextView) view.findViewById(R.id.content_text);
		TextView button1 = (TextView) view.findViewById(R.id.button1_text);
		TextView button2 = (TextView) view.findViewById(R.id.button2_text);
		title.setText(title_str);
		content.setText(content_str);
		button1.setText(btn1_str);
		button2.setText(btn2_str);
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != dialog) {
					dialog.dismiss();
				}
				onButtonOkListener.onClick();
			}
		});
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null == onButtonCancelListener) {
					if (null != dialog) {
						dialog.cancel();
					}
				} else {
					onButtonCancelListener.onClick();
				}
			}
		});
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog = builder.create();
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
		dialog.show();
		dialog.setContentView(view);
//		LayoutParams layout = (LayoutParams) view.getLayoutParams();
//		layout.width = (int) context.getResources().getDimension(
//				R.dimen.normal_dialog_width);
//
//		view.setLayoutParams(layout);
//		dialog.setCanceledOnTouchOutside(true);
//		Window window = dialog.getWindow();
//		window.setWindowAnimations(R.style.dialog_normal);
	}

	public void deleteFireDialog() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_defence, null);
		RelativeLayout deleteFire = (RelativeLayout) view.findViewById(R.id.delete_fire);
		RelativeLayout modifyFireName = (RelativeLayout) view.findViewById(R.id.dialog_content);
		deleteFire.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != dialog) {
					dialog.dismiss();
				}
				onButtonDeleteListener.onClick();

			}
		});
		modifyFireName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != dialog) {
					dialog.dismiss();
				}
				onButtonOkListener.onClick();
			}
		});
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog = builder.create();
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
		dialog.show();
		dialog.setContentView(view);
		WindowManager.LayoutParams layout = dialog.getWindow().getAttributes();
		layout.height=250;
		layout.width=450;
//		dialog.setCanceledOnTouchOutside(true);
		Window window = dialog.getWindow();
		window.setAttributes(layout);
		window.setWindowAnimations(R.style.dialog_normal);
	}

	public void modifyDefenceNameDialog(String oldName, final DefenceListPresenter defenceListPresenter) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_modify_defence_name, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog = builder.create();
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
		dialog.setView(view);
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.dialog_modify_defence_name);
		window.setWindowAnimations(R.style.dialog_normal);
		TextView okTextView = (TextView) dialog.findViewById(R.id.button1_text);
		TextView cancelTextView = (TextView) dialog.findViewById(R.id.button2_text);
		final EditText editText = (EditText) dialog.findViewById(R.id.new_defence_name);
		editText.setText(oldName);
		okTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != dialog) {
					dialog.dismiss();
				}
				String newName= editText.getText().toString().trim();
				defenceListPresenter.getNewName(newName);
				onButtonOkListener.onClick();
			}
		});
		cancelTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null == onButtonCancelListener) {
					if (null != dialog) {
						dialog.cancel();
					}
				} else {
					onButtonCancelListener.onClick();
				}
			}
		});
	}

	public void showSelectorDialog() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_selector, null);
		TextView title = (TextView) view.findViewById(R.id.title_text);
		title.setText(title_str);

		ListView content = (ListView) view.findViewById(R.id.content_text);

		SelectorDialogAdapter adapter = new SelectorDialogAdapter(context,
				list_data);
		content.setAdapter(adapter);
		content.setOnItemClickListener(onItemClickListener);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog = builder.create();
		dialog.show();
		dialog.setContentView(view);

		int itemHeight = (int) context.getResources().getDimension(
				R.dimen.selector_dialog_item_height);
		int margin = (int) context.getResources().getDimension(
				R.dimen.selector_dialog_margin);
		int separatorHeight = (int) context.getResources().getDimension(
				R.dimen.selector_dialog_separator_height);

		LayoutParams layout = (LayoutParams) view.getLayoutParams();
		layout.width = (int) context.getResources().getDimension(
				R.dimen.selector_dialog_width);
		layout.height = itemHeight * list_data.length + margin * 2
				+ (list_data.length - 1) * separatorHeight;
		view.setLayoutParams(layout);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialog_normal);
	}
	
	public String getContentStr(){
		return content_str;
	}
	
	public void setContentStr(int id){
		String str= Utils.getStringForId(id);
		setContentStr(str);
	}
		
	
	public void setContentStr(String contantStr){
		content_str=contantStr;
	}
	
	
	public String getbtnStr1(){
		return btn1_str;
	}
	
	public void setbtnStr1(int id){
		String str=Utils.getStringForId(id);
		setbtnStr1(str);
	}
		
	
	public void setbtnStr1(String btn1_st){
		btn1_str=btn1_st;
	}
	
	public String getbtnStr2(){
		return btn2_str;
	}
	
	public void setbtnStr2(int id){
		String str=Utils.getStringForId(id);
		setbtnStr2(str);
	}
		
	
	public void setbtnStr2(String btn2_st){
		btn2_str=btn2_st;
	}
	
	public String getbtnStr3(){
		return btn3_str;
	}
	
	public void setbtnStr3(int id){
		String str=Utils.getStringForId(id);
		setbtnStr3(str);
	}
		
	
	public void setbtnStr3(String btn3_st){
		btn3_str=btn3_st;
	}
	public void setmContact(Contact mContact){
		this.mContact=mContact;
	}
	public void showPromptDialog() {

		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_prompt, null);
		TextView content = (TextView) view.findViewById(R.id.content_text);
		TextView title = (TextView) view.findViewById(R.id.title_text);
		TextView button2 = (TextView) view.findViewById(R.id.button2_text);
		content.setText(content_str);
		title.setText(title_str);
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null == onButtonCancelListener) {
					if (null != dialog) {
						dialog.dismiss();
					}
				} else {
					onButtonCancelListener.onClick();
				}
			}
		});
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog = builder.create();
		dialog.show();
		dialog.setContentView(view);
		LayoutParams layout = (LayoutParams) view.getLayoutParams();
		layout.width = (int) context.getResources().getDimension(
				R.dimen.normal_dialog_width);

		view.setLayoutParams(layout);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialog_normal);
	}

	public void showPromoptDiaglog() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_promopt_box2, null);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (null != dialog) {
					dialog.dismiss();
				}
			}

		});
		Button bt1 = (Button) view.findViewById(R.id.bt_determine);
		bt1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (null != dialog) {
					dialog.dismiss();
				}
			}
		});
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog = builder.create();
		dialog.show();
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(true);
		LayoutParams layout = (LayoutParams) view.getLayoutParams();
		layout.width = (int) context.getResources().getDimension(
				R.dimen.dialog_promopt_width);
		layout.height = (int) context.getResources().getDimension(
				R.dimen.dialog_promopt_height);
		view.setLayoutParams(layout);
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialog_normal);
	}

	public void showWaitConnectionDialog() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_wait_connection, null);
		// view.setOnClickListener(new OnClickListener(){
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// if (null!=dialog) {
		// dialog.dismiss();
		// }
		// }
		//
		// });
		ImageView anim_load = (ImageView) view.findViewById(R.id.anim_wait);
		AnimationDrawable animationdrawable = (AnimationDrawable) anim_load
				.getDrawable();
		animationdrawable.start();
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog = builder.create();
		dialog.show();
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		LayoutParams layout = (LayoutParams) view.getLayoutParams();
		layout.width = (int) context.getResources().getDimension(
				R.dimen.dialog_remind_width);
		layout.height = (int) context.getResources().getDimension(
				R.dimen.dialog_reming_height);
		view.setLayoutParams(layout);
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialog_normal);
	}

	public void faildDialog() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_prompt_box1, null);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (null != dialog) {
					dialog.dismiss();
				}
			}

		});
		Button bt1 = (Button) view.findViewById(R.id.bt_determine);
		bt1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (dialog != null) {
					dialog.dismiss();
				}

			}
		});
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		dialog = builder.create();
		dialog.show();
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(true);
		LayoutParams layout = (LayoutParams) view.getLayoutParams();
		layout.width = (int) context.getResources().getDimension(
				R.dimen.dialog_promopt_width);
		layout.height = (int) context.getResources().getDimension(
				R.dimen.dialog_promopt_height);
		view.setLayoutParams(layout);
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.dialog_normal);
	}

	private Drawable getDrawableByteID(Context context,int id) {
//		if (Utils.isSpecification(Build.VERSION_CODES.LOLLIPOP)) {
//			return context.getResources().getDrawable(id, context.getTheme());
//		} else {
			return context.getResources().getDrawable(id);
//		}
	}


	public void setTitle(String title) {
		this.title_str = title;
	}

	public void setTitle(int id) {
		this.title_str = context.getResources().getString(id);
	}

	public void setListData(String[] data) {
		this.list_data = data;
	}

	public void setCanceledOnTouchOutside(boolean bool) {
		dialog.setCanceledOnTouchOutside(bool);
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public void setCancelable(boolean bool) {
		dialog.setCancelable(bool);
	}
	public String getBtn1_str() {
		return btn1_str;
	}

	public void setBtn1_str(String btn1_str) {
		this.btn1_str = btn1_str;
	}
	public void cancel() {
		dialog.cancel();
	}

	public void dismiss() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	public boolean isShowing() {
		return dialog.isShowing();
	}

	public void setBtnListener(TextView btn1, TextView btn2) {

	}

	public void setStyle(int style) {
		this.style = style;
	}

	public interface OnButtonOkListener {
		public void onClick();
	}

	public interface OnButtonDeleteListener {
		public void onClick();
	}

	public interface OnButtonCancelListener {
		public void onClick();
	}

	public void setOnButtonOkListener(OnButtonOkListener onButtonOkListener) {
		this.onButtonOkListener = onButtonOkListener;
	}

	public void setOnButtonDeleteListener(OnButtonDeleteListener onButtonDeleteListener) {
		this.onButtonDeleteListener = onButtonDeleteListener;
	}

	public void setOnButtonCancelListener(
			OnButtonCancelListener onButtonCancelListener) {
		this.onButtonCancelListener = onButtonCancelListener;
	}

	public void setOnCancelListener(OnCancelListener onCancelListener) {
		this.onCancelListener = onCancelListener;
		Log.i("dxsSMTP", "setlistener");
	}

	// 设置对话框超时
	private TimerTask reAddTask;
	private Timer mTimer;
	private Handler timeOutHandler;

	public void setTimeOut(long delay) {
		mTimer = new Timer();
		timeOutHandler = new Handler() {
			public void handleMessage(Message msg) {
				if(dialog!=null&&dialog.isShowing()){
					dialog.dismiss();
				}
				TimeOutListner.onTimeOut();
			};
		};
		reAddTask = new TimerTask() {
			@Override
			public void run() {
				timeOutHandler.sendEmptyMessage(0);
			}
		};
		mTimer.schedule(reAddTask, delay);
	}

	private OnNormalDialogTimeOutListner TimeOutListner;

	public interface OnNormalDialogTimeOutListner {
		public void onTimeOut();
	}

	public void setOnNormalDialogTimeOutListner(
			OnNormalDialogTimeOutListner TimeOutListner) {
		this.TimeOutListner = TimeOutListner;
	}

	// 取消自定义监听
	public interface OnCustomCancelListner {
		void onCancle(int mark);
	}

	public void setOnCustomCancelListner(
			OnCustomCancelListner customCancelListner) {
		this.customCancelListner = customCancelListner;
	}

	public void setLoadingMark(int loadingMark) {
		this.loadingMark = loadingMark;
	}
	/**
	 * 报警弹窗
	 * @author Administrator
	 *
	 */
	public interface OnAlarmClickListner{
		void onCancelClick(String alarmID, boolean isSurportDelete, Dialog dialog);
		void onOkClick(String alarmID, boolean isSurportDelete, Dialog dialog);
		void onDeleteClick(String alarmID, boolean isSurportDelete, Dialog dialog);
	}
	private OnAlarmClickListner AlarmClickListner;
	public void setOnAlarmClickListner(
			OnAlarmClickListner Listner) {
		this.AlarmClickListner = Listner;
	}
	//单选弹框使用
		public interface onDialogSingleSelectListner{
			void onItemClick(AlertDialog dialog, int position);
		}
		private onDialogSingleSelectListner select;
		public void setOnDialogSingleSelectListner(onDialogSingleSelectListner select){
			this.select=select;
		}

	public interface  onDialogItemClick{
		void onItemClick(View view, Contact mContact, int position, int contactPosition);
	}
	private onDialogItemClick itemClick;
	public void setOnDialogItemClick(onDialogItemClick itemClick){

		this.itemClick=itemClick;
	}

}
