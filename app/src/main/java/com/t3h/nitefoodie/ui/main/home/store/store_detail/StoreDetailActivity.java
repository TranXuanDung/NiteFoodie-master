package com.t3h.nitefoodie.ui.main.home.store.store_detail;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.t3h.nitefoodie.R;
import com.t3h.nitefoodie.common.Constants;
import com.t3h.nitefoodie.model.Food;
import com.t3h.nitefoodie.model.FoodOrder;
import com.t3h.nitefoodie.model.Order;
import com.t3h.nitefoodie.model.Store;
import com.t3h.nitefoodie.ui.Utils;
import com.t3h.nitefoodie.ui.base.activity.BaseActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class StoreDetailActivity extends BaseActivity implements IStoreDetail.View, View.OnClickListener, FoodAdapter.IRecipeAdapter {

    private static String currentStoreId = "";

    private Toolbar toolbar;
    private ImageView ivToolbar;
    private TextView tvAddress;
    private RatingBar ratingBar;
    private TextView tvNumberRating;
    private ImageView ivOnlineState;
    private TextView tvOnlineState;
    private Button btnPhone;
    private FloatingActionButton fabFavourite;

    private RecyclerView rcStoreMenu;
    private List<Food> mFoods;
    private FoodAdapter mFoodAdapter;
    private StoreDetailPresenter mPresenter;
    private String sId;
    private String userId;
    private boolean mIsFirstBuy = false;
    private Order order;
    private DatabaseReference mData;

    @Override
    public int getLayoutMain() {
        return R.layout.store_detail_fragment;
    }

    @Override
    public void findViewByIds() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fabFavourite = (FloatingActionButton) findViewById(R.id.fab_favourite);
        ivToolbar = (ImageView) findViewById(R.id.iv_store);
        tvAddress = (TextView) findViewById(R.id.tv_store_address);
        ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        tvNumberRating = (TextView) findViewById(R.id.tv_number_rating);
        ivOnlineState = (ImageView) findViewById(R.id.iv_online_state);
        tvOnlineState = (TextView) findViewById(R.id.tv_online_state);
        btnPhone = (Button) findViewById(R.id.btn_store_phone);

        rcStoreMenu = (RecyclerView) findViewById(R.id.rc_store_menu);
    }

    @Override
    public void initComponents() {
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mData = FirebaseDatabase.getInstance().getReference();
        sId = getIntent().getStringExtra(Constants.ID_USER);
        mPresenter = new StoreDetailPresenter(this);

        mFoods = new ArrayList<>();
        mFoodAdapter = new FoodAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcStoreMenu.setLayoutManager(linearLayoutManager);
        rcStoreMenu.setAdapter(mFoodAdapter);
        rcStoreMenu.setFocusable(false);
        rcStoreMenu.setNestedScrollingEnabled(false);

        mPresenter.getStoreInfo(sId);


        mData.child(Constants.USERS).child(userId).child(Constants.FAVORITE_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(sId)) {
                    fabFavourite.setImageResource(R.drawable.ic_favorite_white_24dp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void setEvents() {
        fabFavourite.setOnClickListener(this);
        btnPhone.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackMain();
            }
        });
    }

    @Override
    public void onBackRoot() {
        onBackMain();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_favourite:
                mData.child(Constants.USERS).child(userId).child(Constants.FAVORITE_ID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(sId)) {
                            fabFavourite.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                            mPresenter.removeFromFavorite(sId);
                        }else {
                            fabFavourite.setImageResource(R.drawable.ic_favorite_white_24dp);
                            mPresenter.addToFavorite(sId);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                
                break;
            case R.id.btn_store_phone:
                break;
        }
    }

    @Override
    public int getCount() {
        return mFoods.size();
    }

    @Override
    public void onClick(int position) {
        final Food food = mFoods.get(position);
        FoodDetailDialog detailDialog = new FoodDetailDialog(this, food, new FoodDetailDialog.OnClickDialog() {
            @Override
            public void onOrderSubmitClick(int numberFood) {
                if (numberFood > 0) {
                    if (!mIsFirstBuy) {
                        order = new Order();
                        Calendar calendar = Calendar.getInstance();
                        String orderId = "o" + calendar.getTimeInMillis();
                        order.setId(orderId);
                        order.setStoreId(sId);
                        order.setState(Constants.ORDER_STATE_WAITING);
                        order.setUserId(userId);
                        mPresenter.createOrder(order);
                        mIsFirstBuy = true;
                    }
                    FoodOrder foodOrder = new FoodOrder();
                    foodOrder.setFoodId(food.getFoodId());
                    foodOrder.setName(food.getName());
                    foodOrder.setPhotoUrl(food.getPhotoUrl());
                    foodOrder.setPrice(food.getPrice());
                    foodOrder.setNumberOfFood(numberFood);
                    mPresenter.onUpdateFoodToOrder(order.getId(), foodOrder);
                }
            }
        });
        detailDialog.show();
    }

    @Override
    public Food getFood(int position) {
        return mFoods.get(position);
    }

    @Override
    public void finishGetStoreDetail(Store store) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(store.getName());
        Picasso.with(this).load(store.getPhotoUrl()).into(ivToolbar);
        ivToolbar.setScaleType(ImageView.ScaleType.CENTER_CROP);
        tvAddress.setText(store.getAddress());
        ratingBar.setRating((float) store.getRate());
        tvNumberRating.setText(store.getNumberRating() + "");
        btnPhone.setText(store.getPhone());

        if (Utils.isStoreOpen(store.getOpenTime(), store.getCloseTime())) {
            ivOnlineState.setImageResource(R.drawable.online);
            tvOnlineState.setText("Online");
            tvOnlineState.setTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            ivOnlineState.setImageResource(R.drawable.offline);
            tvOnlineState.setText("Offline");
            tvOnlineState.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        HashMap<String, Food> menu = store.getMenu();
        for (Food food : menu.values()) {
            mFoods.add(food);
            mFoodAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFoodOrderFinish() {
        Snackbar.make(findViewById(R.id.content), "Đã thêm vào giỏ hàng", Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        }).show();
    }
}
