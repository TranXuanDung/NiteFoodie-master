package com.t3h.nitefoodie.ui.main.cart.order_detail;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.t3h.nitefoodie.R;
import com.t3h.nitefoodie.common.Constants;
import com.t3h.nitefoodie.model.FoodOrder;
import com.t3h.nitefoodie.model.Order;
import com.t3h.nitefoodie.model.Store;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailDialog extends Dialog implements FoodOrderAdapter.IFoodAdapter, View.OnClickListener {
    private TextView tvOrderId;
    private TextView tvStoreName;
    private TextView tvTotalPrice;
    private Button btnConfirmOrder;
    private RecyclerView rcFood;
    private FoodOrderAdapter mAdapter;
    private Order mOrder;
    private List<FoodOrder> foodOrderList;
    private OnClickButton mInterf;

    public OrderDetailDialog(@NonNull Context context, Order order, OnClickButton interf) {
        super(context);
        mOrder = order;
        mInterf = interf;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.order_detail);

        findViewByIds();
        initComponents();
        setEvents();
    }


    private void findViewByIds() {
        rcFood = (RecyclerView) findViewById(R.id.rc_food);
        tvOrderId = (TextView) findViewById(R.id.tv_order_id);
        tvStoreName = (TextView) findViewById(R.id.tv_store_name);
        tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
        btnConfirmOrder = (Button) findViewById(R.id.btn_confirm_orer);
    }

    private void setEvents() {
        btnConfirmOrder.setOnClickListener(this);
    }

    private void initComponents() {
        foodOrderList = new ArrayList<>();
        mAdapter = new FoodOrderAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcFood.setLayoutManager(linearLayoutManager);
        rcFood.setAdapter(mAdapter);

        tvOrderId.setText(mOrder.getId());
        DatabaseReference data = FirebaseDatabase.getInstance().getReference();
        data.child(Constants.STORES).child(mOrder.getStoreId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Store store = dataSnapshot.getValue(Store.class);
                tvStoreName.setText(store.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        for (FoodOrder foodOrder : mOrder.getFoodOrders().values()) {
            foodOrderList.add(foodOrder);
            mAdapter.notifyDataSetChanged();
        }

        tvTotalPrice.setText(mOrder.getTotalPrice() + "d");
        if (mOrder.getState().equals(Constants.ORDER_STATE_WAITING)) {
            btnConfirmOrder.setVisibility(View.VISIBLE);
        } else {
            btnConfirmOrder.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getCount() {
        return foodOrderList.size();
    }

    @Override
    public FoodOrder getFoodOrder(int position) {
        return foodOrderList.get(position);
    }

    @Override
    public void onClick(View v) {
        mInterf.onClick();
        dismiss();
    }

    public interface OnClickButton {
        void onClick();
    }
}
