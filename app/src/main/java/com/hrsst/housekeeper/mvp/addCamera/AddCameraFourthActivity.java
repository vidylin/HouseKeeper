package com.hrsst.housekeeper.mvp.addCamera;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.hrsst.housekeeper.AppComponent;
import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.common.baseActivity.BaseActivity;
import com.hrsst.housekeeper.common.widget.XCDropDownListView;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

public class AddCameraFourthActivity extends BaseActivity implements AddCameraFourthView{
    @Inject
    AddCameraFourthPresenter addCameraFourthPresenter;
    @Bind(R.id.add_repeater_mac)
    EditText addRepeaterMac;
    @Bind(R.id.add_fire_mac)
    EditText addFireMac;
    @Bind(R.id.add_fire_lat)
    EditText addFireLat;
    @Bind(R.id.add_fire_lon)
    EditText addFireLon;
    @Bind(R.id.add_fire_address)
    EditText addFireAddress;
    @Bind(R.id.add_fire_man)
    EditText addFireMan;
    @Bind(R.id.add_fire_man_phone)
    EditText addFireManPhone;
    @Bind(R.id.add_fire_man_two)
    EditText addFireManTwo;
    @Bind(R.id.add_fire_man_phone_two)
    EditText addFireManPhoneTwo;
    @Bind(R.id.scan_repeater_ma)
    ImageView scanRepeaterMa;
    @Bind(R.id.scan_er_wei_ma)
    ImageView scanErWeiMa;
    @Bind(R.id.location_image)
    ImageView locationImage;
    @Bind(R.id.add_fire_zjq)
    XCDropDownListView addFireZjq;
    @Bind(R.id.add_fire_type)
    XCDropDownListView addFireType;
    @Bind(R.id.add_fire_dev_btn)
    RelativeLayout addFireDevBtn;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.add_camera_name)
    EditText addCameraName;
    @Bind(R.id.add_camera_relative)
    RelativeLayout addCameraRelative;
    private String contactId;
    private Context mContext;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_camera_four);
        ButterKnife.bind(this);
        mContext = this;
        contactId = getIntent().getExtras().getString("contactId");
        init();
    }

    private void init() {
        addCameraRelative.setVisibility(View.VISIBLE);
        addFireZjq.setEditTextHint("区域");
        addFireType.setEditTextHint("类型");
        RxView.clicks(addFireDevBtn).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

            }
        });
    }

    @OnClick({ R.id.location_image, R.id.add_fire_zjq, R.id.add_fire_type})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.location_image:
//                mvpPresenter.startLocation();
                break;
            case R.id.add_fire_zjq:
                if (addFireZjq.ifShow()) {
                    addFireZjq.closePopWindow();
                } else {
//                    mvpPresenter.getPlaceTypeId(userID, privilege + "", 2);
                    addFireZjq.setClickable(false);
                    addFireZjq.showLoading();
                }
                break;
            case R.id.add_fire_type:
                if (addFireType.ifShow()) {
                    addFireType.closePopWindow();
                } else {
//                    mvpPresenter.getPlaceTypeId(userID, privilege + "", 1);
                    addFireType.setClickable(false);
                    addFireType.showLoading();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (addFireZjq.ifShow()) {
            addFireZjq.closePopWindow();
        }
        if (addFireType.ifShow()) {
            addFireType.closePopWindow();
        }
        ButterKnife.unbind(this);
    }

}