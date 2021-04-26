package com.mrc.reports.database;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

public class SurveyServiceList extends RealmObject implements Parcelable {
    String id;
    String name;

    public SurveyServiceList(){}

    public SurveyServiceList(String id, String name){
        this.id = id;
        this.name = name;
    }

    protected SurveyServiceList(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<SurveyServiceList> CREATOR = new Creator<SurveyServiceList>() {
        @Override
        public SurveyServiceList createFromParcel(Parcel in) {
            return new SurveyServiceList(in);
        }

        @Override
        public SurveyServiceList[] newArray(int size) {
            return new SurveyServiceList[size];
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
