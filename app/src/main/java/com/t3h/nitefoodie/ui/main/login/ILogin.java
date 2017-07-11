package com.t3h.nitefoodie.ui.main.login;

import android.content.Context;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.t3h.nitefoodie.model.User;
import com.t3h.nitefoodie.ui.base.IBasePresenter;
import com.t3h.nitefoodie.ui.base.IViewMain;

public interface ILogin {
    interface View extends IViewMain{
        void onLoginSuccess();
        void onLoginError();
        void onLoginCancel();
        void moveToMain();
    }

    interface Presenter extends IBasePresenter{
        void loginFacebook(Context context, CallbackManager callbackManager);
        void handleFacebookAccessToken(AccessToken token);
        void addUser(User user);
    }
}
