package com.t3h.nitefoodie.ui.main.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.t3h.nitefoodie.R;
import com.t3h.nitefoodie.ui.base.animation.ScreenAnimation;
import com.t3h.nitefoodie.ui.base.fragment.BaseFragment;
import com.t3h.nitefoodie.ui.base.fragment.BaseMVPFragment;
import com.t3h.nitefoodie.ui.main.MainActivity;
import com.t3h.nitefoodie.ui.main.home.store.list_store.ListFavoriteFragment;
import com.t3h.nitefoodie.ui.main.home.store.list_store.ListStoreFragment;
import com.t3h.nitefoodie.ui.main.search.SearchFragment;

public class MainFragment extends BaseMVPFragment implements ViewPager.OnPageChangeListener {
    private ViewPager vpMain;
    private TabLayout tabLayout;
    private Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public int getLayoutMain() {
        return R.layout.main_fragment;
    }

    @Override
    public void findViewByIds() {
        vpMain = (ViewPager) getView().findViewById(R.id.vp_main);
        tabLayout = (TabLayout) getView().findViewById(R.id.tablayout);
        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
    }

    @Override
    public void initComponents() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Tài Khoản");
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Explode explode = new Explode();
        getBaseActivity().getWindow().setExitTransition(explode);
        tabLayout.setupWithViewPager(vpMain);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        vpMain.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                openSearch();
                break;
            case R.id.action_setting:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openSearch() {
        MainActivity activity = (MainActivity) getActivity();

        BaseFragment fragment = BaseFragment.getCurrentBaseFragment(activity.getSupportFragmentManager());
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (fragment != null) {
            BaseFragment.hideFragment(manager, transaction, fragment.getClass(),
                    ScreenAnimation.OPEN_FULL, false, false);
        }
        BaseFragment.openFragment(manager, transaction, SearchFragment.class,
                ScreenAnimation.OPEN_FULL, null, true, true);
    }

    @Override
    public void setEvents() {
        vpMain.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new ListStoreFragment();
            }
            return new ListFavoriteFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getContext().getString(R.string.all);

                case 1:
                    return getContext().getString(R.string.favorite);

                default:
                    return getContext().getString(R.string.favourite);

            }
        }
    }
}
