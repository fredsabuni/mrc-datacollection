package com.mrc.reports.database;

import com.mrc.reports.model.LocationItem;
import com.mrc.reports.model.ZoneItem;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Zone_db extends RealmObject {
    @PrimaryKey
    private String zone_id;
    private String zone_name;

    public Zone_db(){}

    public String getZone_id() {
        return zone_id;
    }

    public void setZone_id(String zone_id) {
        this.zone_id = zone_id;
    }

    public String getZone_name() {
        return zone_name;
    }

    public void setZone_name(String zone_name) {
        this.zone_name = zone_name;
    }

    public Zone_db(ZoneItem zoneItem) {
        zone_id = zoneItem.getId();
        zone_name = zoneItem.getLocation_name();
    }

    /**
     * Persist Region_db to local database
     * @param zoneItems json array of LocationItems
     *
     */

    public static void persistToDatabase(List<ZoneItem> zoneItems){
        if(zoneItems == null) return;

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        int count= zoneItems.size();
        for(int index = 0; index< count; index++){
            Zone_db zoneDb = new Zone_db(zoneItems.get(index));
            realm.insertOrUpdate(zoneDb);
        }
        realm.commitTransaction();
    }
}
