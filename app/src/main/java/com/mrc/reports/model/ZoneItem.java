package com.mrc.reports.model;

import com.mrc.reports.database.ShopType_db;
import com.mrc.reports.database.Zone_db;

public class ZoneItem extends LocationItem {
    public ZoneItem(){}

    public ZoneItem(Zone_db zone_db){
        this.id = zone_db.getZone_id();
        this.location_name = zone_db.getZone_name();
    }
}
