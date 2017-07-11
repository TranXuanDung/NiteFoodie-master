package com.t3h.nitefoodie.ui.base.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.t3h.nitefoodie.R;
import com.t3h.nitefoodie.ui.base.IViewMain;
import com.t3h.nitefoodie.ui.base.fragment.BaseFragment;

public abstract class BaseActivity extends AppCompatActivity implements IViewMain {


    private ProgressDialog mProgressDialog;
    private boolean mIsDestroy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIsDestroy = false;
        setContentView(getLayoutMain());
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.loading));

        findViewByIds();
        initComponents();
        setEvents();
    }

    @Override
    public void showProgress() {
        if (mIsDestroy) {
            return;
        }
        mProgressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (mIsDestroy) {
            return;
        }
        mProgressDialog.hide();
    }

    @Override
    public void showMessage(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(@StringRes int content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        onBackRoot();
    }

    @Override
    public void onBackRoot() {
        BaseFragment fragment = BaseFragment.getCurrentBaseFragment(getSupportFragmentManager());
        if (fragment != null) {
            fragment.onBackRoot();
        }
    }

    public final void onBackMain() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        mIsDestroy = true;
        super.onDestroy();
    }
}
