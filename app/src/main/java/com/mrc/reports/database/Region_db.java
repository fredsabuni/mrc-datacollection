package com.mrc.reports.database;

import com.mrc.reports.model.RegionItem;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Region_db extends RealmObject {
    @PrimaryKey
    private String region_id;
    private String region_name;
    private String zone_id;

    public String getZone_id() {
        return zone_id;
    }

    public void setZone_id(String zone_id) {
        this.zone_id = zone_id;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }

    public Region_db(){}

    public Region_db(RegionItem regionItem){
        region_id = regionItem.getId();
        region_name = regionItem.getLocation_name();
        zone_id = regionItem.getZone_id();
    }

    /**
     * Persist Region_db to local database
     * @param regionItems json array of LocationItems
     *
     */

    public static void persistToDatabase(List<RegionItem> regionItems){
        if(regionItems == null) return;

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        int count= regionItems.size();
        for(int index = 0; index< count; index++){
            Region_db regionDb = new Region_db(regionItems.get(index));
            realm.insertOrUpdate(regionDb);
        }
        realm.commitTransaction();
    }
}
