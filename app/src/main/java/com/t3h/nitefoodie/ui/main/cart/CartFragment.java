package com.t3h.nitefoodie.ui.main.cart;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.t3h.nitefoodie.R;
import com.t3h.nitefoodie.common.Constants;
import com.t3h.nitefoodie.model.Order;
import com.t3h.nitefoodie.ui.base.fragment.BaseMVPFragment;
import com.t3h.nitefoodie.ui.main.cart.order_detail.OrderDetailDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CartFragment extends BaseMVPFragment implements IOrder.View, OrderAdapter.IOrderAdapter {

    private RecyclerView rcOrder;
    private List<Order> mOrders;
    private OrderAdapter mAdapter;
    private Toolbar toolbar;
    private CartPresenter mPresenter;

    @Override
    public int getLayoutMain() {
        return R.layout.cart_fragment;
    }

    @Override
    public void findViewByIds() {
        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        rcOrder = (RecyclerView) getView().findViewById(R.id.rc_order);
    }

    @Override
    public void initComponents() {
        mPresenter = new CartPresenter(this);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Giỏ Hàng");
        activity.getSupportActionBar().setHomeButtonEnabled(true);

        mOrders = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcOrder.setLayoutManager(linearLayoutManager);
        mAdapter = new OrderAdapter(this);
        rcOrder.setAdapter(mAdapter);

        mPresenter.getListOrder();
    }

    @Override
    public void setEvents() {

    }

    @Override
    public int getCount() {
        return mOrders.size();
    }

    @Override
    public Order getItem(int position) {
        return mOrders.get(position);
    }

    @Override
    public void onClick(int position) {
        final Order order = mOrders.get(position);
        OrderDetailDialog dialog = new OrderDetailDialog(getContext(), order, new OrderDetailDialog.OnClickButton() {
            @Override
            public void onClick() {
                Calendar calendar = Calendar.getInstance();
                order.setState(Constants.ORDER_STATE_CONFIRMED);
                order.setOrderTime(calendar.getTimeInMillis());
                mPresenter.updateOrder(order);
                mAdapter.notifyDataSetChanged();
            }
        });
        dialog.show();
    }

    @Override
    public void onGetOrder(Order order) {
        mOrders.add(order);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void finishUpdateOrder() {

        Snackbar.make(getView().findViewById(R.id.content), "Đặt hàng thành công",Snackbar.LENGTH_SHORT)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
    }

    @Override
    public void onOrderError() {

    }
}
