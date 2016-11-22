package com.hrsst.housekeeper.mvp.fragment.OneKeyAlarmFragment;

import com.hrsst.housekeeper.common.basePresenter.BasePresenter;
import com.hrsst.housekeeper.common.data.Contact;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * Created by Administrator on 2016/11/10.
 */
public class OneKeyAlarmPresenter extends BasePresenter<OneKeyAlarmView>{
    private Subscription mSubscription;

    public OneKeyAlarmPresenter(OneKeyAlarmFragment oneKeyAlarmFragment){
        attachView(oneKeyAlarmFragment);
    }

    public void countdown(int time, final Contact contact, final String userId, final String privilege, final String info){
        if(contact==null){
            mvpView.getDataResult("网络错误，请检查网络是否通畅");
            return;
        }
        if (time < 0) time = 0;
        final int countTime = time;
        Observable<Integer> mObservable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long increaseTime) {
                        return countTime - increaseTime.intValue();
                    }
                })
                .take(countTime + 1);
        mSubscription = mObservable.doOnSubscribe(new Action0() {
            @Override
            public void call() {
                mvpView.getCurrentTime(countTime+"");
            }
        })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        //发送报警
                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(Integer integer) {
                        mvpView.getCurrentTime(integer.toString());
                    }
                });
    }

    public void stopCountDown(){
        if(mSubscription!=null){
            if(!mSubscription.isUnsubscribed()){
                mSubscription.unsubscribe();
                mSubscription=null;
                mvpView.stopCountDown("已取消报警");
            }
        }
    }

    private void textAlarm( String userId,String privilege, String smokeMac,String info){

    }

    public void getAllSmoke(String userId, String privilege){

    }
}
