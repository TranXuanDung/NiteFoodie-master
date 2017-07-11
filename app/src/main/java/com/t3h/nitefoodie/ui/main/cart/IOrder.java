package com.t3h.nitefoodie.ui.main.cart;

import com.t3h.nitefoodie.model.Order;
import com.t3h.nitefoodie.ui.base.IBasePresenter;
import com.t3h.nitefoodie.ui.base.IViewMain;

public class IOrder {
    interface View extends IViewMain {
        void onGetOrder(Order order);
        void finishUpdateOrder();
        void onOrderError();
    }

    interface Presenter extends IBasePresenter {
        void getListOrder();
        void updateOrder(Order order);
    }
}
