package com.mrc.reports.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.mrc.reports.database.MaterialTypeList;
import com.mrc.reports.database.Mrc_db;

import java.util.ArrayList;

import io.realm.RealmList;

public class MrcItem implements Parcelable {
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
    String material_removed;
    String material_installed;
    String before_img;
    String after_img;
    int status;
    RealmList<MaterialTypeList> materialTypeLists;

    public MrcItem(Mrc_db mrc_db){
        this.id = mrc_db.getMrc_id();
        this.zone_id = mrc_db.getMrc_zone_id();
        this.region_id = mrc_db.getMrc_region_id();
        this.district_id = mrc_db.getMrc_district_id();
        this.suburb = mrc_db.getMrc_suburb();
        this.street = mrc_db.getMrc_street();
        this.pos_name = mrc_db.getMrc_pos_name();
        this.contact_person = mrc_db.getMrc_contact_person();
        this.phone_number = mrc_db.getMrc_phone_number();
        this.pos_code = mrc_db.getMrc_pos_code();
        this.shop_type_id = mrc_db.getMrc_shop_type_id();
        this.lat = mrc_db.getMrc_lat();
        this.lon = mrc_db.getMrc_lon();
        this.before_img = mrc_db.getMrc_before_img();
        this.after_img = mrc_db.getMrc_after_img();
        this.status = mrc_db.getStatus();
        this.materialTypeLists = mrc_db.getMaterialTypeLists();
    }

    protected MrcItem(Parcel in) {
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
        material_removed = in.readString();
        material_installed = in.readString();
        before_img = in.readString();
        after_img = in.readString();
        status = in.readInt();

        materialTypeLists = new RealmList<>();
        materialTypeLists.addAll(in.createTypedArrayList(MaterialTypeList.CREATOR));
    }

    public static final Creator<MrcItem> CREATOR = new Creator<MrcItem>() {
        @Override
        public MrcItem createFromParcel(Parcel in) {
            return new MrcItem(in);
        }

        @Override
        public MrcItem[] newArray(int size) {
            return new MrcItem[size];
        }
    };

    public RealmList<MaterialTypeList> getMaterialTypeLists() {
        return materialTypeLists;
    }

    public MrcItem setMaterialTypeLists(ArrayList<MaterialTypeList> materialTypeLists) {
        this.materialTypeLists = new RealmList<>();
        return this;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public MrcItem(){}

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

    public String getMaterial_removed() {
        return material_removed;
    }

    public void setMaterial_removed(String material_removed) {
        this.material_removed = material_removed;
    }

    public String getMaterial_installed() {
        return material_installed;
    }

    public void setMaterial_installed(String material_installed) {
        this.material_installed = material_installed;
    }

    public String getBefore_img() {
        return before_img;
    }

    public void setBefore_img(String before_img) {
        this.before_img = before_img;
    }

    public String getAfter_img() {
        return after_img;
    }

    public void setAfter_img(String after_img) {
        this.after_img = after_img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(zone_id);
        parcel.writeString(region_id);
        parcel.writeString(district_id);
        parcel.writeString(suburb);
        parcel.writeString(street);
        parcel.writeString(pos_name);
        parcel.writeString(contact_person);
        parcel.writeString(phone_number);
        parcel.writeString(pos_code);
        parcel.writeString(shop_type_id);
        parcel.writeString(lat);
        parcel.writeString(lon);
        parcel.writeString(material_removed);
        parcel.writeString(material_installed);
        parcel.writeString(before_img);
        parcel.writeString(after_img);
        parcel.writeInt(status);
    }
}
