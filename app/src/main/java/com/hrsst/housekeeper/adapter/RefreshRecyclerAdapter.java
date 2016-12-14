package com.hrsst.housekeeper.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.common.utils.T;
import com.hrsst.housekeeper.entity.AlarmMsg;
import com.hrsst.housekeeper.mvp.fragment.AlarmMsg.AlarmMsgPresenter;
import com.jakewharton.rxbinding.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class RefreshRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int PULLUP_LOAD_MORE = 0;//上拉加载更多
    public static final int LOADING_MORE = 1;//正在加载中
    public static final int NO_MORE_DATA = 2;//正在加载中
    public static final int NO_DATA = 3;//无数据
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private List<AlarmMsg.AlarmBean> alarmBeanList ;
    private Context mContext;
    private AlarmMsgPresenter alarmMsgPresenter;
    private String userId;
    private String privilege;

    public RefreshRecyclerAdapter(Activity mContext, List<AlarmMsg.AlarmBean> alarmBeanList
            , AlarmMsgPresenter alarmMsgPresenter, String userId, String privilege) {
        this.mInflater = LayoutInflater.from(mContext);
        this.alarmBeanList = alarmBeanList;
        this.mContext = mContext;
        this.alarmMsgPresenter = alarmMsgPresenter;
        this.userId = userId;
        this.privilege = privilege;
    }

    public RefreshRecyclerAdapter(Context mContext, List<AlarmMsg.AlarmBean> alarmBeanList
            , AlarmMsgPresenter alarmMsgPresenter) {
        this.mInflater = LayoutInflater.from(mContext);
        this.alarmBeanList = alarmBeanList;
        this.mContext = mContext;
        this.alarmMsgPresenter = alarmMsgPresenter;
    }

    /**
     * item显示类型
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            final View view = mInflater.inflate(R.layout.collect_fragment_adapter, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            ItemViewHolder viewHolder = new ItemViewHolder(view);
            return viewHolder;
        } else if (viewType == TYPE_FOOTER) {
            View foot_view = mInflater.inflate(R.layout.recycler_load_more_layout, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            FootViewHolder footViewHolder = new FootViewHolder(foot_view);
            return footViewHolder;
        }
        return null;
    }

    /**
     * 数据的绑定显示
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            AlarmMsg.AlarmBean alarmBean = alarmBeanList.get(position);
            int alarmType = alarmBean.getAlarmType();
            int ifDeal = alarmBean.getIfDealAlarm();
            final AlarmMsg.AlarmBean.CameraBean cameraBean = alarmBean.getCamera();
            ((ItemViewHolder) holder).alarmTimeTv.setText(alarmBean.getAlarmTime());
            ((ItemViewHolder) holder).smokeMacTv.setText(cameraBean.getAddrcameraNameess());
            ((ItemViewHolder) holder).repeaterAddressTv.setText(cameraBean.getCameraAddress());
            ((ItemViewHolder) holder).repeaterNameTv.setText(cameraBean.getPlaceType());
            ((ItemViewHolder) holder).repeaterMacTv.setText(cameraBean.getAreaName());
            ((ItemViewHolder) holder).userSmokeMarkPrincipal.setText(cameraBean.getPrincipal1());
            ((ItemViewHolder) holder).userSmokeMarkPhoneTv.setText(cameraBean.getPrincipal1Phone());
            ((ItemViewHolder) holder).userSmokeMarkPrincipalTwo.setText(cameraBean.getPrincipal2());
            ((ItemViewHolder) holder).userSmokeMarkPhoneTvTwo.setText(cameraBean.getPrincipal2Phone());
            if (ifDeal == 0) {
                ((ItemViewHolder) holder).dealAlarmActionTv.setText("取消报警");
                ((ItemViewHolder) holder).dealAlarmActionTv.setVisibility(View.VISIBLE);
            } else {
                ((ItemViewHolder) holder).dealAlarmActionTv.setVisibility(View.GONE);
            }
            RxView.clicks(((ItemViewHolder) holder).userSmokeMarkPhoneTv).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    alarmMsgPresenter.telPhoneAction(mContext, cameraBean.getPrincipal1Phone());
                }
            });
            RxView.clicks(((ItemViewHolder) holder).userSmokeMarkPhoneTvTwo).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    alarmMsgPresenter.telPhoneAction(mContext, cameraBean.getPrincipal2Phone());
                }
            });
            holder.itemView.setTag(position);
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (load_more_status) {
                case PULLUP_LOAD_MORE:
                    footViewHolder.footViewItemTv.setText("上拉加载更多...");
                    break;
                case LOADING_MORE:
                    footViewHolder.footViewItemTv.setText("正在加载更多数据...");
                    break;
                case NO_MORE_DATA:
                    T.showShort(mContext, "没有更多数据");
                    footViewHolder.footer.setVisibility(View.GONE);
                    break;
                case NO_DATA:
                    footViewHolder.footer.setVisibility(View.GONE);
                    break;
            }
        }

    }

    /**
     * 进行判断是普通Item视图还是FootView视图
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return alarmBeanList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.alarm_time_tv)
        TextView alarmTimeTv;
        @Bind(R.id.smoke_mac)
        TextView smokeMac;
        @Bind(R.id.smoke_mac_tv)
        TextView smokeMacTv;
        @Bind(R.id.alarm_mark_image)
        ImageView alarmMarkImage;
        @Bind(R.id.repeater_address_tv)
        TextView repeaterAddressTv;
        @Bind(R.id.repeater_name_tv)
        TextView repeaterNameTv;
        @Bind(R.id.repeater_mac_tv)
        TextView repeaterMacTv;
        @Bind(R.id.user_smoke_mark_principal)
        TextView userSmokeMarkPrincipal;
        @Bind(R.id.user_smoke_mark_phone_tv)
        TextView userSmokeMarkPhoneTv;
        @Bind(R.id.user_smoke_mark_principal_two)
        TextView userSmokeMarkPrincipalTwo;
        @Bind(R.id.user_smoke_mark_phone_tv_two)
        TextView userSmokeMarkPhoneTvTwo;
        @Bind(R.id.deal_alarm_action_tv)
        TextView dealAlarmActionTv;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 底部FootView布局
     */
    public static class FootViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.foot_view_item_tv)
        TextView footViewItemTv;
        @Bind(R.id.footer)
        LinearLayout footer;
        public FootViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    //添加数据
    public void addItem(List<AlarmMsg.AlarmBean> alarmBeen) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        alarmBeen.addAll(alarmBeanList);
        alarmBeanList.removeAll(alarmBeanList);
        alarmBeanList.addAll(alarmBeen);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<AlarmMsg.AlarmBean> alarmBeen) {
        alarmBeanList.addAll(alarmBeen);
        notifyDataSetChanged();
    }

    /**
     * //上拉加载更多
     * PULLUP_LOAD_MORE=0;
     * //正在加载中
     * LOADING_MORE=1;
     * //加载完成已经没有更多数据了
     * NO_MORE_DATA=2;
     *
     * @param status
     */
    public void changeMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }
}
