package com.mrc.reports.database;

import com.mrc.reports.model.MrcItem;
import com.mrc.reports.model.SurveyItem;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Survey_db extends RealmObject {

    // Status
    // 1 = needs sync = orange
    // 2 = uploaded + sync = green

    @PrimaryKey
    private String survey_id;
    private String survey_zone_id;
    private String survey_region_id;
    private String survey_district_id;
    private String survey_suburb;
    private String survey_street;
    private String survey_pos_name;
    private String survey_contact_person;
    private String survey_phone_number;
    private String survey_pos_code;
    private String survey_shop_type_id;
    private String survey_lat;
    private String survey_lon;
    private String survey_img;
    private String survey_branding;
    private RealmList<SurveyCategoryList> surveyCategoryLists;
    private RealmList<SurveyComMaterialList> surveyComMaterialLists;
    private RealmList<SurveyCompetitorList> surveyCompetitorLists;
    private RealmList<SurveyMaterialList> surveyMaterialLists;
    private RealmList<SurveyServiceList> surveyServiceLists;
    private int status;

    public String getSurvey_id() {
        return survey_id;
    }

    public void setSurvey_id(String survey_id) {
        this.survey_id = survey_id;
    }

    public String getSurvey_zone_id() {
        return survey_zone_id;
    }

    public void setSurvey_zone_id(String survey_zone_id) {
        this.survey_zone_id = survey_zone_id;
    }

    public String getSurvey_region_id() {
        return survey_region_id;
    }

    public void setSurvey_region_id(String survey_region_id) {
        this.survey_region_id = survey_region_id;
    }

    public String getSurvey_district_id() {
        return survey_district_id;
    }

    public void setSurvey_district_id(String survey_district_id) {
        this.survey_district_id = survey_district_id;
    }

    public String getSurvey_suburb() {
        return survey_suburb;
    }

    public void setSurvey_suburb(String survey_suburb) {
        this.survey_suburb = survey_suburb;
    }

    public String getSurvey_street() {
        return survey_street;
    }

    public void setSurvey_street(String survey_street) {
        this.survey_street = survey_street;
    }

    public String getSurvey_pos_name() {
        return survey_pos_name;
    }

    public void setSurvey_pos_name(String survey_pos_name) {
        this.survey_pos_name = survey_pos_name;
    }

    public String getSurvey_contact_person() {
        return survey_contact_person;
    }

    public void setSurvey_contact_person(String survey_contact_person) {
        this.survey_contact_person = survey_contact_person;
    }

    public String getSurvey_phone_number() {
        return survey_phone_number;
    }

    public void setSurvey_phone_number(String survey_phone_number) {
        this.survey_phone_number = survey_phone_number;
    }

    public String getSurvey_pos_code() {
        return survey_pos_code;
    }

    public void setSurvey_pos_code(String survey_pos_code) {
        this.survey_pos_code = survey_pos_code;
    }

    public String getSurvey_shop_type_id() {
        return survey_shop_type_id;
    }

    public void setSurvey_shop_type_id(String survey_shop_type_id) {
        this.survey_shop_type_id = survey_shop_type_id;
    }

    public String getSurvey_lat() {
        return survey_lat;
    }

    public void setSurvey_lat(String survey_lat) {
        this.survey_lat = survey_lat;
    }

    public String getSurvey_lon() {
        return survey_lon;
    }

    public void setSurvey_lon(String survey_lon) {
        this.survey_lon = survey_lon;
    }

    public String getSurvey_img() {
        return survey_img;
    }

    public void setSurvey_img(String survey_img) {
        this.survey_img = survey_img;
    }

    public String getSurvey_branding() {
        return survey_branding;
    }

    public void setSurvey_branding(String survey_branding) {
        this.survey_branding = survey_branding;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Survey_db(){}

    public Survey_db(SurveyItem surveyItem){
        survey_id = surveyItem.getId();
        survey_zone_id = surveyItem.getZone_id();
        survey_region_id = surveyItem.getRegion_id();
        survey_district_id = surveyItem.getDistrict_id();
        survey_suburb = surveyItem.getSuburb();
        survey_street = surveyItem.getStreet();
        survey_pos_name = surveyItem.getPos_code();
        survey_contact_person = surveyItem.getContact_person();
        survey_phone_number = surveyItem.getPhone_number();
        survey_pos_code = surveyItem.getPos_code();
        survey_shop_type_id = surveyItem.getShop_type_id();
        survey_lat = surveyItem.getLat();
        survey_lon = surveyItem.getLon();
        survey_img = surveyItem.get_img();
        status = surveyItem.getStatus();
    }


    /**
     * Persist Survey_db to local database
     * @param surveyItems json array of LocationItems
     *
     */

    public static void persistToDatabase(List<SurveyItem> surveyItems){
        if(surveyItems == null) return;

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        int count= surveyItems.size();
        for(int index = 0; index< count; index++){
            Survey_db survey_db = new Survey_db(surveyItems.get(index));
            realm.insertOrUpdate(survey_db);
        }
        realm.commitTransaction();
    }
}
