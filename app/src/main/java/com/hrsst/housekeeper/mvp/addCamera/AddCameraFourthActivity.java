package com.hrsst.housekeeper.mvp.addCamera;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.hrsst.housekeeper.AppComponent;
import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.common.baseActivity.BaseActivity;
import com.hrsst.housekeeper.common.global.Constants;
import com.hrsst.housekeeper.common.utils.SharedPreferencesManager;
import com.hrsst.housekeeper.common.utils.T;
import com.hrsst.housekeeper.common.widget.XCDropDownListView;
import com.hrsst.housekeeper.entity.Area;
import com.hrsst.housekeeper.entity.ShopType;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
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
    private ShopType mShopType;
    private Area mArea;
    private String areaId = "";
    private String shopTypeId = "";
    private String userNumber;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAddCameraFourthComponent.builder()
                .appComponent(appComponent)
                .addCameraFourthModule(new AddCameraFourthModule(this))
                .build()
                .inject(this);
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
        addRepeaterMac.setText(contactId);
        init();
        userNumber = SharedPreferencesManager.getInstance().getData(mContext, SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTPASS_NUMBER);
    }

    private void init() {
        addCameraRelative.setVisibility(View.VISIBLE);
        addFireZjq.setEditTextHint("区域");
        addFireType.setEditTextHint("类型");
        RxView.clicks(addFireDevBtn).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                addFire();
            }
        });
    }

    private void addFire() {
        if (mShopType != null) {
            shopTypeId = mShopType.getPlaceTypeId();
        }
        if (mArea != null) {
            areaId = mArea.getAreaId();
        }
        String longitude = addFireLon.getText().toString().trim();
        String latitude = addFireLat.getText().toString().trim();
        String smokeMac = addFireMac.getText().toString().trim();
        String address = addFireAddress.getText().toString().trim();
        String cameraName = addCameraName.getText().toString().trim();
        String principal1 = addFireMan.getText().toString().trim();
        String principal2 = addFireManTwo.getText().toString().trim();
        String principal1Phone = addFireManPhone.getText().toString().trim();
        String principal2Phone = addFireManPhoneTwo.getText().toString().trim();
        addCameraFourthPresenter.addCamera(userNumber,contactId,smokeMac,cameraName,address,longitude,
                latitude,principal1,principal1Phone,principal2,principal2Phone,
                areaId,shopTypeId);
    }

    @OnClick({ R.id.location_image, R.id.add_fire_zjq, R.id.add_fire_type})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.location_image:
                addCameraFourthPresenter.startLocation();
                break;
            case R.id.add_fire_zjq:
                if (addFireZjq.ifShow()) {
                    addFireZjq.closePopWindow();
                } else {
                    addCameraFourthPresenter.getPlaceTypeId(userNumber, "1", 2);
                    addFireZjq.setClickable(false);
                    addFireZjq.showLoading();
                }
                break;
            case R.id.add_fire_type:
                if (addFireType.ifShow()) {
                    addFireType.closePopWindow();
                } else {
                    addCameraFourthPresenter.getPlaceTypeId(userNumber, "1" , 1);
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
        addCameraFourthPresenter.stopLocation();
        super.onDestroy();
        if (addFireZjq.ifShow()) {
            addFireZjq.closePopWindow();
        }
        if (addFireType.ifShow()) {
            addFireType.closePopWindow();
        }
        ButterKnife.unbind(this);
    }

    @Override
    public void onStart() {
        addCameraFourthPresenter.initLocation();
        super.onStart();
    }

    @Override
    public void getLocationData(BDLocation location) {
        addFireLon.setText(location.getLongitude() + "");
        addFireAddress.setText(location.getAddrStr());
        addFireLat.setText(location.getLatitude() + "");
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void addCameraResult(String msg) {
        T.showShort(mContext,msg);
        Intent intent = new Intent();
        intent.setAction(Constants.PUSH_CAMERA_DATA);
        sendBroadcast(intent);
        finish();
    }

    @Override
    public void errorMessage(String msg) {
        T.showShort(mContext,msg);
    }

    @Override
    public void getShopType(ArrayList<Object> shopTypes) {
        addFireType.setItemsData(shopTypes,addCameraFourthPresenter);
        addFireType.showPopWindow();
        addFireType.setClickable(true);
        addFireType.closeLoading();
    }

    @Override
    public void getShopTypeFail(String msg) {
        T.showShort(mContext, msg);
        addFireType.setClickable(true);
        addFireType.closeLoading();
    }

    @Override
    public void getAreaType(ArrayList<Object> shopTypes) {
        addFireZjq.setItemsData(shopTypes,addCameraFourthPresenter);
        addFireZjq.showPopWindow();
        addFireZjq.setClickable(true);
        addFireZjq.closeLoading();
    }

    @Override
    public void getAreaTypeFail(String msg) {
        T.showShort(mContext, msg);
        addFireZjq.setClickable(true);
        addFireZjq.closeLoading();
    }

    @Override
    public void getChoiceArea(Area area) {
        mArea = area;
    }

    @Override
    public void getChoiceShop(ShopType shopType) {
        mShopType = shopType;
    }
}
