package com.hrsst.housekeeper.mvp.defenceList;

import com.hrsst.housekeeper.entity.Defence;

/**
 * Created by Administrator on 2016/11/28.
 */
public interface DefenceListView {
    void showLoading();
    void hideLoading();
    void errorMessage(String msg);
    void getDefenceArea(Defence defence);
    void getDefenceResult(String msg,Defence defence);
    void refresh(Defence defence);
    void deleteDefenceResult(Defence.DefenceBean defenceBean);
    void modifyDefenceNameResult(Defence.DefenceBean defenceBean);
}
