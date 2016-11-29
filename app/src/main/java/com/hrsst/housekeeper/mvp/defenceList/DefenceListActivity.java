package com.hrsst.housekeeper.mvp.defenceList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.hrsst.housekeeper.AppComponent;
import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.adapter.DefenceListAdapter;
import com.hrsst.housekeeper.common.baseActivity.BaseActivity;
import com.hrsst.housekeeper.common.data.Contact;
import com.hrsst.housekeeper.common.utils.T;
import com.hrsst.housekeeper.common.widget.NormalDialog;
import com.hrsst.housekeeper.entity.Defence;
import com.hrsst.housekeeper.mvp.apmonitor.ApMonitorActivity;
import com.hrsst.housekeeper.mvp.defence.DefenceActivity;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class DefenceListActivity extends BaseActivity implements DefenceListView {
    @Inject
    DefenceListPresenter defenceListPresenter;
    @Bind(R.id.main_image)
    ImageView mainImage;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipere_fresh_layout)
    SwipeRefreshLayout swipereFreshLayout;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.relative_add_defence)
    RelativeLayout relativeAddDefence;
    private LinearLayoutManager linearLayoutManager;
    private DefenceListAdapter defenceListAdapter;
    private List<Defence.DefenceBean> defenceBeanList;
    private Context mContext;
    private Contact contact;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerDefenceListComponent.builder()
                .appComponent(appComponent)
                .defenceListModule(new DefenceListModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defence_list);
        ButterKnife.bind(this);
        mContext = this;
        contact = (Contact) getIntent().getExtras().getSerializable("contact");
        refreshListView();
        defenceBeanList = new ArrayList<>();
        defenceListPresenter.getDefenceFromServer(contact,"",false);
        RxView.clicks(relativeAddDefence).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        defenceListPresenter.getDefenceArea();
                    }
                });
    }

    private void refreshListView() {
        //设置刷新时动画的颜色，可以设置4个
        swipereFreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipereFreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipereFreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        swipereFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                defenceListPresenter.getDefenceFromServer(contact,"",true);
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
    public void errorMessage(String msg) {
        T.showShort(mContext,msg);
    }

    @Override
    public void getDefenceArea(Defence defence) {
        Intent defenceIm = new Intent(mContext,DefenceActivity.class);
        defenceIm.putExtra("contact", contact);
        defenceIm.putExtra("defence", defence);
        startActivity(defenceIm);
        finish();
    }

    @Override
    public void getDefenceResult(String msg, Defence defence) {
        List<Defence.DefenceBean> list = defence.getDefence();
        if(list!=null&&list.size()>0){
            defenceBeanList.clear();
            defenceBeanList.addAll(list);
            defenceListAdapter = new DefenceListAdapter(this,defenceBeanList,defenceListPresenter);
            recyclerView.setAdapter(defenceListAdapter);
            defenceListAdapter.setOnItemLongClickListener(new DefenceListAdapter.OnRecyclerViewItemLongClickListener() {
                @Override
                public void onItemLongClick(View view, final Defence.DefenceBean data) {
                    final NormalDialog normalDialog = new NormalDialog(mContext);
                    normalDialog.deleteFireDialog();
                    normalDialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {
                        @Override
                        public void onClick() {
                            normalDialog.modifyDefenceNameDialog(data.getDefenceName(),defenceListPresenter);
                            normalDialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {
                                @Override
                                public void onClick() {
                                    defenceListPresenter.modifyDefenceName(data);
                                }
                            });
                        }
                    });
                    normalDialog.setOnButtonDeleteListener(new NormalDialog.OnButtonDeleteListener() {
                        @Override
                        public void onClick() {
                            defenceListPresenter.deleteDefenceDefence(data);
                        }
                    });
                }
            });
        }
        swipereFreshLayout.setRefreshing(false);
    }

    @Override
    public void refresh(Defence defence) {
        swipereFreshLayout.setRefreshing(false);
        List<Defence.DefenceBean> list = defence.getDefence();
        if(list!=null&&list.size()>0){
            defenceBeanList.clear();
            defenceBeanList.addAll(list);
            defenceListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void deleteDefenceResult(Defence.DefenceBean defenceBean) {
        defenceBeanList.remove(defenceBean);
        defenceListAdapter.notifyDataSetChanged();
    }

    @Override
    public void modifyDefenceNameResult(Defence.DefenceBean defenceBean) {
        int pos = defenceBean.getPos();
        defenceBeanList.set(pos,defenceBean);
        defenceListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        defenceListPresenter.unReceiver();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(mContext, ApMonitorActivity.class);
            i.putExtra("contact", contact);
            startActivity(i);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
