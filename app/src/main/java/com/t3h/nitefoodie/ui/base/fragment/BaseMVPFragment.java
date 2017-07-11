package com.t3h.nitefoodie.ui.base.fragment;

import com.t3h.nitefoodie.ui.base.IBasePresenter;

public abstract class BaseMVPFragment<P extends IBasePresenter> extends BaseFragment {
    protected P mPresenter;

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        super.onDestroyView();
    }
}
