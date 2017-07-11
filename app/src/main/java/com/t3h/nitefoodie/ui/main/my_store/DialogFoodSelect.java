package com.t3h.nitefoodie.ui.main.my_store;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.t3h.nitefoodie.R;

public class DialogFoodSelect extends Dialog implements View.OnClickListener {
    private OnClickDialog mInterf;
    private TextView tvDelete;
    private TextView tvEdit;

    public DialogFoodSelect(@NonNull Context context, OnClickDialog interf) {
        super(context);
        mInterf = interf;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_food_select);
        findViewByIds();
        setEvents();
    }

    private void findViewByIds() {
    }

    private void setEvents() {
        findViewById(R.id.tv_delete).setOnClickListener(this);
        findViewById(R.id.tv_edit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_delete:
                mInterf.onClickDelete();
                dismiss();
                break;
            case R.id.tv_edit:
                mInterf.onClickEdit();
                dismiss();
                break;
        }
    }

    interface OnClickDialog {
        void onClickDelete();

        void onClickEdit();
    }
}
