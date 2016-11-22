package com.hrsst.housekeeper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.common.data.Contact;
import com.hrsst.housekeeper.common.widget.HeaderView;
import com.hrsst.housekeeper.mvp.fragment.DevFragment.DevFragmentPresenter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/8.
 */
public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<Contact> constantsList;
    public static final int PULLUP_LOAD_MORE = 0;//上拉加载更多
    public static final int LOADING_MORE = 1;//正在加载中
    public static final int NO_MORE_DATA = 2;//正在加载中
    public static final int NO_DATA = 3;//无数据
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private DevFragmentPresenter devFragmentPresenter;

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(view, (Contact) view.getTag());
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Contact data);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public ContactAdapter(Context mContext, List<Contact> constantsList, DevFragmentPresenter devFragmentPresenter) {
        this.mContext = mContext;
        this.constantsList = constantsList;
        this.mInflater = LayoutInflater.from(mContext);
        this.devFragmentPresenter = devFragmentPresenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View view = mInflater.inflate(R.layout.contact_adapter, parent, false);
        //这边可以做一些属性设置，甚至事件监听绑定
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Contact contact = constantsList.get(position);
        ((ItemViewHolder) holder).cameraName.setText(contact.getContactName());
        ((ItemViewHolder) holder).userIcon.updateImage(contact.contactId, false, contact.contactType);
        int onLineState = contact.onLineState;
        int state = contact.defenceState;
        if (onLineState == 1) {
            ((ItemViewHolder) holder).lineState.setImageResource(R.mipmap.zx);
        } else {
            ((ItemViewHolder) holder).lineState.setImageResource(R.mipmap.lx);
        }
        if (state == 1) {
            ((ItemViewHolder) holder).lockImage.setImageResource(R.mipmap.suo_g);
        } else {
            ((ItemViewHolder) holder).lockImage.setImageResource(R.mipmap.suo_kai);
        }
        ((ItemViewHolder) holder).lockRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                devFragmentPresenter.setDefence(mContext, contact.contactId, contact.contactPassword, contact.defenceState);
            }
        });
        holder.itemView.setTag(contact);

    }

    @Override
    public int getItemCount() {
        return constantsList.size();
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
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    //添加数据
    public void addItem(List<Contact> list) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        list.addAll(constantsList);
        constantsList.removeAll(constantsList);
        constantsList.addAll(list);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<Contact> list) {
        constantsList.addAll(list);
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


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.camera_name)
        TextView cameraName;
        @Bind(R.id.user_icon)
        HeaderView userIcon;
        @Bind(R.id.lock_image)
        ImageView lockImage;
        @Bind(R.id.modify_camera)
        RelativeLayout modifyCamera;
        @Bind(R.id.line_state)
        ImageView lineState;
        @Bind(R.id.lock_relative)
        RelativeLayout lockRelative;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
