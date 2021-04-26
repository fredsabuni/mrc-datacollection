package com.mrc.reports.database;

import com.mrc.reports.model.ServiceItem;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Service_db extends RealmObject {
    @PrimaryKey
    private String _id;
    private String _type;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public Service_db(){}

    public Service_db(ServiceItem serviceItem){
        _id = serviceItem.getId();
        _type = serviceItem.getName();
    }

    /**
     * Persist Service_db to local database
     * @param serviceItems json array of LocationItems
     *
     */

    public static void persistToDatabase(List<ServiceItem> serviceItems){
        if(serviceItems == null) return;

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        int count= serviceItems.size();
        for(int index = 0; index< count; index++){
            Service_db serviceDb = new Service_db(serviceItems.get(index));
            realm.insertOrUpdate(serviceDb);
        }
        realm.commitTransaction();
    }


}
