package com.hrsst.housekeeper.adapter;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.common.data.Contact;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2016/10/17.
 */
public class ViewPagerAdapter extends PagerAdapter {
    @Bind(R.id.shop_name)
    TextView shopName;
    @Bind(R.id.shop_address)
    TextView shopAddress;
    private Activity activity;
    private List<View> list;
    private List<Contact> contacts;

    public ViewPagerAdapter(List<View> list, Activity activity, List<Contact> contacts) {
        this.list = list;
        this.activity = activity;
        this.contacts = contacts;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(list.get(position));
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = list.get(position);
        ButterKnife.bind(this,view);
        ((ViewPager) container).addView(view, 0);
        Contact contact = contacts.get(position);
        shopName.setText(contact.contactName);
        return view;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
