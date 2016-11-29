package com.hrsst.housekeeper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.entity.Defence;
import com.hrsst.housekeeper.mvp.defenceList.DefenceListActivity;
import com.hrsst.housekeeper.mvp.defenceList.DefenceListPresenter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DefenceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnLongClickListener {

    private DefenceListActivity defenceListActivity;
    private List<Defence.DefenceBean> defences;
    private DefenceListPresenter defenceListPresenter;
    private LayoutInflater mInflater;
    private OnRecyclerViewItemLongClickListener onRecyclerViewItemLongClickListener = null;

    @Override
    public boolean onLongClick(View view) {
        if (onRecyclerViewItemLongClickListener != null) {
            onRecyclerViewItemLongClickListener.onItemLongClick(view, (Defence.DefenceBean) view.getTag());
        }
        return true;
    }

    public static interface OnRecyclerViewItemLongClickListener {
        void onItemLongClick(View view, Defence.DefenceBean data);
    }

    public void setOnItemLongClickListener(OnRecyclerViewItemLongClickListener listener) {
        this.onRecyclerViewItemLongClickListener = listener;
    }


    public DefenceListAdapter(DefenceListActivity defenceListActivity, List<Defence.DefenceBean> defences, DefenceListPresenter defenceListPresenter) {
        this.defenceListActivity = defenceListActivity;
        this.defences = defences;
        this.defenceListPresenter = defenceListPresenter;
        this.mInflater = LayoutInflater.from(defenceListActivity);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.defence_list_item, parent, false);
        //这边可以做一些属性设置，甚至事件监听绑定
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        view.setOnLongClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Defence.DefenceBean bean = defences.get(position);
        bean.setPos(position);
        ((ItemViewHolder) holder).defenceName.setText(bean.getDefenceName());
        String devType = bean.getSensorId();
        switch (devType){
            case "1"://烟感
                ((ItemViewHolder) holder).defenceTypeName.setText("感烟探测器");
                ((ItemViewHolder) holder).defenceTypeImage.setImageResource(R.mipmap.fq_yg);
                break;
            case "2"://门磁
                ((ItemViewHolder) holder).defenceTypeName.setText("门磁探测器");
                ((ItemViewHolder) holder).defenceTypeImage.setImageResource(R.mipmap.fq_mc);
                break;
            case "3"://红外
                ((ItemViewHolder) holder).defenceTypeName.setText("红外探测器");
                ((ItemViewHolder) holder).defenceTypeImage.setImageResource(R.mipmap.fq_hw);
                break;
            case "4"://燃气
                ((ItemViewHolder) holder).defenceTypeName.setText("燃气探测器");
                ((ItemViewHolder) holder).defenceTypeImage.setImageResource(R.mipmap.fq_rq);
                break;
            default:
                break;
        }
        holder.itemView.setTag(bean);
    }

    @Override
    public int getItemCount() {
        return defences.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.defence_type_image)
        ImageView defenceTypeImage;
        @Bind(R.id.defence_name)
        TextView defenceName;
        @Bind(R.id.defence_type_name)
        TextView defenceTypeName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

