package com.t3h.nitefoodie.ui.main.my_store;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.t3h.nitefoodie.common.Constants;
import com.t3h.nitefoodie.model.Food;
import com.t3h.nitefoodie.model.Store;
import com.t3h.nitefoodie.ui.base.BasePresenter;

public class MyStorePresenter extends BasePresenter<IMyStore.View> implements IMyStore.Presenter {

    private DatabaseReference mData;
    private String userId;

    public MyStorePresenter(IMyStore.View view) {
        super(view);
        mData = FirebaseDatabase.getInstance().getReference();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public void getMyStoreInfor() {
        mData.child(Constants.STORES).child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Store store = dataSnapshot.getValue(Store.class);
                mView.finishGetStore(store);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void deleteFood(Food food) {
        mData.child(Constants.STORES).child(userId).child(Constants.MENU).child(food.getFoodId()).removeValue();
    }
}
