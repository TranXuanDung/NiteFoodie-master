package com.t3h.nitefoodie.ui.main.home.store.list_store;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.t3h.nitefoodie.R;
import com.t3h.nitefoodie.common.Constants;
import com.t3h.nitefoodie.model.Store;
import com.t3h.nitefoodie.ui.Utils;
import com.t3h.nitefoodie.ui.main.home.store.store_detail.StoreDetailActivity;

public class StoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private IStoreAdapter mInterf;
    private Context mContext;

    public StoreAdapter(IStoreAdapter interf, Context context) {
        mInterf = interf;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.store_item_layout, parent, false);
        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Store store = mInterf.getPosition(position);
        final StoreViewHolder viewHolder = (StoreViewHolder) holder;
        Picasso.with(mContext).load(store.getPhotoUrl()).into(viewHolder.ivStore);
        viewHolder.ivStore.setScaleType(ImageView.ScaleType.CENTER_CROP);
        viewHolder.tvStoreName.setText(store.getName());
        viewHolder.tvStoreAddress.setText(store.getAddress());
        viewHolder.ratingBar.setRating((float) store.getRate());
        viewHolder.tvTag.setText(store.getTag());

        if (Utils.isStoreOpen(store.getOpenTime(), store.getCloseTime())) {
            viewHolder.ivOnlineState.setImageResource(R.drawable.online);
        } else {
            viewHolder.ivOnlineState.setImageResource(R.drawable.offline);
        }

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StoreDetailActivity.class);
                intent.putExtra(Constants.ID_USER, store.getsId());
                mContext.startActivity(intent,
                        ActivityOptionsCompat.makeSceneTransitionAnimation
                                ((Activity) mContext, viewHolder.ivStore, "shareStore").toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mInterf.getCount();
    }

    static class StoreViewHolder extends RecyclerView.ViewHolder {
        ImageView ivStore;
        TextView tvStoreName;
        TextView tvStoreAddress;
        TextView tvTag;
        ImageView ivOnlineState;
        RatingBar ratingBar;
        CardView cardView;

        public StoreViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
            ivStore = (ImageView) itemView.findViewById(R.id.iv_store);
            tvStoreName = (TextView) itemView.findViewById(R.id.tv_store_name);
            tvStoreAddress = (TextView) itemView.findViewById(R.id.tv_store_address);
            tvTag = (TextView) itemView.findViewById(R.id.tv_tag);
            ivOnlineState = (ImageView) itemView.findViewById(R.id.iv_online_state);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rating_bar);
        }
    }

    public interface IStoreAdapter {
        int getCount();

        Store getPosition(int position);
    }
}
