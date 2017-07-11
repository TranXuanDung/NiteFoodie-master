package com.t3h.nitefoodie.ui.main.account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.t3h.nitefoodie.R;
import com.t3h.nitefoodie.common.Constants;
import com.t3h.nitefoodie.model.Store;
import com.t3h.nitefoodie.ui.Utils;
import com.t3h.nitefoodie.ui.base.fragment.BaseMVPFragment;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class RegisterFragment extends BaseMVPFragment implements Constants, View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private static final String TAG = RegisterFragment.class.getSimpleName();
    private Toolbar toolbar;
    private DatabaseReference mData;
    private ImageView ivStore;
    private TextInputLayout tilName;
    private EditText edtName;
    private TextInputLayout tilAddress;
    private EditText edtAddress;
    private TextInputLayout tilPhone;
    private EditText edtPhone;
    private TextInputLayout tilType;
    private EditText edtType;
    private TimePicker openTimePicker;
    private TimePicker closeTimePicker;

    private String idUser;

    private String name;
    private String address;
    private String phone;
    private int openTime;
    private int closeTime;
    private String tag;
    private String photoUrl;
    private Store store;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public int getLayoutMain() {
        return R.layout.store_register_fragment;
    }

    @Override
    public void findViewByIds() {
        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        ivStore = (ImageView) getView().findViewById(R.id.iv_store);
        tilName = (TextInputLayout) getView().findViewById(R.id.til_name);
        edtName = (EditText) getView().findViewById(R.id.edt_name);
        tilAddress = (TextInputLayout) getView().findViewById(R.id.til_address);
        edtAddress = (EditText) getView().findViewById(R.id.edt_address);
        tilPhone = (TextInputLayout) getView().findViewById(R.id.til_phone);
        edtPhone = (EditText) getView().findViewById(R.id.edt_phone);
        tilType = (TextInputLayout) getView().findViewById(R.id.til_tag);
        edtType = (EditText) getView().findViewById(R.id.edt_tag);
        openTimePicker = (TimePicker) getView().findViewById(R.id.time_picker_open);
        closeTimePicker = (TimePicker) getView().findViewById(R.id.time_picker_close);

    }

    @Override
    public void initComponents() {
        store = new Store();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("My Store");
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mData = FirebaseDatabase.getInstance().getReference();
        idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        openTimePicker.setIs24HourView(true);
        closeTimePicker.setIs24HourView(true);
    }

    @Override
    public void setEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackRoot();
            }
        });
        ivStore.setOnClickListener(this);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_store_register_toolbar, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                if (checkAndUpload()) {
                    uploadStore();
                    onBackRoot();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
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
        if (edtType.getText().equals("")) {
            tilType.setError("Bạn phải điền thiếu phân loại");
        }
        if (edtName.getText().equals("") || edtAddress.getText().equals("")
                || edtPhone.getText().equals("") || edtType.getText().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_store:
                PopupMenu popupMenu = new PopupMenu(getContext(), ivStore);
                popupMenu.getMenuInflater().inflate(R.menu.menu_add_image, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.show();
                break;
            case R.id.btn_add_food:
                //uploadImageToStorage();
                break;
        }
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_take_picture:
                takePicture();
                break;
            case R.id.action_gallery:
                chooseGallery();
                break;
        }
        return false;
    }

    private void chooseGallery() {
        Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
        imagePickerIntent.setType("image/*");
        startActivityForResult(imagePickerIntent, REQUEST_LOAD_GALLERY);
    }

    private void takePicture() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(captureIntent, REQUEST_LOAD_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == getActivity().RESULT_OK && data != null) {
            if (requestCode == REQUEST_LOAD_GALLERY) {
                Uri imgUri = data.getData();
                Picasso.with(getContext()).load(imgUri).into(ivStore);
                ivStore.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            if (requestCode == REQUEST_LOAD_CAPTURE) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivStore.setImageBitmap(bitmap);
                ivStore.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadStore() {
        name = edtName.getText().toString();
        address = edtAddress.getText().toString();
        phone = edtPhone.getText().toString();
        tag = edtType.getText().toString();


        openTime = Utils.convertTimeToInt(Utils.getTimeInTimePicker(openTimePicker));
        closeTime = Utils.convertTimeToInt(Utils.getTimeInTimePicker(closeTimePicker));
        store.setName(name);
        store.setAddress(address);
        store.setPhone(phone);
        store.setTag(tag);
        store.setOpenTime(openTime);
        store.setCloseTime(closeTime);
        store.setRate(0);
        store.setNumberRating(0);
        store.setsId(idUser);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        ivStore.setDrawingCacheEnabled(true);
        ivStore.buildDrawingCache();
        Bitmap bitmap = ivStore.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        Calendar calendar = Calendar.getInstance();

        StorageReference imgUpload = storageRef.child("img" + calendar.getTimeInMillis() + ".png");
        UploadTask uploadTask = imgUpload.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getContext(), "Loi upload hinh anh", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                store.setPhotoUrl(String.valueOf(taskSnapshot.getDownloadUrl()));
                Log.d(TAG, "_______________________________________" + taskSnapshot.getDownloadUrl().toString());
                Log.d(TAG, "_______________________________________" + store.getPhotoUrl());
                //photoUrl = downloadUrl.toString();
                mData.child(STORES).child(idUser).setValue(store);
            }
        });
    }
}
