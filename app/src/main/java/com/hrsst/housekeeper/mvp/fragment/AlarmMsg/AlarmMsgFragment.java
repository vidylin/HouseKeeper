package com.hrsst.housekeeper.mvp.fragment.AlarmMsg;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hrsst.housekeeper.AppApplication;
import com.hrsst.housekeeper.AppComponent;
import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.adapter.RefreshRecyclerAdapter;
import com.hrsst.housekeeper.common.baseActivity.BaseFragment;
import com.hrsst.housekeeper.common.utils.SharedPreferencesManager;
import com.hrsst.housekeeper.common.utils.T;
import com.hrsst.housekeeper.entity.AlarmMsg;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/8.
 */
public class AlarmMsgFragment extends BaseFragment implements AlarmMsgView{

    @Inject
    AlarmMsgPresenter alarmMsgPresenter;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipere_fresh_layout)
    SwipeRefreshLayout swipereFreshLayout;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;
    private LinearLayoutManager linearLayoutManager;
    private Context mContext;
    private List<AlarmMsg.AlarmBean> alarmBeanList;
    private RefreshRecyclerAdapter refreshRecyclerAdapter;
    private int page;
    private int lastVisibleItem;
    private String privilege;
    private String userId;
    private boolean loadMore=false;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAlarmMsgComponent.builder()
                .appComponent(appComponent)
                .alarmMsgModule(new AlarmMsgModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_alarm, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        alarmBeanList = new ArrayList<>();
        refreshListView();
        userId = SharedPreferencesManager.getInstance().getData(mContext, SharedPreferencesManager.SP_FILE_GWELL,
                SharedPreferencesManager.KEY_RECENTPASS_NUMBER);
        privilege = AppApplication.privilege;
        page = 1;
        alarmMsgPresenter.getAllAlarmMsg(userId,privilege,page+"",false);
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
        linearLayoutManager=new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        refreshRecyclerAdapter = new RefreshRecyclerAdapter(mContext,alarmBeanList,alarmMsgPresenter);
        recyclerView.setAdapter(refreshRecyclerAdapter);
        refreshRecyclerAdapter.changeMoreStatus(RefreshRecyclerAdapter.NO_DATA);
        swipereFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMore=false;
                page = 1;
                alarmMsgPresenter.getAllAlarmMsg(userId,privilege,page+"",true);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(refreshRecyclerAdapter==null){
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == refreshRecyclerAdapter.getItemCount()) {
                    if (alarmBeanList != null && alarmBeanList.size() >= 20) {
                        loadMore=true;
                        page = page + 1;
                        alarmMsgPresenter.getAllAlarmMsg(userId,privilege,page+"",true);
                        mProgressBar.setVisibility(View.GONE);
                    }else{
                        refreshRecyclerAdapter.changeMoreStatus(RefreshRecyclerAdapter.NO_DATA);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    @Override
    public String getFragmentName() {
        return "AlarmMsgFragment";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
    public void errorMsg(String msg) {
        swipereFreshLayout.setRefreshing(false);
        T.showShort(mContext,msg);
    }

    @Override
    public void getAllMsg(List<AlarmMsg.AlarmBean> alarmBeen) {
        if(alarmBeen!=null&&alarmBeen.size()>0){
            if(loadMore==false){
                swipereFreshLayout.setRefreshing(false);
                alarmBeanList.clear();
            }
            alarmBeanList.addAll(alarmBeen);
            refreshRecyclerAdapter.notifyDataSetChanged();
        }
    }
}
