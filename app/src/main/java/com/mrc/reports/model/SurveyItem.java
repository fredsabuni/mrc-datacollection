package com.mrc.reports.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.mrc.reports.database.SurveyCategoryList;
import com.mrc.reports.database.SurveyComMaterialList;
import com.mrc.reports.database.SurveyCompetitorList;
import com.mrc.reports.database.SurveyMaterialList;
import com.mrc.reports.database.SurveyServiceList;
import com.mrc.reports.database.Survey_db;

import io.realm.RealmList;

public class SurveyItem implements Parcelable {
    String id;
    String zone_id;
    String region_id;
    String district_id;
    String suburb;
    String street;
    String pos_name;
    String contact_person;
    String phone_number;
    String pos_code;
    String shop_type_id;
    String lat;
    String lon;
    String _img;
    String branding;
    int status;
    private RealmList<SurveyCategoryList> surveyCategoryLists;
    private RealmList<SurveyComMaterialList> surveyComMaterialLists;
    private RealmList<SurveyCompetitorList> surveyCompetitorLists;
    private RealmList<SurveyMaterialList> surveyMaterialLists;
    private RealmList<SurveyServiceList> surveyServiceLists;

    public SurveyItem(Survey_db survey_db){
        this.id = survey_db.getSurvey_id();
        this.zone_id = survey_db.getSurvey_zone_id();
        this.region_id = survey_db.getSurvey_region_id();
        this.district_id = survey_db.getSurvey_district_id();
        this.suburb = survey_db.getSurvey_suburb();
        this.street = survey_db.getSurvey_street();
        this.pos_name = survey_db.getSurvey_pos_name();
        this.contact_person = survey_db.getSurvey_contact_person();
        this.phone_number = survey_db.getSurvey_phone_number();
        this.pos_code = survey_db.getSurvey_pos_code();
        this.shop_type_id = survey_db.getSurvey_shop_type_id();
        this.lat = survey_db.getSurvey_lat();
        this.lon = survey_db.getSurvey_lon();
        this._img = survey_db.getSurvey_img();
        this.status = survey_db.getStatus();
        this.surveyCategoryLists = survey_db.getSurveyCategoryLists();
        this.surveyComMaterialLists = survey_db.getSurveyComMaterialLists();
        this.surveyCompetitorLists = survey_db.getSurveyCompetitorLists();
        this.surveyMaterialLists = survey_db.getSurveyMaterialLists();
        this.surveyServiceLists = survey_db.getSurveyServiceLists();
    }

    protected SurveyItem(Parcel in) {
        id = in.readString();
        zone_id = in.readString();
        region_id = in.readString();
        district_id = in.readString();
        suburb = in.readString();
        street = in.readString();
        pos_name = in.readString();
        contact_person = in.readString();
        phone_number = in.readString();
        pos_code = in.readString();
        shop_type_id = in.readString();
        lat = in.readString();
        lon = in.readString();
        _img = in.readString();
        branding = in.readString();
        status = in.readInt();
    }

    public static final Creator<SurveyItem> CREATOR = new Creator<SurveyItem>() {
        @Override
        public SurveyItem createFromParcel(Parcel in) {
            return new SurveyItem(in);
        }

        @Override
        public SurveyItem[] newArray(int size) {
            return new SurveyItem[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZone_id() {
        return zone_id;
    }

    public void setZone_id(String zone_id) {
        this.zone_id = zone_id;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPos_name() {
        return pos_name;
    }

    public void setPos_name(String pos_name) {
        this.pos_name = pos_name;
    }

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPos_code() {
        return pos_code;
    }

    public void setPos_code(String pos_code) {
        this.pos_code = pos_code;
    }

    public String getShop_type_id() {
        return shop_type_id;
    }

    public void setShop_type_id(String shop_type_id) {
        this.shop_type_id = shop_type_id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String get_img() {
        return _img;
    }

    public void set_img(String _img) {
        this._img = _img;
    }

    public String getBranding() {
        return branding;
    }

    public void setBranding(String branding) {
        this.branding = branding;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public RealmList<SurveyCategoryList> getSurveyCategoryLists() {
        return surveyCategoryLists;
    }

    public void setSurveyCategoryLists(RealmList<SurveyCategoryList> surveyCategoryLists) {
        this.surveyCategoryLists = surveyCategoryLists;
    }

    public RealmList<SurveyComMaterialList> getSurveyComMaterialLists() {
        return surveyComMaterialLists;
    }

    public void setSurveyComMaterialLists(RealmList<SurveyComMaterialList> surveyComMaterialLists) {
        this.surveyComMaterialLists = surveyComMaterialLists;
    }

    public RealmList<SurveyCompetitorList> getSurveyCompetitorLists() {
        return surveyCompetitorLists;
    }

    public void setSurveyCompetitorLists(RealmList<SurveyCompetitorList> surveyCompetitorLists) {
        this.surveyCompetitorLists = surveyCompetitorLists;
    }

    public RealmList<SurveyMaterialList> getSurveyMaterialLists() {
        return surveyMaterialLists;
    }

    public void setSurveyMaterialLists(RealmList<SurveyMaterialList> surveyMaterialLists) {
        this.surveyMaterialLists = surveyMaterialLists;
    }

    public RealmList<SurveyServiceList> getSurveyServiceLists() {
        return surveyServiceLists;
    }

    public void setSurveyServiceLists(RealmList<SurveyServiceList> surveyServiceLists) {
        this.surveyServiceLists = surveyServiceLists;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(zone_id);
        dest.writeString(region_id);
        dest.writeString(district_id);
        dest.writeString(suburb);
        dest.writeString(street);
        dest.writeString(pos_name);
        dest.writeString(contact_person);
        dest.writeString(phone_number);
        dest.writeString(pos_code);
        dest.writeString(shop_type_id);
        dest.writeString(lat);
        dest.writeString(lon);
        dest.writeString(_img);
        dest.writeString(branding);
        dest.writeInt(status);
    }
}
