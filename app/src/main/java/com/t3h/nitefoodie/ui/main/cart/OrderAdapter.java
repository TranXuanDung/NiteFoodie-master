package com.t3h.nitefoodie.ui.main.cart;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.t3h.nitefoodie.R;
import com.t3h.nitefoodie.common.Constants;
import com.t3h.nitefoodie.model.Order;
import com.t3h.nitefoodie.model.Store;
import com.t3h.nitefoodie.ui.base.IGetPosition;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private IOrderAdapter mInterf;

    public OrderAdapter(IOrderAdapter interf) {
        mInterf = interf;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.order_item_layout, parent, false);
        return new OrderViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final OrderViewHolder viewHolder = (OrderViewHolder) holder;
        Order order = mInterf.getItem(position);

        DatabaseReference data = FirebaseDatabase.getInstance().getReference();
        data.child(Constants.STORES).child(order.getStoreId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Store store = dataSnapshot.getValue(Store.class);
                viewHolder.tvStoreName.setText(store.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        viewHolder.tvOrderId.setText(order.getId());
        viewHolder.tvTotalPrice.setText(order.getTotalPrice() + "d");
        viewHolder.tvState.setText(order.getState());
    }

    @Override
    public int getItemCount() {
        return mInterf.getCount();
    }

    @Override
    public void onClick(View v) {
        IGetPosition position = (IGetPosition) v.getTag();
        int pos = position.getPosition();
        mInterf.onClick(pos);
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId;
        TextView tvStoreName;
        TextView tvTotalPrice;
        TextView tvState;

        public OrderViewHolder(View itemView, View.OnClickListener onClickListener) {
            super(itemView);

            tvOrderId = (TextView) itemView.findViewById(R.id.tv_order_id);
            tvStoreName = (TextView) itemView.findViewById(R.id.tv_store_name);
            tvTotalPrice = (TextView) itemView.findViewById(R.id.tv_total_price);
            tvState = (TextView) itemView.findViewById(R.id.tv_state);

            itemView.setOnClickListener(onClickListener);
            IGetPosition position = new IGetPosition() {
                @Override
                public int getPosition() {
                    return getAdapterPosition();
                }
            };
            itemView.setTag(position);

        }
    }

    public interface IOrderAdapter {
        int getCount();

        Order getItem(int position);

        void onClick(int position);
    }
}
