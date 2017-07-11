package com.t3h.nitefoodie.ui.main.login;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.t3h.nitefoodie.R;
import com.t3h.nitefoodie.common.Constants;
import com.t3h.nitefoodie.model.User;
import com.t3h.nitefoodie.ui.base.activity.BaseActivity;
import com.t3h.nitefoodie.ui.main.MainActivity;

public class LoginActivity extends BaseActivity implements View.OnClickListener, ILogin.View, Constants {


    private static final String TAG = LoginActivity.class.getSimpleName();
    private TextView tvTitle;
    private Button btnFbLogin;
    private CallbackManager mCallBackManager;
    private LoginPresenter mPresenter;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListerner;
    private String mIdUser;
    private DatabaseReference mData;
    private Handler mHandler;

    @Override
    public int getLayoutMain() {
        return R.layout.activity_login;
    }

    @Override
    public void findViewByIds() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        btnFbLogin = (Button) findViewById(R.id.btn_fb_login);
    }

    @Override
    public void initComponents() {
        mHandler = new Handler();
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Pacifico.ttf");
        tvTitle.setTypeface(typeface);
        mCallBackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        mPresenter = new LoginPresenter(this);
        checkLogin();
    }

    @Override
    public void setEvents() {
        btnFbLogin.setOnClickListener(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallBackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        onBackMain();
    }

    @Override
    public void onClick(View v) {
        mPresenter.loginFacebook(this, mCallBackManager);
    }

    @Override
    public void onLoginSuccess() {
        showMessage("Login Success!");
    }

    @Override
    public void onLoginError() {
        showMessage("Login Error");
    }

    @Override
    public void onLoginCancel() {
        showMessage("Login Cancel");
    }

    @Override
    public void moveToMain() {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void checkLogin() {
        mAuthListerner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    mIdUser = firebaseUser.getUid().toString();
                    mData.child(USERS).child(mIdUser).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                User user = dataSnapshot.getValue(User.class);
                                if (user != null) {
                                    final Intent intent = new Intent();
                                    intent.setClass(LoginActivity.this, MainActivity.class);
//                                    mHandler.postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//
//                                        }
//                                    }, 3000);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    showMessage("Khong lay duoc du lieu!");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    showLoginLayout();
                }
            }
        };
    }

    private void showLoginLayout() {
        btnFbLogin.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListerner);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListerner != null) {
            mAuth.removeAuthStateListener(mAuthListerner);
        }
    }
}
