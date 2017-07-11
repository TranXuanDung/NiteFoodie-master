package com.t3h.nitefoodie.ui.main.cart.order_detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.t3h.nitefoodie.R;
import com.t3h.nitefoodie.model.FoodOrder;

public class FoodOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private IFoodAdapter mInterf;
    private Context mContext;

    public FoodOrderAdapter(IFoodAdapter interf) {
        mInterf = interf;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.food_order_item_layout, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FoodOrder foodOrder = mInterf.getFoodOrder(position);
        FoodViewHolder viewHolder = (FoodViewHolder) holder;
        Context context = viewHolder.ivFood.getContext();
        Picasso.with(context).load(foodOrder.getPhotoUrl()).into(viewHolder.ivFood);
        viewHolder.tvFoodName.setText(foodOrder.getName());
        viewHolder.tvFoodPrice.setText(foodOrder.getPrice() + "d");
        viewHolder.tvNumberFood.setText(foodOrder.getNumberOfFood() + "");
    }

    @Override
    public int getItemCount() {
        return mInterf.getCount();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFood;
        TextView tvFoodName;
        TextView tvFoodPrice;
        TextView tvNumberFood;

        public FoodViewHolder(View itemView) {
            super(itemView);
            ivFood = (ImageView) itemView.findViewById(R.id.iv_food);
            tvFoodName = (TextView) itemView.findViewById(R.id.tv_food_name);
            tvFoodPrice = (TextView) itemView.findViewById(R.id.tv_food_price);
            tvNumberFood = (TextView) itemView.findViewById(R.id.tv_number_food);

        }

    }

    public interface IFoodAdapter {
        int getCount();

        FoodOrder getFoodOrder(int position);
    }
}
