package com.mrc.reports.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mrc.reports.BaseActivity;
import com.mrc.reports.R;
import com.mrc.reports.adapter.RegionAdapter;
import com.mrc.reports.adapter.ShopAdapter;
import com.mrc.reports.adapter.UpdateInstalledAdapter;
import com.mrc.reports.adapter.UpdateRemovedAdapter;
import com.mrc.reports.adapter.ZoneAdapter;
import com.mrc.reports.database.MaterialList;
import com.mrc.reports.database.MaterialTypeList;
import com.mrc.reports.database.Materials_db;
import com.mrc.reports.database.Mrc_db;
import com.mrc.reports.database.Region_db;
import com.mrc.reports.database.ShopType_db;
import com.mrc.reports.database.Zone_db;
import com.mrc.reports.model.MaterialItem;
import com.mrc.reports.model.MrcItem;
import com.mrc.reports.model.RegionItem;
import com.mrc.reports.model.ShopItem;
import com.mrc.reports.model.ZoneItem;
import com.mrc.reports.utils.AccountUtils;
import com.mrc.reports.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Update_ui extends BaseActivity implements UpdateRemovedAdapter.ItemClickListener, UpdateInstalledAdapter.MaterialClickListener{

    RecyclerView mInstalledList;
    RecyclerView mRemovedList;
    FloatingActionButton mBeforeBtn;
    FloatingActionButton mAfterBtn;
    ImageView mBeforeImg;
    ImageView mAfterImg;
    Spinner mZoneSpinner;
    Spinner mRegionSpinner;
    Spinner mShopSpinner;
    Button mAddUpdateBtn;
    TextInputEditText mDistrict;
    TextInputEditText mSuburb;
    TextInputEditText mStreet;
    TextInputEditText mPosName;
    TextInputEditText mContactPerson;
    TextInputEditText mPhoneNumber;
    TextInputEditText mPosCode;

    ArrayList<MaterialItem> materialItems_installed = new ArrayList<>();
    ArrayList<MaterialItem> materialItems_removed = new ArrayList<>();
    ArrayList<ZoneItem> zoneItems = new ArrayList<>();
    ArrayList<RegionItem> regionItems = new ArrayList<>();
    ArrayList<ShopItem> shopItems = new ArrayList<>();

    public static final String CURRENT_MRC = "current_item";
    public static final String CURRENT_MATERIAL = "current_material";

    private MrcItem mCurrentItem;

    UpdateRemovedAdapter updateRemovedAdapter;
    UpdateInstalledAdapter updateInstalledAdapter;
    ZoneAdapter zoneAdapter;
    RegionAdapter regionAdapter;
    ShopAdapter shopAdapter;

    public final int REQ_CODE_BEFORE = 101;
    public final int REQ_CODE_AFTER = 102;
    public final int STATUS_NEEDS_UPDATE = 2;
    public final int STATUS_NEEDS_UPLOADED = 3;
    private RealmResults<Zone_db> zoneDbRealmResults;
    private RealmResults<Region_db> regionDbRealmResults;
    private RealmResults<ShopType_db> shopTypeDbRealmResults;
    private RealmResults<Materials_db> materialsDbRealmResults;
    Realm realm;

    //Variables
    RealmList<MaterialTypeList> mMaterials = new RealmList<>();
//    ArrayList<MaterialTypeList> mCurrentMaterials = new ArrayList<>();
    ArrayList<MrcItem> mrcItems = new ArrayList<>();

    String BeforeData, AfterData;
    String ID,ZoneData, RegionData, DistrictData, SuburbData, StreetData, PosNameData, ContactData, PhoneData, PosCodeData, ShopData, AfterImg, LatData, LonData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ui);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null){
            mCurrentItem = getIntent().getParcelableExtra(CURRENT_MRC);
        }else {
            mCurrentItem = savedInstanceState.getParcelable(CURRENT_MRC);
        }

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        mInstalledList= findViewById(R.id.material_installed_list);
        mRemovedList = findViewById(R.id.material_removed_list);
        mBeforeBtn = findViewById(R.id.before_btn);
        mAfterBtn = findViewById(R.id.after_btn);
        mBeforeImg = findViewById(R.id.before_img);
        mAfterImg = findViewById(R.id.after_img);
        mZoneSpinner = findViewById(R.id.zone_spinner);
        mRegionSpinner = findViewById(R.id.region_spinner);
        mShopSpinner = findViewById(R.id.shop_type_spinner);
        mAddUpdateBtn = findViewById(R.id.add_update_btn);
        mDistrict = findViewById(R.id.edt_district);
        mSuburb = findViewById(R.id.edt_suburb);
        mStreet = findViewById(R.id.edt_street);
        mPosName = findViewById(R.id.edt_pos_name);
        mContactPerson = findViewById(R.id.edt_contact_person);
        mPhoneNumber = findViewById(R.id.edt_phone);
        mPosCode = findViewById(R.id.edt_pos_code);

        //get Data from Server
        getServerData();

        //Query MaterialType Data
        getMaterialTypeData();

        //Set Data
        mDistrict.setText(mCurrentItem.getDistrict_id());
        mSuburb.setText(mCurrentItem.getSuburb());
        mStreet.setText(mCurrentItem.getStreet());
        mPosName.setText(mCurrentItem.getPos_name());
        mContactPerson.setText(mCurrentItem.getContact_person());
        mPhoneNumber.setText(mCurrentItem.getPhone_number());
        mPosCode.setText(mCurrentItem.getPos_code());

        Bitmap photo = Utils.base64ToBitmap(mCurrentItem.getBefore_img());
        mBeforeImg.setImageBitmap(photo);


        //Setting Material Removed Adapter
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        mRemovedList.setLayoutManager(linearLayoutManager);
//        updateRemovedAdapter = new UpdateRemovedAdapter(this, materialItems_removed, mCurrentMaterials);
//        mRemovedList.setAdapter(updateRemovedAdapter);
//        updateRemovedAdapter.setItemClickListener(this);

        //Setting Material Installed Adapter
