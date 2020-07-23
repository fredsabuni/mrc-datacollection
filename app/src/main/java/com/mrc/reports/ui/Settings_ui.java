package com.mrc.reports.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mrc.reports.BaseActivity;
import com.mrc.reports.R;
import com.mrc.reports.database.Mrc_db;
import com.mrc.reports.database.Region_db;
import com.mrc.reports.model.MrcItem;
import com.mrc.reports.utils.AccountUtils;
import com.mrc.reports.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Settings_ui extends BaseActivity {


    RelativeLayout mLogoutBtn;
    RelativeLayout mAboutBtn;
    Button mSyncBtn;

    Realm realm;

    ArrayList<MrcItem> mrcItems = new ArrayList<>();
    private RealmResults<Mrc_db> mrc_dbRealmResults;
    public final int STATUS_NEEDS_UPLOADED = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_ui);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        mLogoutBtn = findViewById(R.id.logout_btn);
        mAboutBtn = findViewById(R.id.about_btn);
        mSyncBtn = findViewById(R.id.sync_data_btn);

        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Settings_ui.this)
                        .setTitle(R.string.logout)
                        .setMessage(R.string.logout_help)
                        .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AccountUtils.logoutUser(Settings_ui.this);
                                Intent logoutIntent = new Intent(Settings_ui.this, Splash_ui.class);
                                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(logoutIntent);
                                Settings_ui.this.finish();

                            }
                        }).setNegativeButton(R.string.cancel,null)
                        .show();
            }
        });


        mAboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings_ui.this, AboutUs.class));
            }
        });

        mSyncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchDatabaseData();
            }
        });
    }


    public void fetchDatabaseData(){
        mrcItems.clear();

        mrc_dbRealmResults = realm.where(Mrc_db.class).equalTo("status", 2).findAll();
        if(mrc_dbRealmResults !=null && mrc_dbRealmResults.size() > 0){
            for(Mrc_db mrc_db: mrc_dbRealmResults){
                mrcItems.add(new MrcItem(mrc_db));
            }
        }

        if(mrcItems != null){

            if(!Utils.isInternetAvailable(this)) return;

            progressDialog.show();

            int count = mrcItems.size();


            JSONObject detailsObj = new JSONObject();
            JSONObject picturesObj = new JSONObject();
            JSONObject afterObj = new JSONObject();

            JSONArray materialArray = new JSONArray();
            JSONArray materialFinalArray = new JSONArray();

            JSONArray imagesArray = new JSONArray();
            JSONArray imagesFinalArray = new JSONArray();

            JSONArray finalDetailsArray = new JSONArray();

            try {
            for(int i = 0; i < count; i++){
                    detailsObj.put("pos_name", mrcItems.get(i).getPos_name());
                    detailsObj.put("pos_code", mrcItems.get(i).getPos_code());
                    detailsObj.put("shop_type", mrcItems.get(i).getShop_type_id());
                    detailsObj.put("pos_lat", mrcItems.get(i).getLat());
                    detailsObj.put("pos_long", mrcItems.get(i).getLon());
                    detailsObj.put("district", mrcItems.get(i).getDistrict_id());
                    detailsObj.put("suburb", mrcItems.get(i).getSuburb());
                    detailsObj.put("street", mrcItems.get(i).getStreet());
                    detailsObj.put("region_id", mrcItems.get(i).getRegion_id());
                    detailsObj.put("zone_id", mrcItems.get(i).getZone_id());
                    detailsObj.put("contact_person", mrcItems.get(i).getContact_person());
                    detailsObj.put("phone_number", mrcItems.get(i).getPhone_number());


                    for(int j = 0; j<mrcItems.get(i).getMaterialTypeLists().size(); j++){
                        JSONObject materialsObj = new JSONObject();
                        materialsObj.put("material_type", mrcItems.get(i).getMaterialTypeLists().get(j).getType());
                        materialsObj.put("material_quantity", mrcItems.get(i).getMaterialTypeLists().get(j).getQuantity());
                        materialsObj.put("status", mrcItems.get(i).getMaterialTypeLists().get(j).getStatus());
                        materialArray.put(materialsObj);
                    }

                    materialFinalArray.put(materialArray);

                    detailsObj.put("materials", materialFinalArray);

                    picturesObj.put("image",mrcItems.get(i).getBefore_img());
                    picturesObj.put("status","BEFORE");
                    afterObj.put("image",mrcItems.get(i).getAfter_img());
                    afterObj.put("status","AFTER");

                    imagesArray.put(picturesObj);
                    imagesArray.put(afterObj);
                    imagesFinalArray.put(imagesArray);

                    detailsObj.put("pictures", imagesFinalArray);

                    finalDetailsArray.put(detailsObj);

                }
                Log.d(TAG,"Token " + AccountUtils.getToken(this));
                Log.d(TAG,"Data " + finalDetailsArray.toString());
                Log.d(TAG,"username " + AccountUtils.getPhone(this));

                retrofit2.Call<ResponseBody> syncPos = api.dataSync(AccountUtils.getToken(this),AccountUtils.getPhone(this),finalDetailsArray.toString());
                syncPos.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        progressDialog.dismiss();
                        if(response.isSuccessful()){
                            try {
                                parseSyncData(response.body().string());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Snackbar.make(mLogoutBtn, "Could not reach server at this time please try again later", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        progressDialog.dismiss();
                        Snackbar.make(mLogoutBtn,"Something went wrong during login. Please try again",Snackbar.LENGTH_SHORT).show();
                    }
                });
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void parseSyncData(String response) throws JSONException {
        progressDialog.dismiss();
        JSONObject loginObject = new JSONObject(response);
        Log.d(TAG,"return String " + loginObject.toString());

        String mMessage = "";

        if(loginObject.getInt("code") == 200){
            JSONObject responseObject = loginObject.getJSONObject("content");
            if (!responseObject.isNull("message"))
                mMessage = responseObject.getString("message");
            Toast.makeText(this,mMessage,Toast.LENGTH_LONG).show();

            if(mrcItems.size() > 0){
                int count = mrcItems.size();
                for(int i = 0; i < count; i++){
                    //Save Data into Database
                    realm.beginTransaction();
                    Mrc_db mrcDb = new Mrc_db();
                    String ID = mrcItems.get(i).getId();

                    mrcDb.setMrc_id(ID);
                    mrcDb.setMrc_zone_id(mrcItems.get(i).getZone_id());
                    mrcDb.setMrc_region_id(mrcItems.get(i).getRegion_id());
                    mrcDb.setMrc_district_id(mrcItems.get(i).getDistrict_id());
                    mrcDb.setMrc_suburb(mrcItems.get(i).getSuburb());
                    mrcDb.setMrc_street(mrcItems.get(i).getStreet());
                    mrcDb.setMrc_pos_name(mrcItems.get(i).getPos_name());
                    mrcDb.setMrc_contact_person(mrcItems.get(i).getContact_person());
                    mrcDb.setMrc_phone_number(mrcItems.get(i).getPhone_number());
                    mrcDb.setMrc_pos_code(mrcItems.get(i).getPos_code());
                    mrcDb.setMrc_shop_type_id(mrcItems.get(i).getShop_type_id());
                    mrcDb.setMaterialTypeLists(mrcItems.get(i).getMaterialTypeLists());
                    mrcDb.setMrc_before_img(mrcItems.get(i).getBefore_img());
                    mrcDb.setMrc_after_img(mrcItems.get(i).getAfter_img());
                    mrcDb.setMrc_lat(mrcItems.get(i).getLat());
                    mrcDb.setMrc_lon(mrcItems.get(i).getLon());
                    mrcDb.setStatus(STATUS_NEEDS_UPLOADED);
                    //Insert Data into Database
                    realm.copyToRealmOrUpdate(mrcDb);
                    realm.commitTransaction();
                }
            }




        }
    }

}
