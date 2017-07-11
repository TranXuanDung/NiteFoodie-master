package com.t3h.nitefoodie.ui.main.home.store.list_store;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.t3h.nitefoodie.R;
import com.t3h.nitefoodie.ui.base.fragment.BaseMVPFragment;
import com.t3h.nitefoodie.ui.main.home.store.HotSaleAdapter;

public class ListHotSaleFragment extends BaseMVPFragment implements HotSaleAdapter.IHotSaleAdapter {
    private RecyclerView rcStore;
    private HotSaleAdapter mAdapter;

    @Override
    public int getLayoutMain() {
        return R.layout.list_shop_fragment;
    }

    @Override
    public void findViewByIds() {
        rcStore = (RecyclerView) getView().findViewById(R.id.rc_store_all);
    }

    @Override
    public void initComponents() {
        mAdapter = new HotSaleAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rcStore.setLayoutManager(gridLayoutManager);
        rcStore.setAdapter(mAdapter);
    }

    @Override
    public void setEvents() {

    }

    @Override
    public int getCount() {
        return 10;
    }

}
