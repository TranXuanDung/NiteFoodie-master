package com.t3h.nitefoodie.ui.main.account;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.t3h.nitefoodie.R;
import com.t3h.nitefoodie.common.Constants;
import com.t3h.nitefoodie.model.User;

import java.util.HashMap;

public class EditAccountDialog extends Dialog implements View.OnClickListener {
    private TextInputLayout tilName;
    private EditText edtName;
    private TextInputLayout tilAddress;
    private EditText edtAddress;
    private TextInputLayout tilPhone;
    private EditText edtPhone;
    private TextInputLayout tilEmail;
    private EditText edtEmail;
    private String userId;
    private String photoUrl;
    private DatabaseReference mData;
    private HashMap<String, String> favoriteIds = new HashMap<>();

    public EditAccountDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_edit_profile);
        findViewByIds();
        setEvents();
    }

    private void findViewByIds() {
        tilName = (TextInputLayout) findViewById(R.id.til_name);
        edtName = (EditText) findViewById(R.id.edt_name);
        tilAddress = (TextInputLayout) findViewById(R.id.til_address);
        edtAddress = (EditText) findViewById(R.id.edt_address);
        tilPhone = (TextInputLayout) findViewById(R.id.til_phone);
        edtPhone = (EditText) findViewById(R.id.edt_phone);
        tilEmail = (TextInputLayout) findViewById(R.id.til_email);
        edtEmail = (EditText) findViewById(R.id.edt_email);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mData = FirebaseDatabase.getInstance().getReference();
        mData.child(Constants.USERS).child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                favoriteIds = user.getFavoriteIds();
                photoUrl = user.getPhotoUrl();
                edtName.setText(user.getName());
                edtEmail.setText(user.getEmail());
                if (user.getPhone() != null) {
                    edtPhone.setText(user.getPhone());
                }
                if (user.getAddress() != null) {
                    edtAddress.setText(user.getAddress());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setEvents() {
        findViewById(R.id.btn_submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (checkAndUpload()) {
            User user = new User();
            user.setUid(userId);
            user.setFavoriteIds(favoriteIds);
            user.setEmail(edtEmail.getText().toString().trim());
            user.setPhotoUrl(photoUrl);
            user.setName(edtName.getText().toString().trim());
            user.setPhone(edtPhone.getText().toString().trim());
            user.setAddress(edtAddress.getText().toString().trim());
            mData.child(Constants.USERS).child(userId).setValue(user);
            dismiss();
        }
    }

    private boolean checkAndUpload() {
        if (edtName.getText().equals("")) {
            tilName.setError("Bạn phải điền thiếu tên");
        }
        if (edtAddress.getText().equals("")) {
            tilAddress.setError("Bạn phải điền thiếu địa chỉ");
        }
        if (edtPhone.getText().equals("")) {
            tilPhone.setError("Bạn phải điền thiếu số điện thoại");
        }
        if (edtEmail.getText().equals("")) {
            tilEmail.setError("Bạn phải điền thiếu email");
        }
        if (edtName.getText().equals("") || edtAddress.getText().equals("")
                || edtPhone.getText().equals("") || edtEmail.getText().equals("")) {
            return false;
        } else {
            return true;
        }
    }

}
