package com.t3h.nitefoodie.ui.base;

public class BasePresenter<V extends IViewMain> implements IBasePresenter {

    protected V mView;

    public BasePresenter(V view) {
        mView = view;
    }

    @Override
    public void onDestroy() {

    }
}
