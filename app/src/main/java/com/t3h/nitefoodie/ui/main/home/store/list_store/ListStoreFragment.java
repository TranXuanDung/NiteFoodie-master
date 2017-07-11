package com.t3h.nitefoodie.ui.main.home.store.list_store;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.t3h.nitefoodie.R;
import com.t3h.nitefoodie.common.Constants;
import com.t3h.nitefoodie.model.Store;
import com.t3h.nitefoodie.ui.base.fragment.BaseMVPFragment;

import java.util.ArrayList;
import java.util.List;

public class ListStoreFragment extends BaseMVPFragment implements Constants, StoreAdapter.IStoreAdapter, IListStore.View {
    private static final String TAG = ListStoreFragment.class.getSimpleName();
    private RecyclerView rcStore;
    private StoreAdapter mStoreAdapter;
    private List<Store> stores;
    private ListStorePresenter mPresenter;

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
        mPresenter = new ListStorePresenter(this);
        stores = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        rcStore.setLayoutManager(linearLayoutManager);
        mStoreAdapter = new StoreAdapter(this, getContext());
        rcStore.setAdapter(mStoreAdapter);

        mPresenter.getListStore();
        // updateList();
    }

    @Override
    public void setEvents() {
    }

//    private void updateList() {
//        stores.clear();
//        mData.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                stores.add(dataSnapshot.getValue(Store.class));
//                //               HashMap<String, Food> map = stores.get(0).getMenu();
////                Iterator iterator = map.keySet().iterator();
////                while (iterator.hasNext()){
////                    String key = (String) iterator.next();
////                    Food food = (Food) map.get(key);
////                    Log.d(TAG, key+"________________________________________________"+food.getFoodId());
////                }
////                for (Food food : map.values()) {
////                    Log.d(TAG, "________________________________________" + food.getFoodId());
////                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                Store store = dataSnapshot.getValue(Store.class);
//
//                int index = getItemIndex(store);
//                stores.set(index, store);
//                mStoreAdapter.notifyItemChanged(index);
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                Store store = dataSnapshot.getValue(Store.class);
//
//                int index = getItemIndex(store);
//                stores.remove(index);
//                mStoreAdapter.notifyItemRemoved(index);
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }

    private int getItemIndex(Store store) {
        int index = -1;

        for (int i = 0; i < stores.size(); i++) {
            if (stores.get(i).getsId().equals(store.getsId())) {
                index = i;
                break;
            }
        }

        return index;
    }

    @Override
    public int getCount() {
        return stores.size();
    }

    @Override
    public Store getPosition(int position) {
        return stores.get(position);
    }


    @Override
    public void getListStore(Store store) {
        stores.add(store);
        mStoreAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStoreChanged(Store store) {
        int index = getItemIndex(store);
        stores.set(index, store);
        mStoreAdapter.notifyItemChanged(index);
    }

    @Override
    public void onStoreRemoved(Store store) {
        int index = getItemIndex(store);
        stores.remove(index);
        mStoreAdapter.notifyItemRemoved(index);
    }

    @Override
    public void onDataLoadError(String error) {

    }
}
