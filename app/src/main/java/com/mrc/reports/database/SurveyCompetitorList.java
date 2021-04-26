package com.mrc.reports.database;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

public class SurveyCompetitorList extends RealmObject implements Parcelable {
    String id;
    String name;

    public SurveyCompetitorList(){}

    public SurveyCompetitorList(String id, String name){
        this.id = id;
        this.name = name;
    }

    protected SurveyCompetitorList(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<SurveyCompetitorList> CREATOR = new Creator<SurveyCompetitorList>() {
        @Override
        public SurveyCompetitorList createFromParcel(Parcel in) {
            return new SurveyCompetitorList(in);
        }

        @Override
        public SurveyCompetitorList[] newArray(int size) {
            return new SurveyCompetitorList[size];
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
