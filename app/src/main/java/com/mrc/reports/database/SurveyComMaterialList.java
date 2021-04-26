package com.mrc.reports.database;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SurveyComMaterialList extends RealmObject implements Parcelable {

    @PrimaryKey
    private String id;
    private String type;
    private String status;
    private String quantity;

    public SurveyComMaterialList(){}

    public SurveyComMaterialList(String id, String type ,String quantity, String status){
        this.id = id;
        this.type = type;
        this.quantity = quantity;
        this.status = status;
    }

    protected SurveyComMaterialList(Parcel in) {
        id = in.readString();
        type = in.readString();
        status = in.readString();
        quantity = in.readString();
    }

    public static final Creator<SurveyComMaterialList> CREATOR = new Creator<SurveyComMaterialList>() {
        @Override
        public SurveyComMaterialList createFromParcel(Parcel in) {
            return new SurveyComMaterialList(in);
        }

        @Override
        public SurveyComMaterialList[] newArray(int size) {
            return new SurveyComMaterialList[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(type);
        dest.writeString(status);
        dest.writeString(quantity);
    }
}
