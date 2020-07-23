package com.mrc.reports.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrc.reports.BaseActivity;
import com.mrc.reports.R;
import com.mrc.reports.adapter.PosListAdapter;
import com.mrc.reports.database.MaterialTypeList;
import com.mrc.reports.database.Mrc_db;
import com.mrc.reports.model.ErrorMessage;
import com.mrc.reports.model.MrcItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class Reports_ui extends BaseActivity {

    SwipeRefreshLayout refresh;
    ImageView emptyIcon;
    TextView emptyTitle;
    TextView emptyDescription;
    View empty;
    ArrayList<MrcItem> mrcItems = new ArrayList<>();
    RecyclerView recyclerView;

    PosListAdapter posListAdapter;

    RealmList<MaterialTypeList> materialTypeLists = new RealmList<>();

    private Map<Integer, ErrorMessage> errorMessage;

    Realm realm;

    private void showProgress(boolean show){
        refresh.setRefreshing(show);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_ui);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        recyclerView = findViewById(R.id.pos_list_item);
        refresh = findViewById(R.id.refresh);
        emptyIcon = findViewById(R.id.empty_icon);
        emptyTitle = findViewById(R.id.empty_title);
        emptyDescription = findViewById(R.id.empty_description);
        empty = findViewById(R.id.empty);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 startActivity(new Intent(Reports_ui.this, AddRecords_ui.class));
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpSwipeRefresh();
        setUpErrorMessage();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        posListAdapter =  new PosListAdapter(this,mrcItems,realm);
        recyclerView.setAdapter(posListAdapter);

        showProgress(true);
        attemptGetPos();

    }


    private void setUpSwipeRefresh(){
        //setup swipe to refresh layout
        refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary)
                ,getResources().getColor(R.color.orange_400)
                ,getResources().getColor(R.color.colorAccent));

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Attempt to get Pos
                attemptGetPos();
            }
        });
    }


    public void attemptGetPos(){
        mrcItems.clear();
        materialTypeLists.clear();
        RealmResults<Mrc_db> mrcDb = realm.where(Mrc_db.class).findAll();

        if(mrcDb != null && mrcDb.size() > 0){
            showProgress(false);
            for(Mrc_db mrc_db: mrcDb){
                mrcItems.add(new MrcItem(mrc_db));
            }

            if(mrcItems.size() > 0){
                posListAdapter.notifyDataSetChanged();
//                for(int i=0; i<mrcItems.size(); i++){
//                    Log.d("SIZE", String.valueOf(mrcItems.get(i).getMaterialTypeLists().size()));
//                }
            }

        }

        if(mrcItems.size() == 0){
            showProgress(false);
            showEmptyState(true,0);

        }
    }


    //setup all errors available in this fragment
    private void setUpErrorMessage(){
        errorMessage = new HashMap<>();
        emptyIcon.setImageResource(R.drawable.ic_pos_list);
        errorMessage.put(0,new ErrorMessage(getString(R.string.no_pos),getString(R.string.try_refresh)));
    }

    private void showEmptyState(final boolean show, int error){
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {

            long shortAnimTime = (long)(getResources().getInteger(android.R.integer.config_shortAnimTime));
            empty.setVisibility(show ? View.VISIBLE : View.GONE);
            empty.animate()
                    .setDuration(shortAnimTime)
                    .alpha( show ? 1 : 0 )
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            empty.setVisibility( show ? View.VISIBLE : View.GONE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            empty.setVisibility( show ? View.VISIBLE : View.GONE);
        }

        if(show){
            emptyTitle.setText(errorMessage.get(error).getTitle());
            emptyDescription.setText(errorMessage.get(error).getDescription());
        }
    }

}
