package com.t3h.nitefoodie.ui.main.home.store.list_store;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.t3h.nitefoodie.common.Constants;
import com.t3h.nitefoodie.model.Store;
import com.t3h.nitefoodie.ui.base.BasePresenter;

public class ListStorePresenter extends BasePresenter<IListStore.View> implements IListStore.Presenter, Constants {
    private DatabaseReference mData;

    public ListStorePresenter(IListStore.View view) {
        super(view);
    }

    @Override
    public void getListStore() {
        mData = FirebaseDatabase.getInstance().getReference().child(STORES);
        mData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Store store = dataSnapshot.getValue(Store.class);
                mView.getListStore(store);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Store store = dataSnapshot.getValue(Store.class);
                mView.onStoreChanged(store);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Store store = dataSnapshot.getValue(Store.class);
                mView.onStoreRemoved(store);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mView.onDataLoadError(databaseError.getDetails());
            }
        });
    }
}
