package com.t3h.nitefoodie.ui.main.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.t3h.nitefoodie.R;
import com.t3h.nitefoodie.common.Constants;
import com.t3h.nitefoodie.model.User;
import com.t3h.nitefoodie.ui.base.animation.ScreenAnimation;
import com.t3h.nitefoodie.ui.base.fragment.BaseFragment;
import com.t3h.nitefoodie.ui.base.fragment.BaseMVPFragment;
import com.t3h.nitefoodie.ui.main.MainActivity;
import com.t3h.nitefoodie.ui.main.login.LoginActivity;
import com.t3h.nitefoodie.ui.main.my_store.MyStoreActivity;

public class AccountFragment extends BaseMVPFragment implements View.OnClickListener {
    private Toolbar toolbar;
    private String idUser;
    private DatabaseReference mData;
    private ImageView ivUserAvatar;
    private TextView tvUserName;
    private TextView tvUserEmail;
    private Button btnRegister;
    private TextView tvPhone;
    private TextView tvAddress;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public int getLayoutMain() {
        return R.layout.account_fragment;
    }

    @Override
    public void findViewByIds() {
        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        ivUserAvatar = (ImageView) getView().findViewById(R.id.iv_user_avatar);
        tvUserName = (TextView) getView().findViewById(R.id.tv_user_name);
        tvUserEmail = (TextView) getView().findViewById(R.id.tv_user_email);
        btnRegister = (Button) getView().findViewById(R.id.btn_register);
        tvPhone = (TextView) getView().findViewById(R.id.tv_user_phone);
        tvAddress = (TextView) getView().findViewById(R.id.tv_user_address);
    }

    @Override
    public void initComponents() {
        mData = FirebaseDatabase.getInstance().getReference();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Tài Khoản");
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initProfile();
    }

    private void initProfile() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        idUser = firebaseUser.getUid();
        //  idUser = getArguments().getString(Constants.ID_USER);

        mData.child(Constants.USERS).child(idUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    User user = dataSnapshot.getValue(User.class);
                    Picasso.with(getContext()).load(user.getPhotoUrl())
                            .into(ivUserAvatar);
                    tvUserName.setText(user.getName());
                    tvUserEmail.setText(user.getEmail());
                    if (user.getPhone() != null) {
                        tvPhone.setText(user.getPhone());
                    }
                    if (user.getAddress() != null) {
                        tvAddress.setText(user.getAddress());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void setEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackRoot();
            }
        });

        btnRegister.setOnClickListener(this);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_account_toolbar, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_account_logout:
                logOut();
                break;
            case R.id.action_account_edit:
                editAccount();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void editAccount() {
        EditAccountDialog dialog = new EditAccountDialog(getContext());
        dialog.show();
    }

    private void logOut() {
        LoginManager.getInstance().logOut();
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setMessage("Do you want to logout?");
        alert.setCancelable(false);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();

    }

    @Override
    public void onClick(View v) {
        mData.child(Constants.STORES).child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    showRegisterFragment();
                } else {
                    showMyStoreFragment();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void showMyStoreFragment() {
        Intent intent = new Intent(getContext(), MyStoreActivity.class);
        startActivity(intent);
    }

    private void showRegisterFragment() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Bạn chưa có cửa hàng! Bạn có muốn tạo mơi?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity activity = (MainActivity) getActivity();

                BaseFragment fragment = BaseFragment.getCurrentBaseFragment(activity.getSupportFragmentManager());
                //Log.d(TAG, fragment.getClass().getName() + "");
                FragmentManager manager = activity.getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                if (fragment != null) {
                    BaseFragment.hideFragment(manager, transaction, fragment.getClass(),
                            ScreenAnimation.OPEN_FULL, false, false);
                }

                BaseFragment.openFragment(manager, transaction,
                        RegisterFragment.class, ScreenAnimation.OPEN_FULL, null, true, true);
            }
        });
    }
}
