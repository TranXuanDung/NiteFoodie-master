package com.t3h.nitefoodie.ui.main.my_store;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.t3h.nitefoodie.R;
import com.t3h.nitefoodie.common.Constants;
import com.t3h.nitefoodie.model.Food;
import com.t3h.nitefoodie.model.Store;
import com.t3h.nitefoodie.ui.base.activity.BaseActivity;
import com.t3h.nitefoodie.ui.base.animation.ScreenAnimation;
import com.t3h.nitefoodie.ui.base.fragment.BaseFragment;
import com.t3h.nitefoodie.ui.main.home.store.store_detail.FoodAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyStoreActivity extends BaseActivity implements IMyStore.View, FoodAdapter.IRecipeAdapter, View.OnClickListener {
    private Toolbar toolbar;
    private ImageView ivToolbar;
    private TextView tvAddress;
    private RatingBar ratingBar;
    private TextView tvNumberRating;
    private Button btnPhone;
    private FloatingActionButton btnAddFood;

    private RecyclerView rcStoreMenu;
    private List<Food> mFoods;
    private FoodAdapter mFoodAdapter;

    private MyStorePresenter mPresenter;

    @Override
    public int getLayoutMain() {
        return R.layout.activity_my_store;
    }

    @Override
    public void findViewByIds() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnAddFood = (FloatingActionButton) findViewById(R.id.btn_add_food);
        ivToolbar = (ImageView) findViewById(R.id.iv_store);
        tvAddress = (TextView) findViewById(R.id.tv_store_address);
        ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        tvNumberRating = (TextView) findViewById(R.id.tv_number_rating);
        btnPhone = (Button) findViewById(R.id.btn_store_phone);
    }

    @Override
    public void initComponents() {
        mPresenter = new MyStorePresenter(this);
        rcStoreMenu = (RecyclerView) findViewById(R.id.rc_store_menu);

        mFoods = new ArrayList<>();
        mFoodAdapter = new FoodAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcStoreMenu.setLayoutManager(linearLayoutManager);
        rcStoreMenu.setAdapter(mFoodAdapter);
        rcStoreMenu.setFocusable(false);
        rcStoreMenu.setNestedScrollingEnabled(false);

        mPresenter.getMyStoreInfor();
    }

    @Override
    public void setEvents() {
        btnAddFood.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackMain();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_my_store_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        editMyStore();
        return super.onOptionsItemSelected(item);
    }

    private void editMyStore() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        BaseFragment.openFragment(manager, transaction, EditMyStoreFragment.class,
                ScreenAnimation.NONE, null, true, true);
    }

    @Override
    public int getCount() {
        return mFoods.size();
    }

    @Override
    public void onClick(int position) {
        final Food food = mFoods.get(position);
        DialogFoodSelect dialogFoodSelect = new DialogFoodSelect(this, new DialogFoodSelect.OnClickDialog() {
            @Override
            public void onClickDelete() {
                deleteFood(food);
            }

            @Override
            public void onClickEdit() {
                editFood(food);
                mFoodAdapter.notifyDataSetChanged();
            }
        });
        dialogFoodSelect.show();
    }

    private void editFood(Food food) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FOOD_ID, food.getFoodId());
        bundle.putString(Constants.FOOD_NAME, food.getName());
        bundle.putString(Constants.FOOD_PRICE, String.valueOf(food.getPrice()));
        bundle.putString(Constants.FOOD_URL, food.getPhotoUrl());
        BaseFragment.openFragment(manager, transaction, EditFoodFragment.class,
                ScreenAnimation.OPEN_FULL, bundle, true, true);
    }

    private void deleteFood(Food food) {
        mPresenter.deleteFood(food);
    }

    @Override
    public Food getFood(int position) {
        return mFoods.get(position);
    }

    @Override
    public void finishGetStore(Store store) {
        mFoods.clear();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(store.getName());

        Picasso.with(this).load(store.getPhotoUrl()).into(ivToolbar);
        ivToolbar.setScaleType(ImageView.ScaleType.CENTER_CROP);
        tvAddress.setText(store.getAddress());
        ratingBar.setRating((float) store.getRate());
        tvNumberRating.setText(store.getNumberRating() + "");
        btnPhone.setText(store.getPhone());

        HashMap<String, Food> menu = store.getMenu();
        for (Food food : menu.values()) {
            mFoods.add(food);
            mFoodAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_food:
                addFood();
                break;
        }
    }

    private void addFood() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        BaseFragment.openFragment(manager, transaction, AddFoodFragment.class,
                ScreenAnimation.OPEN_FULL, null, true, true);
    }

    @Override
    public void onBackRoot() {
        onBackMain();
    }
}
