package com.t3h.nitefoodie.ui.main.home.store.store_detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.t3h.nitefoodie.R;
import com.t3h.nitefoodie.model.Food;
import com.t3h.nitefoodie.ui.Utils;
import com.t3h.nitefoodie.ui.base.IGetPosition;

public class FoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private IRecipeAdapter mInterf;
    private Context mContext;

    public FoodAdapter(IRecipeAdapter interf) {
        mInterf = interf;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.food_item_layout, parent, false);
        return new FoodViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Food food = mInterf.getFood(position);
        FoodViewHolder viewHolder = (FoodViewHolder) holder;
        int size = (int) Utils.convertDpToPixel(56f);
        Context context = viewHolder.ivFood.getContext();
        Picasso.with(context).load(food.getPhotoUrl()).resize(size, size).into(viewHolder.ivFood);
        viewHolder.tvFoodName.setText(food.getName());
        viewHolder.tvFoodPrice.setText(food.getPrice() + "");
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

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFood;
        TextView tvFoodName;
        TextView tvFoodPrice;

        public FoodViewHolder(View itemView, View.OnClickListener onClickListener) {
            super(itemView);
            ivFood = (ImageView) itemView.findViewById(R.id.iv_food);
            tvFoodName = (TextView) itemView.findViewById(R.id.tv_food_name);
            tvFoodPrice = (TextView) itemView.findViewById(R.id.tv_food_price);

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

    public interface IRecipeAdapter {
        int getCount();

        void onClick(int position);

        Food getFood(int position);
    }
}
