package com.hrsst.housekeeper.common.widget;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.PaintDrawable;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hrsst.housekeeper.R;


/**
 * Created by dxs on 2016/1/29.
 */
public class BasePopWindow extends PopupWindow {
    private Context context;
    private int h;

    public BasePopWindow(Context context,int h) {
        super(context);
        this.context = context;
        initPop(h);
    }

    public BasePopWindow(Context context, AttributeSet attrs,int h) {
        super(context, attrs);
        this.context = context;
        initPop(h);
    }

    private void initPop(int h) {
        setHeight(h);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置动画效果
        this.setAnimationStyle(R.style.popdown_up);
        this.setBackgroundDrawable(new PaintDrawable(0));
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 设置不被软键盘遮挡
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = 0.4f;
        ((Activity) context).getWindow().setAttributes(lp);
        this.setOnDismissListener(new OnDismissListener() {

            // 在dismiss中恢复透明度
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = ((Activity) context)
                        .getWindow().getAttributes();
                lp.alpha = 1f;
                ((Activity) context).getWindow().setAttributes(lp);
            }
        });
    }
}
