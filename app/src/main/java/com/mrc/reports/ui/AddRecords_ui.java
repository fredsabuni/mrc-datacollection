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

import com.google.android.material.textfield.TextInputEditText;
import com.mrc.reports.BaseActivity;
import com.mrc.reports.R;
import com.mrc.reports.adapter.MaterialInstalled;
import com.mrc.reports.adapter.MaterialRemoved;
import com.mrc.reports.adapter.RegionAdapter;
import com.mrc.reports.adapter.ShopAdapter;
import com.mrc.reports.adapter.ZoneAdapter;
import com.mrc.reports.database.MaterialList;
import com.mrc.reports.database.MaterialTypeList;
import com.mrc.reports.database.Materials_db;
import com.mrc.reports.database.Mrc_db;
import com.mrc.reports.database.Region_db;
import com.mrc.reports.database.ShopType_db;
import com.mrc.reports.database.Zone_db;
import com.mrc.reports.model.MaterialItem;
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

public class AddRecords_ui extends BaseActivity implements MaterialRemoved.ItemClickListener, MaterialInstalled.MaterialClickListener{

    RecyclerView mInstalledList;
    RecyclerView mRemovedList;
    FloatingActionButton mBeforeBtn;
    FloatingActionButton mAfterBtn;
    ImageView mBeforeImg;
    ImageView mAfterImg;
    Spinner mZoneSpinner;
    Spinner mRegionSpinner;
    Spinner mShopSpinner;
    Button mAddRecordBtn;
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

    MaterialRemoved materialAdapter_in;
    MaterialInstalled materialAdapter_;
    ZoneAdapter zoneAdapter;
    RegionAdapter regionAdapter;
    ShopAdapter shopAdapter;

    public final int REQ_CODE_BEFORE = 101;
    public final int REQ_CODE_AFTER = 102;
    public final int STATUS_NEEDS_UPDATE = 1;
    private RealmResults<Zone_db> zoneDbRealmResults;
    private RealmResults<Region_db> regionDbRealmResults;
    private RealmResults<ShopType_db> shopTypeDbRealmResults;
    private RealmResults<Materials_db> materialsDbRealmResults;
    Realm realm;

