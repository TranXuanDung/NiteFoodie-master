package com.t3h.nitefoodie.ui.main.home.store.store_detail;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.t3h.nitefoodie.R;
import com.t3h.nitefoodie.model.Food;

public class FoodDetailDialog extends Dialog implements View.OnClickListener {
    private ImageView ivFood;
    private TextView tvFoodName;
    private TextView tvFoodPrice;
    private TextView tvTotalPrice;
    private TextView tvNumberFood;
    private OnClickDialog mInterf;
    private Food mFood;

    public FoodDetailDialog(@NonNull Context context, Food food, OnClickDialog interf) {
        super(context);
        mInterf = interf;
        mFood = food;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_food_detail);
        findViewByIds();
        setEvents();
    }

    private void setEvents() {
        Picasso.with(getContext()).load(mFood.getPhotoUrl()).into(ivFood);
        ivFood.setScaleType(ImageView.ScaleType.CENTER_CROP);
        tvFoodName.setText(mFood.getName());
        tvFoodPrice.setText(mFood.getPrice()+"đ");
        findViewById(R.id.ib_add_food).setOnClickListener(this);
        findViewById(R.id.ib_remove_food).setOnClickListener(this);
        findViewById(R.id.ll_order).setOnClickListener(this);
    }

    private void findViewByIds() {
        ivFood = (ImageView) findViewById(R.id.iv_food);
        tvFoodName = (TextView) findViewById(R.id.tv_food_name);
        tvFoodPrice = (TextView) findViewById(R.id.tv_food_price);
        tvNumberFood = (TextView) findViewById(R.id.tv_number_food);
        tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);

    }

    @Override
    public void onClick(View v) {
        int numberFood = Integer.parseInt(tvNumberFood.getText().toString());
        switch (v.getId()) {
            case R.id.ib_add_food:
                numberFood++;
                tvNumberFood.setText(numberFood + "");
                updateTotalPrice(numberFood);
                break;
            case R.id.ib_remove_food:
                if (numberFood > 0) {
                    numberFood--;
                    tvNumberFood.setText(numberFood + "");
                    updateTotalPrice(numberFood);
                }
                break;
            case R.id.ll_order:
                mInterf.onOrderSubmitClick(numberFood);
                dismiss();
                break;
        }
    }

    private void updateTotalPrice(int numberFood) {
        tvTotalPrice.setText(numberFood * mFood.getPrice() + "đ");
    }

    public interface OnClickDialog {
        void onOrderSubmitClick(int numberFood);
    }
}
