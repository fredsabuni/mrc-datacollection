package com.mrc.reports.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.mrc.reports.BaseActivity;
import com.mrc.reports.R;
import com.mrc.reports.adapter.CategoryAdapter;
import com.mrc.reports.adapter.CompetitorAdapter;
import com.mrc.reports.adapter.CompetitorMaterialAdapter;
import com.mrc.reports.adapter.MaterialAdapter;
import com.mrc.reports.adapter.RegionAdapter;
import com.mrc.reports.adapter.ServicesAdapter;
import com.mrc.reports.adapter.ShopAdapter;
import com.mrc.reports.adapter.ZoneAdapter;
import com.mrc.reports.database.Category_db;
import com.mrc.reports.database.Competitor_db;
import com.mrc.reports.database.Materials_db;
import com.mrc.reports.database.Region_db;
import com.mrc.reports.database.Service_db;
import com.mrc.reports.database.ShopType_db;
import com.mrc.reports.database.SurveyCategoryList;
import com.mrc.reports.database.SurveyComMaterialList;
import com.mrc.reports.database.SurveyCompetitorList;
import com.mrc.reports.database.SurveyMaterialList;
import com.mrc.reports.database.SurveyServiceList;
import com.mrc.reports.database.Survey_db;
import com.mrc.reports.database.Zone_db;
import com.mrc.reports.model.CategoryItem;
import com.mrc.reports.model.CompetitorItem;
import com.mrc.reports.model.MaterialItem;
import com.mrc.reports.model.RegionItem;
import com.mrc.reports.model.ServiceItem;
import com.mrc.reports.model.ShopItem;
import com.mrc.reports.model.ZoneItem;
import com.mrc.reports.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class AddSurvey_ui extends BaseActivity implements CategoryAdapter.CategoryClickListener, CompetitorAdapter.CompetitorClickListener, CompetitorMaterialAdapter.ComMaterialClickListener, ServicesAdapter.ItemServiceClickListener,  MaterialAdapter.MaterialClickListener {

    TextInputEditText mDistrict;
    TextInputEditText mSuburb;
    TextInputEditText mStreet;
    TextInputEditText mPosName;
    TextInputEditText mContactPerson;
    TextInputEditText mPhoneNumber;
    TextInputEditText mPosCode;
    TextView mMaterialInstalledTitle;
    Button mAddRecordBtn;
    ImageView mImg;
    FloatingActionButton mImgBtn;
    Spinner mZoneSpinner;
    Spinner mRegionSpinner;
    Spinner mShopSpinner;
    RadioButton mYesBtn;
    RadioButton mNoBtn;
    RecyclerView mInstalledList;
    RecyclerView mServiceList;
    RecyclerView mCompetitorList;
    RecyclerView mCompetitorMaterialList;
    RecyclerView mCategoryList;

    ArrayList<MaterialItem> materialItems_installed = new ArrayList<>();
    ArrayList<MaterialItem> materialItems_competitor = new ArrayList<>();
    ArrayList<ZoneItem> zoneItems = new ArrayList<>();
    ArrayList<RegionItem> regionItems = new ArrayList<>();
    ArrayList<ShopItem> shopItems = new ArrayList<>();
    ArrayList<ServiceItem> serviceItems = new ArrayList<>();
    ArrayList<CompetitorItem> competitorItems = new ArrayList<>();
    ArrayList<CategoryItem> categoryItems = new ArrayList<>();

    ArrayList<ServiceItem> surveyServiceItems = new ArrayList<>();
    ArrayList<CompetitorItem> surveyCompetitorItems = new ArrayList<>();
    ArrayList<CategoryItem> surveyCategoryItems = new ArrayList<>();
    ArrayList<MaterialItem> surveyMaterialItems = new ArrayList<>();
    ArrayList<MaterialItem> surveyCompetitorMaterialItems  = new ArrayList<>();

    ZoneAdapter zoneAdapter;
    RegionAdapter regionAdapter;
    ShopAdapter shopAdapter;
    MaterialAdapter materialAdapter;
    CompetitorMaterialAdapter competitorMaterialAdapter;
    CompetitorAdapter competitorAdapter;
    ServicesAdapter servicesAdapter;
    CategoryAdapter categoryAdapter;

    public final int REQ_CODE_IMG = 101;
    private RealmResults<Zone_db> zoneDbRealmResults;
    private RealmResults<Region_db> regionDbRealmResults;
    private RealmResults<ShopType_db> shopTypeDbRealmResults;
    private RealmResults<Materials_db> materialsDbRealmResults;
    private RealmResults<Competitor_db> competitorDbRealmResults;
    private RealmResults<Service_db> serviceDbRealmResults;
    private RealmResults<Category_db> categoryDbRealmResults;

    RealmList<SurveyCategoryList> surveyCategoryLists = new RealmList<>();
    RealmList<SurveyComMaterialList> surveyComMaterialLists = new RealmList<>();
    RealmList<SurveyCompetitorList> surveyCompetitorLists = new RealmList<>();
    RealmList<SurveyMaterialList> surveyMaterialLists = new RealmList<>();
    RealmList<SurveyServiceList> surveyServiceLists = new RealmList<>();

    public final int STATUS_NEEDS_SYNC = 1;

    Realm realm;

    String ID,ZoneData, RegionData, DistrictData, SuburbData, StreetData, PosNameData, ContactData, PhoneData, PosCodeData, ShopData, ImgData, LatData, LonData, brandingData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_survey_ui);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        mInstalledList= findViewById(R.id.material_list);
        mServiceList = findViewById(R.id.service_list);
        mCompetitorList = findViewById(R.id.competitor_list);
        mCompetitorMaterialList = findViewById(R.id.competitor_material_list);
        mNoBtn = findViewById(R.id.radio_no);
        mYesBtn = findViewById(R.id.radio_yes);
        mImg = findViewById(R.id.pos_img);
        mImgBtn = findViewById(R.id.before_btn);
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
        mCategoryList = findViewById(R.id.categories_list);
        mMaterialInstalledTitle = findViewById(R.id.material_installed_title);

        //get Data from Server
        getServerData();

        if(materialItems_installed.size() == 0){
            try{
                //Query the Database
                materialsDbRealmResults = realm.where(Materials_db.class).findAll();
                if(materialsDbRealmResults != null && materialsDbRealmResults.size() > 0){
                    for(Materials_db materials_db: materialsDbRealmResults){
                        materialItems_competitor.add(new MaterialItem(materials_db));
                        materialItems_installed.add(new MaterialItem(materials_db));
                    }
                    materialAdapter.notifyDataSetChanged();
                    competitorAdapter.notifyDataSetChanged();

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        //Setting Material Adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mInstalledList.setLayoutManager(linearLayoutManager);
        materialAdapter = new MaterialAdapter(this, materialItems_installed);
        mInstalledList.setAdapter(materialAdapter);
        materialAdapter.setMaterialClickListener(this);


        //Setting Competitor Material Adapter
        LinearLayoutManager linearManager = new LinearLayoutManager(this);
        mCompetitorMaterialList.setLayoutManager(linearManager);
        competitorMaterialAdapter = new CompetitorMaterialAdapter(this, materialItems_competitor);
        mCompetitorMaterialList.setAdapter(competitorMaterialAdapter);
        competitorMaterialAdapter.setMaterialClickListener(this);

        if(competitorItems.size() == 0){
            try{
                //Query the Database
                competitorDbRealmResults = realm.where(Competitor_db.class).findAll();
                if(competitorDbRealmResults != null && competitorDbRealmResults.size() > 0){
                    for(Competitor_db competitor_db: competitorDbRealmResults){
                        competitorItems.add(new CompetitorItem(competitor_db));
                    }
                    competitorAdapter.notifyDataSetChanged();

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        //Setting Competitor Material Adapter
        LinearLayoutManager linearCompetitorManager = new LinearLayoutManager(this);
        mCompetitorList.setLayoutManager(linearCompetitorManager);
        competitorAdapter = new CompetitorAdapter(this, competitorItems);
        mCompetitorList.setAdapter(competitorAdapter);
        competitorAdapter.setItemClickListener(this);

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

        if(serviceItems.size() == 0){
            try{
                //Query the Database
                serviceDbRealmResults = realm.where(Service_db.class).findAll();
                if(serviceDbRealmResults != null && serviceDbRealmResults.size() > 0){
                    for(Service_db service_db: serviceDbRealmResults){
                        serviceItems.add(new ServiceItem(service_db));
                    }
                    servicesAdapter.notifyDataSetChanged();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        //Setting Service Material Adapter
        LinearLayoutManager linearServiceManager = new LinearLayoutManager(this);
        mServiceList.setLayoutManager(linearServiceManager);
        servicesAdapter = new ServicesAdapter(this, serviceItems);
        mServiceList.setAdapter(servicesAdapter);
        servicesAdapter.setItemClickListener(this);

        if(categoryItems.size() == 0){
            try{
                //Query the Database
                categoryDbRealmResults = realm.where(Category_db.class).findAll();
                if(categoryDbRealmResults != null && categoryDbRealmResults.size() > 0){
                    for(Category_db category_db: categoryDbRealmResults){
                        categoryItems.add(new CategoryItem(category_db));
                    }
                    categoryAdapter.notifyDataSetChanged();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        //Setting Category Material Adapter
        LinearLayoutManager linearCategoryManager = new LinearLayoutManager(this);
        mCategoryList.setLayoutManager(linearCategoryManager);
        categoryAdapter = new CategoryAdapter(this, categoryItems);
        mCategoryList.setAdapter(categoryAdapter);
        categoryAdapter.setItemClickListener(this);


        mImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Take a picture after Installation
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQ_CODE_IMG);
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
                    Snackbar.make(mImg, R.string.server_reachable_error, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String message = t.getMessage();
                Log.d("failure", message);
                Snackbar.make(mImg,R.string.wrong_error,Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void parseData(String response) throws JSONException {

        shopItems.clear();
        zoneItems.clear();
        materialItems_competitor.clear();
        competitorItems.clear();
        materialItems_installed.clear();
        regionItems.clear();
        serviceItems.clear();


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
                    materialItems_competitor.add(materialItem);
                    materialItems_installed.add(materialItem);
                }
                competitorAdapter.notifyDataSetChanged();
                materialAdapter.notifyDataSetChanged();

                if(materialItems_installed.size() > 0){
                    //persist data to local database
                    Materials_db.persistToDatabase(materialItems_installed);
                }
            }

            //Getting Services  Data
            if(!dataObj.isNull("service_offered")){
                JSONArray servicesArray = dataObj.getJSONArray("service_offered");
                int count_services = servicesArray.length();
                for(int i = 0; i<count_services; i++){
                    JSONObject serviceObj = servicesArray.getJSONObject(i);
                    ServiceItem serviceItem = new ServiceItem();
                    if(!serviceObj.isNull("id"))
                        serviceItem.setId(serviceObj.getString("id"));
                    if(!serviceObj.isNull("service_name"))
                        serviceItem.setName(serviceObj.getString("service_name"));
                    serviceItems.add(serviceItem);
                }
                servicesAdapter.notifyDataSetChanged();

                if(serviceItems.size() > 0){
                    //persist data to local database
                    Service_db.persistToDatabase(serviceItems);
                }
            }

            //Getting Competitors  Data
            if(!dataObj.isNull("competitors")){
                JSONArray competitorsArray = dataObj.getJSONArray("competitors");
                int count_competitors = competitorsArray.length();
                for(int i = 0; i<count_competitors; i++){
                    JSONObject competitorObj = competitorsArray.getJSONObject(i);
                    CompetitorItem competitorItem = new CompetitorItem();
                    if(!competitorObj.isNull("id"))
                        competitorItem.setId(competitorObj.getString("id"));
                    if(!competitorObj.isNull("competitor_name"))
                        competitorItem.setName(competitorObj.getString("competitor_name"));
                    competitorItems.add(competitorItem);
                }
                competitorAdapter.notifyDataSetChanged();

                if(competitorItems.size() > 0){
                    //persist data to local database
                    Competitor_db.persistToDatabase(competitorItems);
                }
            }
        }

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
        // Check for a valid Shop Type, if the user enter one.
        if(TextUtils.isEmpty(ShopData)){
            Snackbar.make(mAddRecordBtn,getString(R.string.shop_error), Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Check for a valid Shop Type, if the user enter one.
        if(TextUtils.isEmpty(ImgData)){
            Snackbar.make(mAddRecordBtn,getString(R.string.picture_error), Snackbar.LENGTH_SHORT).show();
            return;
        }

        if(surveyServiceItems.size() > 0){
            for (int i = 0; i< surveyServiceItems.size(); i++){
                SurveyServiceList surveyServiceList = new SurveyServiceList();
                surveyServiceList.setId(surveyServiceItems.get(i).getId());
                surveyServiceList.setName(surveyServiceItems.get(i).getName());
                surveyServiceLists.add(surveyServiceList);
            }
        }

        if(surveyCategoryItems.size() > 0){
            for (int i = 0; i< surveyCategoryItems.size(); i++){
                SurveyCategoryList surveyCategoryList = new SurveyCategoryList();
                surveyCategoryList.setId(surveyCategoryItems.get(i).getId());
                surveyCategoryList.setName(surveyCategoryItems.get(i).getName());
                surveyCategoryLists.add(surveyCategoryList);
            }
        }

        if(surveyMaterialItems.size() > 0){
            for (int i = 0; i< surveyMaterialItems.size(); i++){
                SurveyMaterialList surveyMaterialList = new SurveyMaterialList();
                surveyMaterialList.setId(surveyMaterialItems.get(i).getId());
                surveyMaterialList.setType(surveyMaterialItems.get(i).getName());
                surveyMaterialLists.add(surveyMaterialList);
            }
        }

        if(surveyCompetitorItems.size() > 0){
            for (int i = 0; i< surveyCompetitorItems.size(); i++){
                SurveyCompetitorList surveyCompetitorList = new SurveyCompetitorList();
                surveyCompetitorList.setId(surveyCompetitorItems.get(i).getId());
                surveyCompetitorList.setName(surveyCompetitorItems.get(i).getName());
                surveyCompetitorLists.add(surveyCompetitorList);
            }
        }

        if(surveyCompetitorMaterialItems.size() > 0){
            for (int i = 0; i< surveyCompetitorMaterialItems.size(); i++){
                SurveyComMaterialList surveyComMaterialList = new SurveyComMaterialList();
                surveyComMaterialList.setId(surveyCompetitorMaterialItems.get(i).getId());
                surveyComMaterialList.setType(surveyCompetitorMaterialItems.get(i).getName());
                surveyComMaterialLists.add(surveyComMaterialList);
            }
        }

        //Save Data into Database
        realm.beginTransaction();
        progressDialog.show();
        Survey_db survey_db = new Survey_db();
        ID = UUID.randomUUID().toString();

        survey_db.setSurvey_id(ID);
        survey_db.setSurvey_zone_id(ZoneData);
        survey_db.setSurvey_region_id(RegionData);
        survey_db.setSurvey_district_id(DistrictData);
        survey_db.setSurvey_suburb(SuburbData);
        survey_db.setSurvey_street(StreetData);
        survey_db.setSurvey_pos_name(PosNameData);
        survey_db.setSurvey_contact_person(ContactData);
        survey_db.setSurvey_phone_number(PhoneData);
        survey_db.setSurvey_pos_code(PosCodeData);
        survey_db.setSurvey_shop_type_id(ShopData);
        survey_db.setSurveyCategoryLists(surveyCategoryLists);
        survey_db.setSurveyComMaterialLists(surveyComMaterialLists);
        survey_db.setSurveyCompetitorLists(surveyCompetitorLists);
        survey_db.setSurveyServiceLists(surveyServiceLists);
        survey_db.setSurveyMaterialLists(surveyMaterialLists);
        survey_db.setSurvey_lat(String.valueOf(MLATITUDE));
        survey_db.setSurvey_lon(String.valueOf(MLONGITUDE));
        survey_db.setSurvey_branding(brandingData);
        survey_db.setSurvey_img(ImgData);
        survey_db.setStatus(STATUS_NEEDS_SYNC);
        //Insert Data into Database
        realm.insertOrUpdate(survey_db);
        realm.commitTransaction();

        //Check if Data Is Insert
        Survey_db surveyDb = realm.where(Survey_db.class).equalTo("survey_id",ID).findFirst();

        if(surveyDb != null){
            progressDialog.dismiss();
            //Clear Data
            mDistrict.setText("");
            mSuburb.setText("");
            mStreet.setText("");
            mPosName.setText("");
            mContactPerson.setText("");
            mPhoneNumber.setText("");
            mPosCode.setText("");

            AlertDialog alertDialog = new AlertDialog.Builder(AddSurvey_ui.this).create();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_IMG && resultCode == Activity.RESULT_OK && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mImg.setImageBitmap(photo);
            ImgData = Utils.bitmapToBase64(photo);
        }
    }

    public void onRadioBrandingBtn(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_yes:
                if (checked)
                    mMaterialInstalledTitle.setVisibility(View.VISIBLE);
                    mInstalledList.setVisibility(View.VISIBLE);
                    brandingData = "yes";
                    break;
            case R.id.radio_no:
                if (checked)
                    mMaterialInstalledTitle.setVisibility(View.GONE);
                    mInstalledList.setVisibility(View.GONE);
                brandingData = "no";
                    break;
        }
    }

    @Override
    public void onCompetitorItemClick(View view, ArrayList<CompetitorItem> competitorItems) {
        surveyCompetitorItems.addAll(competitorItems);
        Log.d("Added", surveyCompetitorItems.get(0).getName());
    }

    @Override
    public void onCompetitorRemoveItemClick(View view, CompetitorItem position) {
        try {
            if(surveyCompetitorItems.size() != 0){
                int indexID = getCompetitor(position) + 1;
                surveyCompetitorItems.remove(indexID);
                for (int i = 0; i<surveyCompetitorItems.size(); i++){
                    Log.d("Competitors",surveyCompetitorItems.get(i).getName());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private int getCompetitor(CompetitorItem position) {
        return surveyCompetitorItems.indexOf(position);
    }

    @Override
    public void onComMaterialClick(View view, ArrayList<MaterialItem> materialItems) {
        surveyCompetitorMaterialItems.addAll(materialItems);
        Log.d("Added", surveyCompetitorMaterialItems.get(0).getName());
    }

    @Override
    public void onComMaterialRemoveClick(View view, MaterialItem position) {
        try {
            if(surveyCompetitorMaterialItems.size() != 0){
                int indexID = getCompetitorMaterials(position) + 1;
                surveyCompetitorMaterialItems.remove(indexID);
                for (int i = 0; i<surveyCompetitorMaterialItems.size(); i++){
                    Log.d("Competitors Materials",surveyCompetitorMaterialItems.get(i).getName());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private int getCompetitorMaterials(MaterialItem position) {
        return surveyCompetitorMaterialItems.indexOf(position);
    }

    @Override
    public void onMaterialClick(View view, ArrayList<MaterialItem> materialItems) {
        surveyMaterialItems.addAll(materialItems);
        Log.d("Added", materialItems.get(0).getName());
    }

    @Override
    public void onMaterialRemoveClick(View view, MaterialItem position) {
        try {
            if(surveyMaterialItems.size() != 0){
                int indexID = getMaterials(position) + 1;
                surveyMaterialItems.remove(indexID);
                for (int i = 0; i<surveyMaterialItems.size(); i++){
                    Log.d("Materials Installed",surveyMaterialItems.get(i).getName());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int getMaterials(MaterialItem position){
        return surveyMaterialItems.indexOf(position);
    }

    @Override
    public void onServiceItemClick(View view, ArrayList<ServiceItem> Items) {
        surveyServiceItems.addAll(Items);
        Log.d("Added", surveyServiceItems.get(0).getName());
    }

    @Override
    public void onServiceRemoveItemClick(View view, ServiceItem position) {
        try {
            if(surveyServiceItems.size() != 0){
                int indexID = getService(position) + 1;
                Log.d("Services-indexID",String.valueOf(getService(position)));
                surveyServiceItems.remove(indexID);
                for (int i = 0; i<surveyServiceItems.size(); i++){
                    Log.d("Services",surveyServiceItems.get(i).getName());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private int getService(ServiceItem position) {
        return surveyServiceItems.indexOf(position);
    }

    @Override
    public void onCategoryItemClick(View view, ArrayList<CategoryItem> Items) {
        surveyCategoryItems.addAll(Items);
        Log.d("Added", surveyCategoryItems.get(0).getName());
    }

    @Override
    public void onCategoryRemoveItemClick(View view, CategoryItem position) {
        try {
            if(surveyCategoryItems.size() != 0){
                int indexID = getCategory(position) + 1;
                surveyCategoryItems.remove(indexID);
                for (int i = 0; i<surveyCategoryItems.size(); i++){
                    Log.d("Category",surveyCategoryItems.get(i).getName());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int getCategory(CategoryItem position){
        return  surveyCategoryItems.indexOf(position);
    }
}