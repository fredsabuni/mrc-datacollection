package com.mrc.reports.model;

import com.mrc.reports.database.Region_db;

public class RegionItem extends LocationItem {
    String zone_id;

    public RegionItem(){}

    public RegionItem(Region_db region_db){
        this.id = region_db.getRegion_id();
        this.location_name = region_db.getRegion_name();
        this.zone_id = region_db.getZone_id();
    }

    public String getZone_id() {
        return zone_id;
    }

    public void setZone_id(String zone_id) {
        this.zone_id = zone_id;
    }
}
