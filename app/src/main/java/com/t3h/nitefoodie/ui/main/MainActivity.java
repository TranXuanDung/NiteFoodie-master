package com.t3h.nitefoodie.ui.main;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.t3h.nitefoodie.R;
import com.t3h.nitefoodie.common.Constants;
import com.t3h.nitefoodie.model.Order;
import com.t3h.nitefoodie.ui.base.activity.BaseActivity;
import com.t3h.nitefoodie.ui.base.animation.ScreenAnimation;
import com.t3h.nitefoodie.ui.base.fragment.BaseFragment;
import com.t3h.nitefoodie.ui.main.account.AccountFragment;
import com.t3h.nitefoodie.ui.main.cart.CartFragment;
import com.t3h.nitefoodie.ui.main.home.MainFragment;
import com.t3h.nitefoodie.ui.main.notification.NotificationFragment;

public class MainActivity extends BaseActivity implements AHBottomNavigation.OnTabSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private AHBottomNavigation mBottomNavigation;
    private String idUser;
    private DatabaseReference mData;

    @Override
    public int getLayoutMain() {
        return R.layout.activity_main;
    }

    @Override
    public void findViewByIds() {
        mBottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
    }

    @Override
    public void initComponents() {
        mData = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        idUser = intent.getStringExtra(Constants.ID_USER);
        initBottomNavigation();


        FragmentManager manager = getSupportFragmentManager();
        BaseFragment.openFragment(manager, manager.beginTransaction(),
                MainFragment.class, ScreenAnimation.OPEN_FULL, null, false, true);

        onListenOrderAdd();

    }

    private void onListenOrderAdd() {
        mData.child(Constants.ORDERS).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Order order = dataSnapshot.getValue(Order.class);
                if (order != null) {
                    mBottomNavigation.setNotification("1", 1);
                }
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

    private void initBottomNavigation() {
        AHBottomNavigationItem itemHome = new AHBottomNavigationItem(R.string.home, R.drawable.ic_home_black_24dp, R.color.white);
        AHBottomNavigationItem itemStore = new AHBottomNavigationItem(R.string.store, R.drawable.ic_local_grocery_store_black_24dp, R.color.white);
        AHBottomNavigationItem itemNotification = new AHBottomNavigationItem(R.string.notification, R.drawable.ic_notifications_black_24dp, R.color.white);
        AHBottomNavigationItem itemAccount = new AHBottomNavigationItem(R.string.account, R.drawable.ic_account_box_black_24dp, R.color.white);
        mBottomNavigation.addItem(itemHome);
        mBottomNavigation.addItem(itemStore);
        mBottomNavigation.addItem(itemNotification);
        mBottomNavigation.addItem(itemAccount);

        mBottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.colorPrimary));
        //     mBottonNavigation.setBehaviorTranslationEnabled(true);
        mBottomNavigation.setAccentColor(getResources().getColor(R.color.white));
        mBottomNavigation.setInactiveColor(getResources().getColor(R.color.blue_grey_200));
        //     mBottonNavigation.setTranslucentNavigationEnabled(true);
        //  mBottonNavigation.setForceTint(true);
        mBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        mBottomNavigation.setNotificationBackgroundColor(getResources().getColor(R.color.colorAccent));
        // mBottomNavigation.setNotification("ABC", 1);

        //mBottomNavigation.setColored(true);

    }

    @Override
    public void setEvents() {
        mBottomNavigation.setOnTabSelectedListener(this);
    }

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {
        //item.setChecked(true);
        BaseFragment fragment = BaseFragment.getCurrentBaseFragment(getSupportFragmentManager());
        //Log.d(TAG, fragment.getClass().getName() + "");
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (fragment != null) {
            BaseFragment.hideFragment(manager, transaction, fragment.getClass(),
                    ScreenAnimation.OPEN_FULL, false, false);
        }
        switch (position) {
            case 0:
                BaseFragment.openFragment(manager, transaction,
                        MainFragment.class, ScreenAnimation.OPEN_FULL, null, false, true);
                break;
            case 1:
                mBottomNavigation.setNotification("",1);
                BaseFragment.openFragment(manager, transaction,
                        CartFragment.class, ScreenAnimation.OPEN_FULL, null, false, true);
                break;
            case 2:
                BaseFragment.openFragment(manager, transaction,
                        NotificationFragment.class, ScreenAnimation.OPEN_FULL, null, false, true);
                break;
            case 3:
//                Bundle bundle = new Bundle();
//                bundle.putString(Constants.ID_USER, idUser);
                BaseFragment.openFragment(manager, transaction, AccountFragment.class,
                        ScreenAnimation.OPEN_FULL, null, false, true);
                break;
        }
        return true;
    }
}
