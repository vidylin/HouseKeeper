package com.hrsst.housekeeper.mvp.defence;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.hrsst.housekeeper.AppComponent;
import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.common.baseActivity.BaseActivity;
import com.hrsst.housekeeper.common.data.Contact;
import com.hrsst.housekeeper.common.utils.T;
import com.hrsst.housekeeper.entity.Defence;
import com.hrsst.housekeeper.mvp.defenceList.DefenceListActivity;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class DefenceActivity extends BaseActivity implements DefenceView {
    @Inject
    DefencePresenter defencePresenter;
    @Bind(R.id.my_radio_group)
    RadioGroup myRadioGroup;
    @Bind(R.id.my_radio_group2)
    RadioGroup myRadioGroup2;
    @Bind(R.id.confirm)
    RelativeLayout confirm;
    @Bind(R.id.choice_image1)
    ImageView choiceImage1;
    @Bind(R.id.choice_image2)
    ImageView choiceImage2;
    @Bind(R.id.choice_image3)
    ImageView choiceImage3;
    @Bind(R.id.choice_image4)
    ImageView choiceImage4;
    @Bind(R.id.my_radio_group3)
    RadioGroup myRadioGroup3;
    @Bind(R.id.yzw_one)
    RadioButton yzwOne;
    @Bind(R.id.yzw_two)
    RadioButton yzwTwo;
    @Bind(R.id.yzw_three)
    RadioButton yzwThree;
    @Bind(R.id.yzw_four)
    RadioButton yzwFour;
    @Bind(R.id.yzw_five)
    RadioButton yzwFive;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.yzw_relative)
    RelativeLayout yzwRelative;
    private Context context;
    private Boolean changedGroup = false;
    private String clickStr;
    private List<ImageView> imageViews = new ArrayList<>();
    private String yzwNum;
    private Contact contact;
    private Defence defence;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerDefenceComponent.builder()
                .appComponent(appComponent)
                .defenceModule(new DefenceModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defence);
        ButterKnife.bind(this);
        context = this;
        contact = (Contact) getIntent().getExtras().getSerializable("contact");
        defence = (Defence) getIntent().getExtras().getSerializable("defence");
        myRadioGroup.setOnCheckedChangeListener(new MyRadioGroupOnCheckedChangedListener());
        myRadioGroup2.setOnCheckedChangeListener(new MyRadioGroupOnCheckedChangedListener());
        showYzw();
        init();
    }

    private void init() {
        imageViews.add(choiceImage1);
        imageViews.add(choiceImage2);
        imageViews.add(choiceImage3);
        imageViews.add(choiceImage4);
        RxView.clicks(confirm).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        defencePresenter.setDefence(yzwNum,contact,defence,clickStr);
                    }
                });
        myRadioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.yzw_one:
                        yzwNum = "0";
                        break;
                    case R.id.yzw_two:
                        yzwNum = "1";
                        break;
                    case R.id.yzw_three:
                        yzwNum = "2";
                        break;
                    case R.id.yzw_four:
                        yzwNum = "3";
                        break;
                    case R.id.yzw_five:
                        yzwNum = "4";
                        break;
                    default:
                        break;
                }
            }
        });
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
    public void studyErrorResult(String msg) {
        T.showShort(context, msg);
    }

    @Override
    public void studySuccessResult(String msg) {
        T.showShort(context, msg);
        Intent intent = new Intent(this, DefenceListActivity.class);
        intent.putExtra("contact", contact);
        startActivity(intent);
        finish();
    }

    private void showYzw() {
        int areaId = defence.getArea();
        if (areaId==1) {
            yzwRelative.setVisibility(View.VISIBLE);
        }
    }

    class MyRadioGroupOnCheckedChangedListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (!changedGroup) {
                changedGroup = true;
                if (group == myRadioGroup) {
                    myRadioGroup2.clearCheck();
                } else if (group == myRadioGroup2) {
                    myRadioGroup.clearCheck();
                }
                changedGroup = false;
            }
            switch (checkedId) {
                case R.id.my_radio_mc:
                    choiceGroup(0);
                    clickStr = "2";
                    break;
                case R.id.my_radio_hw:
                    choiceGroup(1);
                    clickStr = "3";
                    break;
                case R.id.my_radio_rq:
                    choiceGroup(2);
                    clickStr = "4";
                    break;
                case R.id.my_radio_yg:
                    choiceGroup(3);
                    clickStr = "1";
                    break;
                default:
                    break;
            }
        }
    }

    private void choiceGroup(int choiceNum) {
        for (int i = 0; i < 4; i++) {
            if (choiceNum - i == 0) {
                imageViews.get(i).setVisibility(View.VISIBLE);
            } else {
                imageViews.get(i).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(context, DefenceListActivity.class);
            i.putExtra("contact", contact);
            startActivity(i);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        defencePresenter.unReceiver();
    }
}
