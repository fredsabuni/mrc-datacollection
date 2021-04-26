package com.mrc.reports.database;

import com.mrc.reports.model.MrcItem;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Mrc_db extends RealmObject {

    // Status
    // 1 = needs update = orange
    // 2 = data has been updated not sync = purple
    // 3 = updated + sync = green

    @PrimaryKey
    private String mrc_id;
    private String mrc_zone_id;
    private String mrc_region_id;
    private String mrc_district_id;
    private String mrc_suburb;
    private String mrc_street;
    private String mrc_pos_name;
    private String mrc_contact_person;
    private String mrc_phone_number;
    private String mrc_pos_code;
    private String mrc_shop_type_id;
    private String mrc_lat;
    private String mrc_lon;
    private String mrc_before_img;
    private String mrc_after_img;
    private RealmList<MaterialTypeList> materialTypeLists;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMrc_before_img() {
        return mrc_before_img;
    }

    public void setMrc_before_img(String mrc_before_img) {
        this.mrc_before_img = mrc_before_img;
    }

    public String getMrc_after_img() {
        return mrc_after_img;
    }

    public void setMrc_after_img(String mrc_after_img) {
        this.mrc_after_img = mrc_after_img;
    }

    public RealmList<MaterialTypeList> getMaterialTypeLists() {
        return materialTypeLists;
    }

    public void setMaterialTypeLists(RealmList<MaterialTypeList> materialTypeLists) {
        this.materialTypeLists = materialTypeLists;
    }

    public String getMrc_region_id() {
        return mrc_region_id;
    }

    public void setMrc_region_id(String mrc_region_id) {
        this.mrc_region_id = mrc_region_id;
    }

    public Mrc_db(){}

    public String getMrc_id() {
        return mrc_id;
    }

    public void setMrc_id(String mrc_id) {
        this.mrc_id = mrc_id;
    }

    public String getMrc_zone_id() {
        return mrc_zone_id;
    }

    public void setMrc_zone_id(String mrc_zone_id) {
        this.mrc_zone_id = mrc_zone_id;
    }

    public String getMrc_district_id() {
        return mrc_district_id;
    }

    public void setMrc_district_id(String mrc_district_id) {
        this.mrc_district_id = mrc_district_id;
    }

    public String getMrc_suburb() {
        return mrc_suburb;
    }

    public void setMrc_suburb(String mrc_suburb) {
        this.mrc_suburb = mrc_suburb;
    }

    public String getMrc_street() {
        return mrc_street;
    }

    public void setMrc_street(String mrc_street) {
        this.mrc_street = mrc_street;
    }

    public String getMrc_pos_name() {
        return mrc_pos_name;
    }

    public void setMrc_pos_name(String mrc_pos_name) {
        this.mrc_pos_name = mrc_pos_name;
    }

    public String getMrc_contact_person() {
        return mrc_contact_person;
    }

    public void setMrc_contact_person(String mrc_contact_person) {
        this.mrc_contact_person = mrc_contact_person;
    }

    public String getMrc_phone_number() {
        return mrc_phone_number;
    }

    public void setMrc_phone_number(String mrc_phone_number) {
        this.mrc_phone_number = mrc_phone_number;
    }

    public String getMrc_pos_code() {
        return mrc_pos_code;
    }

    public void setMrc_pos_code(String mrc_pos_code) {
        this.mrc_pos_code = mrc_pos_code;
    }

    public String getMrc_shop_type_id() {
        return mrc_shop_type_id;
    }

    public void setMrc_shop_type_id(String mrc_shop_type_id) {
        this.mrc_shop_type_id = mrc_shop_type_id;
    }

    public String getMrc_lat() {
        return mrc_lat;
    }

    public void setMrc_lat(String mrc_lat) {
        this.mrc_lat = mrc_lat;
    }

    public String getMrc_lon() {
        return mrc_lon;
    }

    public void setMrc_lon(String mrc_lon) {
        this.mrc_lon = mrc_lon;
    }


    public Mrc_db(MrcItem mrcItem){
        mrc_id = mrcItem.getId();
        mrc_zone_id = mrcItem.getZone_id();
        mrc_region_id = mrcItem.getRegion_id();
        mrc_district_id = mrcItem.getDistrict_id();
        mrc_suburb = mrcItem.getSuburb();
        mrc_street = mrcItem.getStreet();
        mrc_pos_code = mrcItem.getPos_code();
        mrc_contact_person = mrcItem.getContact_person();
        mrc_phone_number = mrcItem.getPhone_number();
        mrc_pos_code = mrcItem.getPos_code();
        mrc_shop_type_id = mrcItem.getShop_type_id();
        mrc_lat = mrcItem.getLat();
        mrc_lon = mrcItem.getLon();
        mrc_before_img = mrcItem.getBefore_img();
        mrc_after_img = mrcItem.getAfter_img();
        status = mrcItem.getStatus();
    }

    /**
     * Persist Mrc_db to local database
     * @param mrcItems json array of LocationItems
     *
     */

    public static void persistToDatabase(List<MrcItem> mrcItems){
        if(mrcItems == null) return;

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        int count= mrcItems.size();
        for(int index = 0; index< count; index++){
            Mrc_db mrcDb = new Mrc_db(mrcItems.get(index));
            realm.insertOrUpdate(mrcDb);
        }
        realm.commitTransaction();
    }


//    public static void updateTuneStatus(final long tuneId, final int status){
//        Realm realm = Realm.getDefaultInstance();
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                UserTune tune = realm.where(UserTune.class).equalTo(FIELD_TUNE_ID, tuneId).findFirst();
//                assert tune != null;
//                tune.status = status;
//            }
//        });
//    }


//    public static void deleteTune(final long tuneId){
//        Realm realm = Realm.getDefaultInstance();
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                DeviceTune tune = realm.where(DeviceTune.class).equalTo(FIELD_ID, tuneId).findFirst();
//                assert tune != null;
//                tune.deleteFromRealm();
//            }
//        });
//    }

}
