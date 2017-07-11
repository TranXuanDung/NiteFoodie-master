package com.t3h.nitefoodie.ui.main.notification;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.t3h.nitefoodie.R;
import com.t3h.nitefoodie.ui.base.fragment.BaseMVPFragment;

public class NotificationFragment extends BaseMVPFragment {
    private Toolbar toolbar;
    @Override
    public int getLayoutMain() {
        return R.layout.notification_fragment;
    }

    @Override
    public void findViewByIds() {
        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
    }

    @Override
    public void initComponents() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Thông Báo");
    }

    @Override
    public void setEvents() {

    }
}
