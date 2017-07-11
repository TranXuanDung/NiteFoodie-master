package com.t3h.nitefoodie.ui.main.my_store;

import com.t3h.nitefoodie.model.Food;
import com.t3h.nitefoodie.model.Store;
import com.t3h.nitefoodie.ui.base.IBasePresenter;
import com.t3h.nitefoodie.ui.base.IViewMain;

public class IMyStore {
    interface View extends IViewMain{
        void finishGetStore(Store store);
    }

    interface Presenter extends IBasePresenter{
        void getMyStoreInfor();
        void deleteFood(Food food);
    }
}
