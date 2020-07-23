package com.mrc.reports.model;

import com.mrc.reports.database.Region_db;
import com.mrc.reports.database.Zone_db;

public class LocationItem {

    String id;
    String location_name;

    public LocationItem(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }
}
