package com.mrc.reports.database;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MaterialTypeList extends RealmObject implements Parcelable {
    @PrimaryKey
    private String id;
    private String type;
    private String status;
    private String quantity;

    public MaterialTypeList(){}

    public MaterialTypeList(String id, String type ,String quantity, String status){
        this.id = id;
        this.type = type;
        this.quantity = quantity;
        this.status = status;
    }


    protected MaterialTypeList(Parcel in) {
        id = in.readString();
        type = in.readString();
        status = in.readString();
        quantity = in.readString();
    }

    public static final Creator<MaterialTypeList> CREATOR = new Creator<MaterialTypeList>() {
        @Override
        public MaterialTypeList createFromParcel(Parcel in) {
            return new MaterialTypeList(in);
        }

        @Override
        public MaterialTypeList[] newArray(int size) {
            return new MaterialTypeList[size];
        }
    };

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(type);
        parcel.writeString(status);
        parcel.writeString(quantity);
    }
}
