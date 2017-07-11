package com.t3h.nitefoodie.ui.main.home.store.store_detail;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.t3h.nitefoodie.common.Constants;
import com.t3h.nitefoodie.model.FoodOrder;
import com.t3h.nitefoodie.model.Order;
import com.t3h.nitefoodie.model.Store;
import com.t3h.nitefoodie.ui.base.BasePresenter;

public class StoreDetailPresenter extends BasePresenter<IStoreDetail.View> implements IStoreDetail.Presenter {
    private DatabaseReference mData;
    private String userId;

    public StoreDetailPresenter(IStoreDetail.View view) {
        super(view);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mData = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void getStoreInfo(String sId) {
        mData.child(Constants.STORES).child(sId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Store store = dataSnapshot.getValue(Store.class);
                mView.finishGetStoreDetail(store);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void createOrder(Order order) {
        mData.child(Constants.ORDERS).child(order.getId()).setValue(order);
    }

    @Override
    public void onUpdateFoodToOrder(String orderId, FoodOrder foodOrder) {
        mData.child(Constants.ORDERS).child(orderId).child(Constants.FOOD_ORDERS).child(foodOrder.getFoodId()).setValue(foodOrder);
        mView.onFoodOrderFinish();
    }

    @Override
    public void addToFavorite(String sId) {
        mData.child(Constants.USERS).child(userId).child(Constants.FAVORITE_ID).child(sId).setValue(sId);
    }

    @Override
    public void removeFromFavorite(String sId) {
        mData.child(Constants.USERS).child(userId).child(Constants.FAVORITE_ID).child(sId).removeValue();
    }
}
