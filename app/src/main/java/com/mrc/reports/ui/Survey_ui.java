package com.mrc.reports.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.mrc.reports.BaseActivity;
import com.mrc.reports.R;
import com.mrc.reports.adapter.CompetitorAdapter;
import com.mrc.reports.adapter.CompetitorMaterialAdapter;
import com.mrc.reports.adapter.MaterialAdapter;
import com.mrc.reports.adapter.PosListAdapter;
import com.mrc.reports.adapter.RegionAdapter;
import com.mrc.reports.adapter.ServicesAdapter;
import com.mrc.reports.adapter.ShopAdapter;
import com.mrc.reports.adapter.SurveyPosListAdapter;
import com.mrc.reports.adapter.ZoneAdapter;
import com.mrc.reports.database.Competitor_db;
import com.mrc.reports.database.MaterialTypeList;
import com.mrc.reports.database.Materials_db;
import com.mrc.reports.database.Mrc_db;
import com.mrc.reports.database.Region_db;
import com.mrc.reports.database.Service_db;
import com.mrc.reports.database.ShopType_db;
import com.mrc.reports.database.SurveyMaterialList;
import com.mrc.reports.database.Survey_db;
import com.mrc.reports.database.Zone_db;
import com.mrc.reports.model.CompetitorItem;
import com.mrc.reports.model.ErrorMessage;
import com.mrc.reports.model.MaterialItem;
import com.mrc.reports.model.MrcItem;
import com.mrc.reports.model.RegionItem;
import com.mrc.reports.model.ServiceItem;
import com.mrc.reports.model.ShopItem;
import com.mrc.reports.model.SurveyItem;
import com.mrc.reports.model.ZoneItem;
import com.mrc.reports.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Survey_ui extends BaseActivity {

    SwipeRefreshLayout refresh;
    ImageView emptyIcon;
    TextView emptyTitle;
    TextView emptyDescription;
    View empty;
    ArrayList<SurveyItem> surveyItems = new ArrayList<>();
    RecyclerView recyclerView;

    SurveyPosListAdapter surveyPosListAdapter;
    RealmList<SurveyMaterialList> surveyMaterialLists = new RealmList<>();
    private Map<Integer, ErrorMessage> errorMessage;

    Realm realm;

    private void showProgress(boolean show){
        refresh.setRefreshing(show);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_ui);
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
                startActivity(new Intent(Survey_ui.this, AddSurvey_ui.class));
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpSwipeRefresh();
        setUpErrorMessage();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        surveyPosListAdapter =  new SurveyPosListAdapter(this,surveyItems,realm);
        recyclerView.setAdapter(surveyPosListAdapter);

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
        surveyItems.clear();
        surveyMaterialLists.clear();
        RealmResults<Survey_db> surveyDb = realm.where(Survey_db.class).findAll();

        if(surveyDb != null && surveyDb.size() > 0){
            showProgress(false);
            for(Survey_db survey_db: surveyDb){
                surveyItems.add(new SurveyItem(survey_db));
            }

            if(surveyItems.size() > 0){
                surveyPosListAdapter.notifyDataSetChanged();
//                for(int i=0; i<surveyItems.size(); i++){
//                    Log.d("SIZE", String.valueOf(surveyItems.get(i).getSurveyMaterialLists().size()));
//                }
            }

        }

        if(surveyItems.size() == 0){
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