package com.hrsst.housekeeper.common.basePresenter;

import com.hrsst.housekeeper.entity.Area;
import com.hrsst.housekeeper.entity.ShopType;

/**
 * Created by Administrator on 2016/10/19.
 */
public interface  Presenter<V> {

    void attachView(V view);

    void detachView();

    void getArea(Area area);

    void getShop(ShopType shopType);
}
