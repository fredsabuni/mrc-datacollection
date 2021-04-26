package com.mrc.reports.database;

import com.mrc.reports.model.CompetitorItem;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Competitor_db extends RealmObject {
    @PrimaryKey
    private String _id;
    private String _name;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public Competitor_db(){}

    public Competitor_db(CompetitorItem competitorItem){
        _id = competitorItem.getId();
        _name = competitorItem.getName();
    }

    /**
     * Persist Competitor_db to local database
     * @param competitorItems
     *
     */

    public static void persistToDatabase(List<CompetitorItem> competitorItems){
        if(competitorItems == null) return;

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        int count= competitorItems.size();
        for(int index = 0; index< count; index++){
            Competitor_db competitor_db = new Competitor_db(competitorItems.get(index));
            realm.insertOrUpdate(competitor_db);
        }
        realm.commitTransaction();
    }
}