//        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
//        mInstalledList.setLayoutManager(LayoutManager);
//        updateInstalledAdapter = new UpdateInstalledAdapter(this, materialItems_installed);
//        mInstalledList.setAdapter(updateInstalledAdapter);
//        updateInstalledAdapter.setMaterialClickListener(this);


        mBeforeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Take a picture before Installation
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQ_CODE_BEFORE);
            }
        });

        mAfterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Take a picture after Installation
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQ_CODE_AFTER);
            }
        });

        //Zone Spinner
        zoneAdapter = new ZoneAdapter(this,zoneItems);
        mZoneSpinner.setAdapter(zoneAdapter);
        mZoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ZoneItem clickedItem = (ZoneItem) parent.getItemAtPosition(position);
                ZoneData = clickedItem.getId();
                //get CityData
                getReligionData(clickedItem.getId());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Region Spinner
        regionAdapter = new RegionAdapter(this,regionItems);
        mRegionSpinner.setAdapter(regionAdapter);
        mRegionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RegionItem clickedItem = (RegionItem) parent.getItemAtPosition(position);
                RegionData = clickedItem.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Shop Type Spinner
        shopAdapter = new ShopAdapter(this,shopItems);
        mShopSpinner.setAdapter(shopAdapter);
        mShopSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ShopItem clickedItem = (ShopItem) parent.getItemAtPosition(position);
                ShopData = clickedItem.getShop_type();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mAddUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUpdateData();
            }
        });
    }


    public void getMaterialTypeData(){
        mrcItems.clear();
        mMaterials.clear();
        realm.beginTransaction();
        RealmResults<Mrc_db> realmProducts = realm.where(Mrc_db.class).equalTo("mrc_id",mCurrentItem.getId()).findAll();

        if(realmProducts != null && realmProducts.size() > 0){
            for(Mrc_db mrc_db: realmProducts){
                mrcItems.add(new MrcItem(mrc_db));
            }
        }

        for(int i = 0; i < mrcItems.size(); i++){
            mMaterials.addAll(mrcItems.get(i).getMaterialTypeLists());
        }


//        JSONArray materialArray = new JSONArray();

//        for(int i = 0; i < mMaterials.size(); i++){
////            Log.d("Materials", mMaterials.get(i).getStatus());
//        }

//        try{
//            for(int i = 0; i < mMaterials.size(); i++){
//                JSONObject materialObj = new JSONObject();
//                materialObj.put("material_type", mMaterials.get(i).getType());
//                materialObj.put("material_quantity", mMaterials.get(i).getQuantity());
//                materialObj.put("status", mMaterials.get(i).getStatus());
//                Log.d("Materials", mMaterials.get(i).getType());
//                materialArray.put(materialObj);
//            }
//            Log.d("Materials", materialArray.toString());
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }



        realm.commitTransaction();

    }


    public void onUpdateData(){

        DistrictData = mDistrict.getText().toString();
        SuburbData = mSuburb.getText().toString();
        StreetData = mStreet.getText().toString();
        PosNameData = mPosName.getText().toString();
        ContactData = mContactPerson.getText().toString();
        PhoneData = mPhoneNumber.getText().toString();
        PosCodeData = mPosCode.getText().toString();
        LatData = String.valueOf(MLATITUDE);
        LonData = String.valueOf(MLONGITUDE);
        BeforeData = mCurrentItem.getBefore_img();

        // Check for a valid Zone, if the user enter one.
        if(TextUtils.isEmpty(ZoneData)){
            Snackbar.make(mAddUpdateBtn,getString(R.string.zone_error), Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Check for a valid Region, if the user enter one.
        if(TextUtils.isEmpty(RegionData)){
            Snackbar.make(mAddUpdateBtn,getString(R.string.region_error), Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Check for a valid District
        if(TextUtils.isEmpty(DistrictData)){
            mDistrict.setError(getString(R.string.district_error));
            mDistrict.requestFocus();
            return;
        }

        // Check for a valid Suburb
        if(TextUtils.isEmpty(SuburbData)){
            mSuburb.setError(getString(R.string.suburb_error));
            mSuburb.requestFocus();
            return;
        }

        // Check for a valid Street
        if(TextUtils.isEmpty(StreetData)){
            mStreet.setError(getString(R.string.street_error));
            mStreet.requestFocus();
            return;
        }

        // Check for a valid Pos Name
        if(TextUtils.isEmpty(PosNameData)){
            mPosName.setError(getString(R.string.pos_name_error));
            mPosName.requestFocus();
            return;
        }

        // Check for a valid Contact Person
        if(TextUtils.isEmpty(ContactData)){
            mContactPerson.setError(getString(R.string.contact_error));
            mContactPerson.requestFocus();
            return;
        }

        // Check for a valid Contact Person
        if(TextUtils.isEmpty(PhoneData)){
            mPhoneNumber.setError(getString(R.string.phone_error));
            mPhoneNumber.requestFocus();
            return;
        }

        // Check for a valid Contact Person
//        if(TextUtils.isEmpty(PosCodeData)){
//            mPosCode.setError(getString(R.string.pos_code_error));
//            mPosCode.requestFocus();
//            return;
//        }

        // Check for a valid Shop Type, if the user enter one.
        if(TextUtils.isEmpty(ShopData)){
            Snackbar.make(mAddUpdateBtn,getString(R.string.shop_error), Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Check for a valid Shop Type, if the user enter one.
        if(TextUtils.isEmpty(BeforeData)){
            Snackbar.make(mAddUpdateBtn,getString(R.string.picture_error), Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Check for a valid Shop Type, if the user enter one.
        if(TextUtils.isEmpty(AfterData)){
            Snackbar.make(mAddUpdateBtn,getString(R.string.picture_error), Snackbar.LENGTH_SHORT).show();
            return;
        }

        //Save Data into Database
        realm.beginTransaction();
        progressDialog.show();
        Mrc_db mrcDb = new Mrc_db();
        ID = mCurrentItem.getId();

        mrcDb.setMrc_id(ID);
        mrcDb.setMrc_zone_id(ZoneData);
        mrcDb.setMrc_region_id(RegionData);
        mrcDb.setMrc_district_id(DistrictData);
        mrcDb.setMrc_suburb(SuburbData);
        mrcDb.setMrc_street(StreetData);
        mrcDb.setMrc_pos_name(PosNameData);
        mrcDb.setMrc_contact_person(ContactData);
        mrcDb.setMrc_phone_number(PhoneData);
        mrcDb.setMrc_pos_code(PosCodeData);
        mrcDb.setMrc_shop_type_id(ShopData);
        mrcDb.setMaterialTypeLists(mMaterials);
        mrcDb.setMrc_before_img(BeforeData);
        mrcDb.setMrc_after_img(AfterData);
        mrcDb.setMrc_lat(String.valueOf(MLATITUDE));
        mrcDb.setMrc_lon(String.valueOf(MLONGITUDE));
        mrcDb.setStatus(STATUS_NEEDS_UPDATE);
        //Insert Data into Database
        realm.copyToRealmOrUpdate(mrcDb);
        realm.commitTransaction();

        if(Utils.isInternetAvailable(this)){
            progressDialog.show();

            JSONObject posDetails = new JSONObject();
            JSONArray posArray = new JSONArray();
            JSONObject imgBeforeObj = new JSONObject();
            JSONObject imgAfterObj = new JSONObject();
            JSONArray imgArray = new JSONArray();
            JSONArray materialArray = new JSONArray();
            try {
                posDetails.put("pos_name", PosNameData);
                posDetails.put("pos_code", PosCodeData);
                posDetails.put("shop_type", ShopData);
                posDetails.put("pos_lat", String.valueOf(MLATITUDE));
                posDetails.put("pos_long", String.valueOf(MLONGITUDE));
                posDetails.put("district", DistrictData);
                posDetails.put("suburb", SuburbData);
                posDetails.put("street", StreetData);
                posDetails.put("region_id", RegionData);
                posDetails.put("zone_id", ZoneData);
                posDetails.put("contact_person", ContactData);
                posDetails.put("phone_number", PhoneData);
                posArray.put(posDetails);

                //Creating Object for Images
                imgBeforeObj.put("image", BeforeData);
                imgBeforeObj.put("status", "BEFORE");

                imgAfterObj.put("image", AfterData);
                imgAfterObj.put("status", "AFTER");

                imgArray.put(imgAfterObj);
                imgArray.put(imgBeforeObj);

                for(int i = 0; i<mMaterials.size(); i++){
                    JSONObject materialObj = new JSONObject();
                    materialObj.put("material_type", mMaterials.get(i).getType());
                    materialObj.put("material_quantity", mMaterials.get(i).getQuantity());
                    materialObj.put("status", mMaterials.get(i).getStatus());
                    materialArray.put(materialObj);
                }

                Log.d(TAG,"Token " + AccountUtils.getToken(this));
                Log.d(TAG,"Details " + posArray.toString());
                Log.d(TAG,"Materials " + materialArray.toString());
                Log.d(TAG,"images " + imgArray.toString());

                retrofit2.Call<ResponseBody> registerPos = api.posRegister(AccountUtils.getToken(this),posArray.toString(),materialArray.toString(),imgArray.toString());
                registerPos.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        progressDialog.dismiss();
                        if(response.isSuccessful()){
                            try {
                                parsePosData(response.body().string());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Snackbar.make(mAfterBtn, "Could not reach server at this time please try again later", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        progressDialog.dismiss();
                        Snackbar.make(mAfterBtn,"Something went wrong during login. Please try again",Snackbar.LENGTH_SHORT).show();
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }else {
            //Check if Data Is Insert
            Mrc_db mrc_db = realm.where(Mrc_db.class).equalTo("mrc_id",ID).findFirst();

            if(mrc_db != null){
                progressDialog.dismiss();

                //Clear Data
                AlertDialog alertDialog = new AlertDialog.Builder(Update_ui.this).create();
                alertDialog.setTitle("Success");
                alertDialog.setMessage("Data saved successfully");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }else {
                progressDialog.dismiss();
                Log.d(TAG, "Data not saved");
            }

        }

    }

    public void parsePosData(String response) throws JSONException {
        progressDialog.dismiss();
        JSONObject loginObject = new JSONObject(response);
        Log.d(TAG,"return String " + loginObject.toString());

        String mMessage = "";

        if(loginObject.getInt("code") == 200){
            JSONObject responseObject = loginObject.getJSONObject("content");
            if (!responseObject.isNull("message"))
                mMessage = responseObject.getString("message");
            Toast.makeText(this,mMessage,Toast.LENGTH_LONG).show();

            //Save Data into Database
            realm.beginTransaction();
            Mrc_db mrcDb = new Mrc_db();
            ID = mCurrentItem.getId();

            mrcDb.setMrc_id(ID);
            mrcDb.setMrc_zone_id(ZoneData);
            mrcDb.setMrc_region_id(RegionData);
            mrcDb.setMrc_district_id(DistrictData);
            mrcDb.setMrc_suburb(SuburbData);
            mrcDb.setMrc_street(StreetData);
            mrcDb.setMrc_pos_name(PosNameData);
            mrcDb.setMrc_contact_person(ContactData);
            mrcDb.setMrc_phone_number(PhoneData);
            mrcDb.setMrc_pos_code(PosCodeData);
            mrcDb.setMrc_shop_type_id(ShopData);
            mrcDb.setMaterialTypeLists(mMaterials);
            mrcDb.setMrc_before_img(BeforeData);
            mrcDb.setMrc_after_img(AfterImg);
            mrcDb.setMrc_lat(String.valueOf(MLATITUDE));
            mrcDb.setMrc_lon(String.valueOf(MLONGITUDE));
            mrcDb.setStatus(STATUS_NEEDS_UPLOADED);
            //Insert Data into Database
            realm.copyToRealmOrUpdate(mrcDb);
            realm.commitTransaction();


        }
    }

    //Query Region based on Zone
    public void getReligionData(String zoneID){
        regionItems.clear();
        //Query the Database
        regionDbRealmResults = realm.where(Region_db.class).equalTo("zone_id", zoneID).findAll();
        if(regionDbRealmResults != null && regionDbRealmResults.size() > 0){
            for(Region_db regionDb: regionDbRealmResults){
                regionItems.add(new RegionItem(regionDb));
            }
            regionAdapter.notifyDataSetChanged();

            //setSelected country Adapter
            for(int i=0; i < regionAdapter.getCount(); i++) {
                if(!TextUtils.isEmpty(mCurrentItem.getRegion_id())) {
                    if (mCurrentItem.getRegion_id().trim().equals(regionAdapter.getItem(i).getId())) {
                        mRegionSpinner.setSelection(i);
                        RegionData = mCurrentItem.getRegion_id();
                        break;
                    }
                }
            }
        }

    }


    public void getServerData(){

        if(!Utils.isInternetAvailable(this)) return;

        Call<ResponseBody> getParameters = api.getParameters();
        getParameters.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        parseData(response.body().string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    Snackbar.make(mAfterBtn, R.string.server_reachable_error, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String message = t.getMessage();
                Log.d("failure", message);
                Snackbar.make(mAfterBtn,R.string.wrong_error,Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    public void parseData(String response) throws JSONException {

        shopItems.clear();
        zoneItems.clear();
        materialItems_removed.clear();
        materialItems_installed.clear();

        //Clear data from Database
        try {
            //Delete all from tables
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
//                    zoneDbRealmResults.deleteAllFromRealm();
                    regionDbRealmResults.deleteAllFromRealm();
//                    shopTypeDbRealmResults.deleteAllFromRealm();
//                    materialsDbRealmResults.deleteAllFromRealm();
                    Log.d("TAG","Deleted all available data");
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

        JSONObject data = new JSONObject(response);
        Log.d(TAG, data.toString());
        if(data.getInt("status_code") == 200){
            JSONObject dataObj = data.getJSONObject("data");

            //Getting ZoneData's
            if(!dataObj.isNull("zones")){
                JSONArray zonesArray = dataObj.getJSONArray("zones");
                int count_zone = zonesArray.length();
                for(int i = 0; i<count_zone; i++){
                    JSONObject zoneObj = zonesArray.getJSONObject(i);
                    ZoneItem zoneItem = new ZoneItem();
                    if(!zoneObj.isNull("id"))
                        zoneItem.setId(zoneObj.getString("id"));
                    if(!zoneObj.isNull("zone_name"))
                        zoneItem.setLocation_name(zoneObj.getString("zone_name"));
                    zoneItems.add(zoneItem);
                }
                zoneAdapter.notifyDataSetChanged();

                //setSelected country Adapter
                for(int i=0; i < zoneAdapter.getCount(); i++) {
                    if(!TextUtils.isEmpty(mCurrentItem.getZone_id())) {
                        if (mCurrentItem.getZone_id().trim().equals(zoneAdapter.getItem(i).getId())) {
                            mZoneSpinner.setSelection(i);
                            ZoneData = mCurrentItem.getZone_id();
                            break;
                        }
                    }
                }

            }


            //Getting RegionData's
            if(!dataObj.isNull("regions")){
                JSONArray regionsArray = dataObj.getJSONArray("regions");
                int count_region = regionsArray.length();
                for(int i = 0; i<count_region; i++){
                    JSONObject regionObj = regionsArray.getJSONObject(i);
                    RegionItem regionItem = new RegionItem();
                    if(!regionObj.isNull("id"))
                        regionItem.setId(regionObj.getString("id"));
                    if(!regionObj.isNull("region_name"))
                        regionItem.setLocation_name(regionObj.getString("region_name"));
                    if(!regionObj.isNull("zone_id"))
                        regionItem.setZone_id(regionObj.getString("zone_id"));
                    regionItems.add(regionItem);
                }

                if(regionItems.size() > 0){
                    Log.d("TAG",regionItems.toString());
                    //persist data to local database
                    Region_db.persistToDatabase(regionItems);
                }


            }

            //Getting ShopType Data
            if(!dataObj.isNull("shop_type")){
                JSONArray shopTypeArray = dataObj.getJSONArray("shop_type");
                int count_shop = shopTypeArray.length();
                for(int i = 0; i<count_shop; i++){
                    JSONObject shopObj = shopTypeArray.getJSONObject(i);
                    ShopItem shopItem = new ShopItem();
                    if(!shopObj.isNull("id"))
                        shopItem.setId(shopObj.getString("id"));
                    if(!shopObj.isNull("shop_type"))
                        shopItem.setShop_type(shopObj.getString("shop_type"));
                    shopItems.add(shopItem);
                }
                shopAdapter.notifyDataSetChanged();

                //setSelected country Adapter
                for(int i=0; i < shopAdapter.getCount(); i++) {
                    if(!TextUtils.isEmpty(mCurrentItem.getShop_type_id())) {
                        if (mCurrentItem.getShop_type_id().trim().equals(shopAdapter.getItem(i).getId())) {
                            mShopSpinner.setSelection(i);
                            ShopData = mCurrentItem.getShop_type_id();
                            break;
                        }
                    }
                }

            }

            //Getting Materials  Data
//            if(!dataObj.isNull("material_type")){
//                JSONArray materialTypeArray = dataObj.getJSONArray("material_type");
//                int count_material = materialTypeArray.length();
//                for(int i = 0; i<count_material; i++){
//                    JSONObject materialObj = materialTypeArray.getJSONObject(i);
//                    MaterialItem materialItem = new MaterialItem();
//                    if(!materialObj.isNull("id"))
//                        materialItem.setId(materialObj.getString("id"));
//                    if(!materialObj.isNull("material_type"))
//                        materialItem.setName(materialObj.getString("material_type"));
//                    materialItems_removed.add(materialItem);
//                    materialItems_installed.add(materialItem);
//                }
//                updateRemovedAdapter.notifyDataSetChanged();
//                updateInstalledAdapter.notifyDataSetChanged();
//
//            }



        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_BEFORE && resultCode == Activity.RESULT_OK && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mBeforeImg.setImageBitmap(photo);
            BeforeData = Utils.bitmapToBase64(photo);
        }else if(requestCode == REQ_CODE_AFTER && resultCode == Activity.RESULT_OK && data != null){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mAfterImg.setImageBitmap(photo);
            AfterData = Utils.bitmapToBase64(photo);
        }
    }


    @Override
    public void onItemClick(View view , RealmList<MaterialTypeList> materialItems) {
        Log.d("Installed", materialItems.get(0).getStatus());
        mMaterials = materialItems;
    }

    @Override
    public void onRemoveItemClick(View view, int position) {
        mMaterials.remove(position);
    }

    @Override
    public void onCurrentItems(View view , ArrayList<MaterialItem> materialItems) {
    }

    @Override
    public void onMaterialClick(View view, RealmList<MaterialTypeList> materialItems) {
        mMaterials = materialItems;
    }

    @Override
    public void onMaterialRemoveClick(View view, int position) {
        Log.d("Removed",mMaterials.get(position).getStatus());
        mMaterials.remove(position);
    }

}
