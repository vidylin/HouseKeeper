package com.hrsst.housekeeper.common.basePresenter;

import android.content.Context;

import com.hrsst.housekeeper.common.utils.T;
import com.hrsst.housekeeper.common.utils.Utils;
import com.hrsst.housekeeper.common.widget.NormalDialog;
import com.hrsst.housekeeper.entity.Area;
import com.hrsst.housekeeper.entity.ShopType;
import com.hrsst.housekeeper.retrofit.ApiStores;
import com.hrsst.housekeeper.retrofit.AppClient;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class BasePresenter <V> implements Presenter<V>{
    public V mvpView;
    public ApiStores[] apiStore = {AppClient.retrofit().create(ApiStores.class),AppClient.retrofitTwo().create(ApiStores.class),AppClient.retrofitThree().create(ApiStores.class),AppClient.retrofitFour().create(ApiStores.class)};
    public ApiStores apiStoreServer = AppClient.retrofitServer().create(ApiStores.class);
//    public ApiStores apiStoreServerTest = AppClient.testRetrofitServer().create(ApiStores.class);
    private CompositeSubscription mCompositeSubscription;

    @Override
    public void attachView(V mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void detachView() {
        this.mvpView = null;
        onUnsubscribe();
    }

    @Override
    public void getArea(Area area) {

    }

    @Override
    public void getShop(ShopType shopType) {

    }

    public void telPhoneAction(Context mContext, String phoneNum){
        if(Utils.isPhoneNumber(phoneNum)){
            NormalDialog mNormalDialog = new NormalDialog(mContext, "是否需要拨打电话：", phoneNum,
                    "是", "否");
            mNormalDialog.showNormalDialog();
        }else{
            T.showShort(mContext, "电话号码不合法");
        }
    }

    //RXjava取消注册，以避免内存泄露
    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public void addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }
    public void twoSubscription(Observable observable, Func1 func1, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .flatMap(func1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }


}
