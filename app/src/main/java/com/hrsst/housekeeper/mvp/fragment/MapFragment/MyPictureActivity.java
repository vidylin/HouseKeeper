package com.hrsst.housekeeper.mvp.fragment.MapFragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.hrsst.housekeeper.R;
import com.hrsst.housekeeper.common.widget.NormalDialog;

import java.io.File;
import java.io.FileFilter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/8.
 */
public class MyPictureActivity extends Activity {
    @Bind(R.id.list_grid)
    GridView listGrid;
    @Bind(R.id.l_no_pictrue)
    LinearLayout lNoPictrue;
    @Bind(R.id.bg)
    View mBg;
    @Bind(R.id.img)
    PhotoView mPhotoView;
    @Bind(R.id.parent)
    View mParent;
    private Context mContext;
    private File[] files;
    private File[] data;
    private Info mInfo;
    private AlphaAnimation in = new AlphaAnimation(0, 1);
    private AlphaAnimation out = new AlphaAnimation(1, 0);
    private BaseAdapter baseAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map);
        ButterKnife.bind(this);
        mContext = this;
        if (null == files) {
            files = new File[0];
        }
        init();
    }

    private void init(){
        in.setDuration(300);
        out.setDuration(300);
        out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBg.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        initComponent();

        baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return data.length;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                PhotoView p = new PhotoView(mContext);
                p.setLayoutParams(new AbsListView.LayoutParams((int) (getResources().getDisplayMetrics().density * 120), (int) (getResources().getDisplayMetrics().density * 90)));
                p.setScaleType(ImageView.ScaleType.CENTER_CROP);
                final String path = data[position].getPath();
                Drawable drawable = Drawable.createFromPath(path);
                p.setImageDrawable(drawable);
                // 把PhotoView当普通的控件把触摸功能关掉
                p.disenable();
                return p;
            }
        };

        listGrid.setAdapter(baseAdapter);
        listGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhotoView p = (PhotoView) view;
                mInfo = p.getInfo();
                final String path = data[position].getPath();
                Drawable drawable = Drawable.createFromPath(path);
                mPhotoView.setImageDrawable(drawable);
                mBg.startAnimation(in);
                mBg.setVisibility(View.VISIBLE);
                mParent.setVisibility(View.VISIBLE);
                mPhotoView.animaFrom(mInfo);
            }
        });

        listGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                NormalDialog dialog = new NormalDialog(mContext, mContext
                        .getResources().getString(R.string.delete), mContext
                        .getResources().getString(R.string.confirm_delete),
                        mContext.getResources().getString(R.string.delete),
                        mContext.getResources().getString(R.string.cancel));
                dialog.setOnButtonOkListener(new NormalDialog.OnButtonOkListener() {

                    @Override
                    public void onClick() {
                        // TODO Auto-generated method stub
                        File f = data[i];
                        try {
                            f.delete();
                            updateData();
                        } catch (Exception e) {
                        }
                    }
                });
                dialog.showDialog();
                return true;
            }
        });

        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBg.startAnimation(out);
                mPhotoView.animaTo(mInfo, new Runnable() {
                    @Override
                    public void run() {
                        mParent.setVisibility(View.GONE);
                    }
                });
            }
        });

        mPhotoView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mPhotoView.enable();
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBg.startAnimation(out);
                mPhotoView.animaTo(mInfo, new Runnable() {
                    @Override
                    public void run() {
                        mParent.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private void initComponent() {
        String path = Environment.getExternalStorageDirectory().getPath()
                + "/screenshot";
        File file = new File(path);
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                // TODO Auto-generated method stub
                if (pathname.getName().endsWith(".jpg")) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        data = file.listFiles(filter);
        if (null == data) {
            data = new File[0];
        }
        showHideNoPictrue(data);
    }

    private void showHideNoPictrue(File[] data) {
        if (data.length <= 0) {
            lNoPictrue.setVisibility(View.VISIBLE);
        } else {
            lNoPictrue.setVisibility(View.GONE);
        }
    }

    public void updateData() {
        String path = Environment.getExternalStorageDirectory().getPath()
                + "/screenshot";
        File file = new File(path);
        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                // TODO Auto-generated method stub
                if (pathname.getName().endsWith(".jpg")) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        File[] files = file.listFiles(filter);
        data = files;
        showHideNoPictrue(data);
        baseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
