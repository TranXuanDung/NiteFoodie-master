package com.t3h.nitefoodie.ui.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.t3h.nitefoodie.R;
import com.t3h.nitefoodie.ui.base.IViewMain;
import com.t3h.nitefoodie.ui.base.activity.BaseActivity;
import com.t3h.nitefoodie.ui.base.animation.ScreenAnimation;

import java.util.List;

public abstract class BaseFragment extends Fragment implements IViewMain {
    private boolean mIsDestroy;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutMain(), container, false);
        mIsDestroy = false;
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewByIds();
        initComponents();
        setEvents();
    }

    @Override
    public void showProgress() {
        if (mIsDestroy) {
            return;
        }
        getBaseActivity().showProgress();
    }

    @Override
    public void hideProgress() {
        if (mIsDestroy) {
            return;
        }
        getBaseActivity().hideProgress();
    }

    @Override
    public void showMessage(String content) {
        Toast.makeText(getBaseActivity(), content, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showMessage(@StringRes int content) {
        Toast.makeText(getBaseActivity(), content, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackRoot() {
        getBaseActivity().onBackMain();
    }

    @Override
    public void onDestroyView() {
        mIsDestroy = true;
        super.onDestroyView();
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public static BaseFragment getCurrentBaseFragment(FragmentManager manager) {
        List<Fragment> fragments = manager.getFragments();
        if (fragments == null) {
            return null;
        }
        for (int i = fragments.size() - 1; i >= 0; i--) {
            BaseFragment fragment = (BaseFragment) fragments.get(i);
            if (fragment != null && fragment.isVisible()) {
                return fragment;
            }
        }
        return null;
    }

    public static void openFragment(FragmentManager manager, FragmentTransaction transaction,
                                    Class<? extends BaseFragment> newClass,
                                    ScreenAnimation screenAnimation,
                                    Bundle data,
                                    boolean isAddToBackStack,
                                    boolean isCommit) {
        String tag = newClass.getName();
        BaseFragment fragment = (BaseFragment) manager.findFragmentByTag(tag);
        if (fragment == null) {
            try {
                fragment = newClass.newInstance();
                transaction.setCustomAnimations(screenAnimation.getEnterToRight(), screenAnimation.getExitToRight(),
                        screenAnimation.getEnterToLeft(), screenAnimation.getExitToLeft());
                fragment.setArguments(data);
                transaction.add(R.id.content, fragment, tag);

            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            if (fragment.isVisible()) {
                return;
            }
            transaction.setCustomAnimations(screenAnimation.getEnterToRight(), screenAnimation.getExitToRight(),
                    screenAnimation.getEnterToLeft(), screenAnimation.getExitToLeft());
            transaction.show(fragment);
        }

        if (isAddToBackStack) {
            transaction.addToBackStack(tag);
        }
        if (isCommit) {
            transaction.commit();
        }
    }

    public static void openFragment(FragmentTransaction transaction,
                                    BaseFragment fragment,
                                    ScreenAnimation screenAnimation,
                                    boolean isAddBackStack,
                                    boolean isCommit) {
        String tag = fragment.getClass().getName();
        transaction.setCustomAnimations(
                screenAnimation.getEnterToRight(), screenAnimation.getExitToRight(),
                screenAnimation.getEnterToLeft(), screenAnimation.getExitToLeft());
        transaction.add(R.id.content, fragment, tag);
        if (isAddBackStack) {
            transaction.addToBackStack(tag);
        }
        if (isCommit) {
            transaction.commit();
        }
    }

    public static void hideFragment(FragmentManager manager,
                                    FragmentTransaction transaction,
                                    Class<? extends BaseFragment> hideClass,
                                    ScreenAnimation screenAnimation,
                                    boolean isAddToBackStack,
                                    boolean isCommit){
        String tag = hideClass.getName();
        BaseFragment fragment = (BaseFragment) manager.findFragmentByTag(tag);
        if (fragment !=null && fragment.isVisible()){
            transaction.setCustomAnimations(
                    screenAnimation.getEnterToRight(), screenAnimation.getExitToRight(),
                    screenAnimation.getEnterToLeft(), screenAnimation.getExitToLeft());
            transaction.hide(fragment);
            if (isAddToBackStack){
                transaction.addToBackStack(tag);
            }
            if (isCommit){
                transaction.commit();
            }
        }
    }

    public static void hideFragment(FragmentTransaction transaction,
                                    BaseFragment fragment,
                                    ScreenAnimation screenAnimation,
                                    boolean isAddBackStack,
                                    boolean isCommit) {
        if (fragment != null && fragment.isVisible()) {
            transaction.setCustomAnimations(
                    screenAnimation.getEnterToRight(), screenAnimation.getExitToRight(),
                    screenAnimation.getEnterToLeft(), screenAnimation.getExitToLeft());
            transaction.hide(fragment);
            if (isAddBackStack) {
                transaction.addToBackStack(fragment.getClass().getName());
            }
            if (isCommit) {
                transaction.commit();
            }
        }
    }
}
