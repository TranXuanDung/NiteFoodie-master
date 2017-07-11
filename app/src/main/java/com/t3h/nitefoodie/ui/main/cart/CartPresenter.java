package com.t3h.nitefoodie.ui.main.cart;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.t3h.nitefoodie.common.Constants;
import com.t3h.nitefoodie.model.Order;
import com.t3h.nitefoodie.ui.base.BasePresenter;

public class CartPresenter extends BasePresenter<IOrder.View> implements IOrder.Presenter {
    private DatabaseReference mData;
    private String userId;

    public CartPresenter(IOrder.View view) {
        super(view);
        mData = FirebaseDatabase.getInstance().getReference().child(Constants.ORDERS);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public void getListOrder() {
        mData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Order order = dataSnapshot.getValue(Order.class);
                if (order.getUserId().equals(userId)) {
                    mView.onGetOrder(order);
                }
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
    public void updateOrder(Order order) {
        mData.child(order.getId()).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    mView.finishUpdateOrder();
                }else {
                    mView.onOrderError();
                }
            }
        });

    }
}
