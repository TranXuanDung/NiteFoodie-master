package com.t3h.nitefoodie.ui.main.search;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.t3h.nitefoodie.R;
import com.t3h.nitefoodie.common.Constants;
import com.t3h.nitefoodie.model.Store;
import com.t3h.nitefoodie.ui.base.fragment.BaseMVPFragment;
import com.t3h.nitefoodie.ui.main.home.store.list_store.StoreAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends BaseMVPFragment implements StoreAdapter.IStoreAdapter {
    private FloatingSearchView mSearchView;
    private RecyclerView rcStore;
    private List<Store> mStores;
    private StoreAdapter mAdapter;
    private DatabaseReference mData;

    @Override
    public int getLayoutMain() {
        return R.layout.search_fragment;
    }

    @Override
    public void findViewByIds() {
        mSearchView = (FloatingSearchView) getView().findViewById(R.id.search_view);
        rcStore = (RecyclerView) getView().findViewById(R.id.rc_store);
    }

    @Override
    public void initComponents() {
        mData = FirebaseDatabase.getInstance().getReference();
        mStores = new ArrayList<>();
        mAdapter = new StoreAdapter(this, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcStore.setLayoutManager(linearLayoutManager);
        rcStore.setAdapter(mAdapter);
    }

    @Override
    public void setEvents() {
        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
            }

            @Override
            public void onSearchAction(String currentQuery) {
                search(currentQuery);
            }
        });
    }

    private void search(final String currentQuery) {
        mStores.clear();
        mAdapter.notifyDataSetChanged();
        mData.child(Constants.STORES).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Store store = dataSnapshot.getValue(Store.class);
                String name = store.getName().trim().toLowerCase();
                String query  = currentQuery.trim().toLowerCase();

                if (name.contains(query)){
                    mStores.add(store);
                    mAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getCount() {
        return mStores.size();
    }

    @Override
    public Store getPosition(int position) {
        return mStores.get(position);
    }
}
