package com.hrsst.housekeeper.mvp.fragment.MyFragment;

import com.hrsst.housekeeper.common.basePresenter.BasePresenter;

/**
 * Created by Administrator on 2016/12/2.
 */
public class MyFragmentPresenter extends BasePresenter<MyFragmentView>{

    public MyFragmentPresenter (MyFragment myFragment){
        attachView(myFragment);
    }
}
