package com.t3h.nitefoodie.ui.main.home.store;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.t3h.nitefoodie.R;
import com.t3h.nitefoodie.ui.main.home.store.store_detail.StoreDetailActivity;

public class HotSaleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private IHotSaleAdapter mInterf;

    public HotSaleAdapter(IHotSaleAdapter interf) {
        mInterf = interf;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.hotsale_item_layout, parent, false);
        return new HotSaleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mInterf.getCount();
    }

    static class HotSaleViewHolder extends RecyclerView.ViewHolder {
        ImageView ivStore;
        TextView tvSaleContent;
        TextView tvStoreName;

        public HotSaleViewHolder(final View itemView) {
            super(itemView);
            ivStore = (ImageView) itemView.findViewById(R.id.iv_store);
            tvSaleContent = (TextView) itemView.findViewById(R.id.tv_sale_content);
            tvStoreName = (TextView) itemView.findViewById(R.id.tv_store_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = itemView.getContext();
                    Intent intent = new Intent(context, StoreDetailActivity.class);
                    context.startActivity(intent,
                            ActivityOptionsCompat.makeSceneTransitionAnimation
                                    ((Activity) context, ivStore, "shareStore").toBundle());
                }
            });
        }
    }

    public interface IHotSaleAdapter {
        int getCount();
    }
}