    //Variables
    ArrayList<MaterialItem> mMaterials = new ArrayList<>();
    RealmList<MaterialTypeList> materialTypeLists = new RealmList<>();
    String BeforeData;
    String ID,ZoneData, RegionData, DistrictData, SuburbData, StreetData, PosNameData, ContactData, PhoneData, PosCodeData, ShopData, AfterImg, LatData, LonData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_records_ui);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        mMaterials.clear();

        mInstalledList= findViewById(R.id.material_installed_list);
        mRemovedList = findViewById(R.id.material_removed_list);
        mBeforeBtn = findViewById(R.id.before_btn);
        mAfterBtn = findViewById(R.id.after_btn);
        mBeforeImg = findViewById(R.id.before_img);
        mAfterImg = findViewById(R.id.after_img);
        mZoneSpinner = findViewById(R.id.zone_spinner);
        mRegionSpinner = findViewById(R.id.region_spinner);
        mShopSpinner = findViewById(R.id.shop_type_spinner);
        mAddRecordBtn = findViewById(R.id.add_record_btn);
        mDistrict = findViewById(R.id.edt_district);
        mSuburb = findViewById(R.id.edt_suburb);
        mStreet = findViewById(R.id.edt_street);
        mPosName = findViewById(R.id.edt_pos_name);
        mContactPerson = findViewById(R.id.edt_contact_person);
        mPhoneNumber = findViewById(R.id.edt_phone);
        mPosCode = findViewById(R.id.edt_pos_code);

        //get Data from Server
        getServerData();

        if(materialItems_removed.size() == 0){
            try{
                //Query the Database
                materialsDbRealmResults = realm.where(Materials_db.class).findAll();
                if(materialsDbRealmResults != null && materialsDbRealmResults.size() > 0){
                    for(Materials_db materials_db: materialsDbRealmResults){
                        materialItems_removed.add(new MaterialItem(materials_db));
                        materialItems_installed.add(new MaterialItem(materials_db));
                    }
                    materialAdapter_in.notifyDataSetChanged();
                    materialAdapter_.notifyDataSetChanged();

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        //Setting Material Removed Adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRemovedList.setLayoutManager(linearLayoutManager);
        materialAdapter_in = new MaterialRemoved(this, materialItems_removed);
        mRemovedList.setAdapter(materialAdapter_in);
        materialAdapter_in.setItemClickListener(this);

        //Setting Material Installed Adapter
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        mInstalledList.setLayoutManager(LayoutManager);
        materialAdapter_ = new MaterialInstalled(this, materialItems_installed);
        mInstalledList.setAdapter(materialAdapter_);
        materialAdapter_.setMaterialClickListener(this);


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

        if(zoneItems.size() == 0){
            try{
                //Query the Database
                zoneDbRealmResults = realm.where(Zone_db.class).findAll();
                if(zoneDbRealmResults != null && zoneDbRealmResults.size() > 0){
                    for(Zone_db zone_db: zoneDbRealmResults){
                        zoneItems.add(new ZoneItem(zone_db));
                    }
                    zoneAdapter.notifyDataSetChanged();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

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

        if(shopItems.size() == 0){
            try{
                //Query the Database
                shopTypeDbRealmResults = realm.where(ShopType_db.class).findAll();
                if(shopTypeDbRealmResults != null && shopTypeDbRealmResults.size() > 0){
                    for(ShopType_db shopType_db: shopTypeDbRealmResults){
                        shopItems.add(new ShopItem(shopType_db));
                    }
                    shopAdapter.notifyDataSetChanged();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

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

        mAddRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //On Adding Button Records
                onRecordingData();
            }
        });

    }


    public void onRecordingData(){

        DistrictData = mDistrict.getText().toString();
        SuburbData = mSuburb.getText().toString();
        StreetData = mStreet.getText().toString();
        PosNameData = mPosName.getText().toString();
        ContactData = mContactPerson.getText().toString();
        PhoneData = mPhoneNumber.getText().toString();
        PosCodeData = mPosCode.getText().toString();
        LatData = String.valueOf(MLATITUDE);
        LonData = String.valueOf(MLONGITUDE);

        // Check for a valid Zone, if the user enter one.
        if(TextUtils.isEmpty(ZoneData)){
            Snackbar.make(mAddRecordBtn,getString(R.string.zone_error), Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Check for a valid Region, if the user enter one.
        if(TextUtils.isEmpty(RegionData)){
            Snackbar.make(mAddRecordBtn,getString(R.string.region_error), Snackbar.LENGTH_SHORT).show();
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
            Snackbar.make(mAddRecordBtn,getString(R.string.shop_error), Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Check for a valid Shop Type, if the user enter one.
        if(TextUtils.isEmpty(BeforeData)){
            Snackbar.make(mAddRecordBtn,getString(R.string.picture_error), Snackbar.LENGTH_SHORT).show();
            return;
        }

        if(mMaterials.size() > 0){
            for (int i = 0; i< mMaterials.size(); i++){
                MaterialTypeList materialTypeList = new MaterialTypeList();
                materialTypeList.setId(mMaterials.get(i).getId());
                materialTypeList.setType(mMaterials.get(i).getName());
                materialTypeList.setQuantity(mMaterials.get(i).getQuantity());
                materialTypeList.setStatus(mMaterials.get(i).getStatus());
                materialTypeLists.add(materialTypeList);
            }
        }

        //Save Data into Database
        realm.beginTransaction();
        progressDialog.show();
        Mrc_db mrcDb = new Mrc_db();
        ID = UUID.randomUUID().toString();

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
        mrcDb.setMaterialTypeLists(materialTypeLists);
        mrcDb.setMrc_before_img(BeforeData);
        mrcDb.setMrc_after_img(AfterImg);
        mrcDb.setMrc_lat(String.valueOf(MLATITUDE));
        mrcDb.setMrc_lon(String.valueOf(MLONGITUDE));
        mrcDb.setStatus(STATUS_NEEDS_UPDATE);
        //Insert Data into Database
        realm.insertOrUpdate(mrcDb);
        realm.commitTransaction();

        //Check if Data Is Insert
        Mrc_db mrc_db = realm.where(Mrc_db.class).equalTo("mrc_id",ID).findFirst();

        if(mrc_db != null){
            progressDialog.dismiss();
            //Clear Data
            mDistrict.setText("");
            mSuburb.setText("");
            mStreet.setText("");
            mPosName.setText("");
            mContactPerson.setText("");
            mPhoneNumber.setText("");
            mPosCode.setText("");

            AlertDialog alertDialog = new AlertDialog.Builder(AddRecords_ui.this).create();
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
        regionItems.clear();

        JSONObject data = new JSONObject(response);
        Log.d(TAG, data.toString());
        if(data.getInt("status_code") == 200){
            //Clear data from Database
            try {
                //Delete all from tables
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                    zoneDbRealmResults.deleteAllFromRealm();
                    regionDbRealmResults.deleteAllFromRealm();
                    shopTypeDbRealmResults.deleteAllFromRealm();
                    materialsDbRealmResults.deleteAllFromRealm();
                    Log.d("TAG","Deleted all available data");
                    }
                });

            }catch (Exception e){
                e.printStackTrace();
            }

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

                if(zoneItems.size() > 0){
                    //persist data to local database
                    Zone_db.persistToDatabase(zoneItems);
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

                if(shopItems.size() > 0){
                    //persist data to local database
                    ShopType_db.persistToDatabase(shopItems);
                }

            }

            //Getting Materials  Data
            if(!dataObj.isNull("material_type")){
                JSONArray materialTypeArray = dataObj.getJSONArray("material_type");
                int count_material = materialTypeArray.length();
                for(int i = 0; i<count_material; i++){
                    JSONObject materialObj = materialTypeArray.getJSONObject(i);
                    MaterialItem materialItem = new MaterialItem();
                    if(!materialObj.isNull("id"))
                        materialItem.setId(materialObj.getString("id"));
                    if(!materialObj.isNull("material_type"))
                        materialItem.setName(materialObj.getString("material_type"));
                    materialItems_removed.add(materialItem);
                    materialItems_installed.add(materialItem);
                }
                materialAdapter_in.notifyDataSetChanged();
                materialAdapter_.notifyDataSetChanged();

                if(materialItems_removed.size() > 0){
                    //persist data to local database
                    Materials_db.persistToDatabase(materialItems_removed);
                }
            }
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
        }
    }


    @Override
    public void onItemClick(View view , ArrayList<MaterialItem> materialItems) {
        mMaterials.addAll(materialItems) ;
        Log.d("Added", mMaterials.get(0).getStatus());

    }

    @Override
    public void onRemoveItemClick(View view, MaterialItem position) {
        try {
            if(mMaterials.size() != 0){
                int indexID = getMaterialPos(position);
                mMaterials.remove(indexID);
                for (int i = 0; i<mMaterials.size(); i++){
                    Log.d("Materials",mMaterials.get(i).getName());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onCurrentItems(View view , ArrayList<MaterialItem> materialItems) {
    }

    @Override
    public void onMaterialClick(View view, ArrayList<MaterialItem> materialItems) {
        mMaterials.addAll(materialItems);
    }

    @Override
    public void onMaterialRemoveClick(View view, MaterialItem position) {
        try{
            if(mMaterials.size() != 0){
                int indexID = getMaterialPos(position);
                mMaterials.remove(indexID);
                for (int i = 0; i<mMaterials.size(); i++){
                    Log.d("Materials",mMaterials.get(i).getName());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private int getMaterialPos(MaterialItem position) {
        return mMaterials.indexOf(position);
    }
}
