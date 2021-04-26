package com.mrc.reports.ui;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;
import com.mrc.reports.R;
import com.mrc.reports.adapter.MaterialAdapter;
import com.mrc.reports.adapter.RegionAdapter;
import com.mrc.reports.adapter.ShopAdapter;
import com.mrc.reports.adapter.ZoneAdapter;
import com.mrc.reports.model.CompetitorItem;
import com.mrc.reports.model.MaterialItem;
import com.mrc.reports.model.RegionItem;
import com.mrc.reports.model.ServiceItem;
import com.mrc.reports.model.ShopItem;
import com.mrc.reports.model.ZoneItem;

import java.util.ArrayList;

public class Survey_ui extends AppCompatActivity {

    TextInputEditText mDistrict;
    TextInputEditText mSuburb;
    TextInputEditText mStreet;
    TextInputEditText mPosName;
    TextInputEditText mContactPerson;
    TextInputEditText mPhoneNumber;
    TextInputEditText mPosCode;
    Button mAddRecordBtn;
    ImageView mImg;
    Spinner mZoneSpinner;
    Spinner mRegionSpinner;
    Spinner mShopSpinner;
    RecyclerView mInstalledList;
    RecyclerView mServiceList;
    RecyclerView mCompetitorList;
    RecyclerView mCompetitorMaterialList;
    FloatingActionButton mBtn;

    ArrayList<MaterialItem> materialItems_installed = new ArrayList<>();
    ArrayList<MaterialItem> materialItems_competitor = new ArrayList<>();
    ArrayList<ZoneItem> zoneItems = new ArrayList<>();
    ArrayList<RegionItem> regionItems = new ArrayList<>();
    ArrayList<ShopItem> shopItems = new ArrayList<>();
    ArrayList<ServiceItem> serviceItems = new ArrayList<>();
    ArrayList<CompetitorItem> competitorItems = new ArrayList<>();

    ZoneAdapter zoneAdapter;
    RegionAdapter regionAdapter;
    ShopAdapter shopAdapter;
    MaterialAdapter materialAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_ui);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    public void onRadioBrandingBtn(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_yes:
                if (checked)
                    break;
            case R.id.radio_no:
                if (checked)
                    break;
        }
    }
}