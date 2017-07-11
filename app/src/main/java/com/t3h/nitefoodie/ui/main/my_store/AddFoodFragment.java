package com.t3h.nitefoodie.ui.main.my_store;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.t3h.nitefoodie.model.Food;
import com.t3h.nitefoodie.ui.base.fragment.BaseMVPFragment;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class AddFoodFragment extends BaseMVPFragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    private ImageView ivFood;
    private TextInputLayout tilName;
    private EditText edtName;
    private TextInputLayout tilPrice;
    private EditText edtPrice;
    private Food food;
    private DatabaseReference mData;
    @Override
    public void setEvents() {
        getView().findViewById(R.id.btn_add_food).setOnClickListener(this);
        ivFood.setOnClickListener(this);
    }

    @Override
    public int getLayoutMain() {
        return R.layout.add_food_fragment;
    }

    @Override
    public void findViewByIds() {
        ivFood = (ImageView) getView().findViewById(R.id.iv_food);
        tilName = (TextInputLayout) getView().findViewById(R.id.til_food_name);
        tilPrice = (TextInputLayout) getView().findViewById(R.id.til_food_price);
        edtName = (EditText) getView().findViewById(R.id.edt_food_name);
        edtPrice = (EditText) getView().findViewById(R.id.edt_food_price);
    }

    @Override
    public void initComponents() {
        mData = FirebaseDatabase.getInstance().getReference();
        food = new Food();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_food:
                PopupMenu popupMenu = new PopupMenu(getContext(), ivFood);
                popupMenu.getMenuInflater().inflate(R.menu.menu_add_image, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.show();
                break;
            case R.id.btn_add_food:
                if (checkEditText()){
                    uploadFood();
                    onBackRoot();
                }
                break;
        }


    }

    private boolean checkEditText() {
        if (edtName.getText().toString().trim().equals("")) {
            tilName.setError("Bạn không được để trống dòng này!");
        }
        if (edtPrice.getText().toString().trim().equals("")) {
            tilPrice.setError("Bạn không được để trống dòng này!");
        }

        if (edtName.getText().toString().trim().equals("") || edtPrice.getText().toString().trim().equals("")) {
            return false;
        } else {
            return true;
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
        startActivityForResult(imagePickerIntent, Constants.REQUEST_LOAD_GALLERY);
    }

    private void takePicture() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(captureIntent, Constants.REQUEST_LOAD_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == getActivity().RESULT_OK && data != null) {
            if (requestCode == Constants.REQUEST_LOAD_GALLERY) {
                Uri imgUri = data.getData();
                Picasso.with(getContext()).load(imgUri).into(ivFood);
                ivFood.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            if (requestCode == Constants.REQUEST_LOAD_CAPTURE) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ivFood.setImageBitmap(bitmap);
                ivFood.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadFood() {
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Calendar calendar = Calendar.getInstance();
        food.setName(edtName.getText().toString().trim());
        food.setFoodId("f"+calendar.getTimeInMillis());
        food.setPrice(Long.parseLong(edtPrice.getText().toString().trim()));

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        ivFood.setDrawingCacheEnabled(true);
        ivFood.buildDrawingCache();
        Bitmap bitmap = ivFood.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

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
                food.setPhotoUrl(String.valueOf(taskSnapshot.getDownloadUrl()));
                //photoUrl = downloadUrl.toString();
                mData.child(Constants.STORES).child(userId).child(Constants.MENU).child(food.getFoodId()).setValue(food);
            }
        });
    }
}
