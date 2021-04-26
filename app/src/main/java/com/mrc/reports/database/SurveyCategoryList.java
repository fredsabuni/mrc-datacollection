package com.mrc.reports.database;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SurveyCategoryList extends RealmObject implements Parcelable {
    @PrimaryKey
    private String id;
    private String name;

    public SurveyCategoryList(){}

    public SurveyCategoryList(String id, String name){
        this.id = id;
        this.name = name;
    }

    protected SurveyCategoryList(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<SurveyCategoryList> CREATOR = new Creator<SurveyCategoryList>() {
        @Override
        public SurveyCategoryList createFromParcel(Parcel in) {
            return new SurveyCategoryList(in);
        }

        @Override
        public SurveyCategoryList[] newArray(int size) {
            return new SurveyCategoryList[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }
}
