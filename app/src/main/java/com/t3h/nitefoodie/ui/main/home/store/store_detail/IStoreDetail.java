package com.t3h.nitefoodie.ui.main.home.store.store_detail;

import com.t3h.nitefoodie.model.FoodOrder;
import com.t3h.nitefoodie.model.Order;
import com.t3h.nitefoodie.model.Store;
import com.t3h.nitefoodie.ui.base.IBasePresenter;
import com.t3h.nitefoodie.ui.base.IViewMain;

public class IStoreDetail {
    interface View extends IViewMain {
        void finishGetStoreDetail(Store store);
        void onFoodOrderFinish();
    }

    interface Presenter extends IBasePresenter {
        void getStoreInfo(String sId);

        void createOrder(Order order);

        void onUpdateFoodToOrder(String orderId, FoodOrder foodOrder);

        void addToFavorite(String sId);
        void removeFromFavorite(String sId);
    }
}
