package com.hrsst.housekeeper.mvp.fragment.MyFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hrsst.housekeeper.AppComponent;
import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.common.baseActivity.BaseFragment;
import com.hrsst.housekeeper.common.global.Constants;
import com.hrsst.housekeeper.common.utils.SharedPreferencesManager;
import com.hrsst.housekeeper.common.utils.T;
import com.hrsst.housekeeper.mvp.fragment.MapFragment.MyPictureActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/2.
 */
public class MyFragment extends BaseFragment implements MyFragmentView {
    @Inject
    MyFragmentPresenter myFragmentPresenter;
    @Bind(R.id.setting_image)
    ImageView settingImage;
    @Bind(R.id.my_picture)
    ImageView myPicture;
    @Bind(R.id.setting_user_id)
    TextView settingUserId;
    @Bind(R.id.setting_user_code)
    TextView settingUserCode;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_my, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();

        String userId = SharedPreferencesManager.getInstance().getData(mContext, SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTPASS_NUMBER);
        String userNum = SharedPreferencesManager.getInstance().getData(mContext, SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTNAME);
        settingUserId.setText(userId);
        settingUserCode.setText("编号:"+userNum);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMyFragmentComponent.builder()
                .appComponent(appComponent)
                .myFragmentModule(new MyFragmentModule(this))
                .build()
                .inject(this);
    }

    @OnClick({R.id.my_picture, R.id.app_update, R.id.setting_help_exit,R.id.system_msg_image,R.id.setting_help_about})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_picture:
                Intent intent = new Intent(mContext, MyPictureActivity.class);
                startActivity(intent);
                break;
            case R.id.app_update:
                Intent intent1 = new Intent();
                intent1.setAction(Constants.CHECK_VERSION_UPDATE);
                mContext.sendBroadcast(intent1);
                break;
            case R.id.setting_help_exit:
                Intent intent2 = new Intent();
                intent2.setAction(Constants.APP_EXIT);
                mContext.sendBroadcast(intent2);
                break;
            case R.id.system_msg_image:
                T.showShort(mContext,"暂无消息");
                break;
            case R.id.setting_help_about:
                Intent intent3 = new Intent(mContext,AboutActivity.class);
                startActivity(intent3);
                break;
            default:
                break;
        }
    }

    @Override
    public String getFragmentName() {
        return "MyFragment";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
