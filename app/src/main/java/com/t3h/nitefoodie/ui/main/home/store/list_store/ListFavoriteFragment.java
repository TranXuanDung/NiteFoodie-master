package com.t3h.nitefoodie.ui.main.home.store.list_store;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.t3h.nitefoodie.R;
import com.t3h.nitefoodie.common.Constants;
import com.t3h.nitefoodie.model.Store;
import com.t3h.nitefoodie.ui.base.fragment.BaseMVPFragment;

import java.util.ArrayList;
import java.util.List;

public class ListFavoriteFragment extends BaseMVPFragment implements StoreAdapter.IStoreAdapter {

    private RecyclerView rcStoreAll;
    private StoreAdapter mAdapter;
    private List<Store> mStores;
    private List<String> mStoreIds;
    private String userId;
    private DatabaseReference mData;

    @Override
    public int getLayoutMain() {
        return R.layout.list_shop_fragment;
    }

    @Override
    public void findViewByIds() {
        rcStoreAll = (RecyclerView) getView().findViewById(R.id.rc_store_all);
    }

    @Override
    public void initComponents() {
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mData = FirebaseDatabase.getInstance().getReference();

        mStores = new ArrayList<>();
        mStoreIds = new ArrayList<>();
        mAdapter = new StoreAdapter(this, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcStoreAll.setLayoutManager(linearLayoutManager);
        rcStoreAll.setAdapter(mAdapter);

        mData.child(Constants.USERS).child(userId).child(Constants.FAVORITE_ID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                mData.child(Constants.STORES).child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Store store = dataSnapshot.getValue(Store.class);
                        mStores.add(store);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void setEvents() {

    }

    @Override
    public int getCount() {
        return mStores.size();
    }

    @Override
    public Store getPosition(int position) {
        return mStores.get(position);
    }
}
