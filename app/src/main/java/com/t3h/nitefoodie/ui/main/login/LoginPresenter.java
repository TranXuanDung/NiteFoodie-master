package com.t3h.nitefoodie.ui.main.login;

import android.content.Context;
import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.t3h.nitefoodie.common.Constants;
import com.t3h.nitefoodie.model.User;
import com.t3h.nitefoodie.ui.base.BasePresenter;
import com.t3h.nitefoodie.ui.base.activity.BaseActivity;

import java.util.Arrays;

public class LoginPresenter extends BasePresenter<ILogin.View> implements ILogin.Presenter,Constants {
    private FirebaseAuth mAuth;
    private DatabaseReference mData;

    public LoginPresenter(ILogin.View view) {
        super(view);
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void loginFacebook(Context context, CallbackManager callbackManager) {
        FacebookSdk.sdkInitialize(context);
        AppEventsLogger.activateApp(context);

        String[] listPermissions = {"email", "public_profile", "user_friends"};
        final LoginManager loginManager = LoginManager.getInstance();
        loginManager.logInWithReadPermissions((BaseActivity) mView, Arrays.asList(listPermissions));
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mView.onLoginSuccess();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                mView.onLoginCancel();
            }

            @Override
            public void onError(FacebookException error) {
                mView.onLoginError();
            }
        });
    }

    @Override
    public void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener((BaseActivity) mView, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mData.child(Constants.USERS).child(task.getResult().getUser().getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() != null) {
                                        mView.moveToMain();
                                    } else {
                                        User user = new User();
                                        FirebaseUser firebaseUser = task.getResult().getUser();
                                        user.setUid(firebaseUser.getUid());
                                        user.setName(firebaseUser.getDisplayName());
                                        user.setEmail(firebaseUser.getEmail());
                                        user.setPhotoUrl(firebaseUser.getPhotoUrl().toString());
                                        addUser(user);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                } else {
                    //mView.moveToMain();
                }
            }
        });
    }

    @Override
    public void addUser(User user) {
        mData.child(Constants.USERS).child(user.getUid()).setValue(user);
    }

}
