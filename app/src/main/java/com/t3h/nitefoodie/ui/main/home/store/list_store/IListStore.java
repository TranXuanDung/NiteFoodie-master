package com.t3h.nitefoodie.ui.main.home.store.list_store;

import com.t3h.nitefoodie.model.Store;
import com.t3h.nitefoodie.ui.base.IBasePresenter;
import com.t3h.nitefoodie.ui.base.IViewMain;

public class IListStore {
    interface View extends IViewMain {
        void getListStore(Store store);
        void onStoreChanged(Store store);
        void onStoreRemoved(Store store);
        void onDataLoadError(String error);
    }

    interface Presenter extends IBasePresenter {
        void getListStore();
    }
}
