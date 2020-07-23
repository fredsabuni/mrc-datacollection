package com.mrc.reports.database;

import com.mrc.reports.model.MaterialItem;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Materials_db extends RealmObject {
    @PrimaryKey
    private String materials_id;
    private String materials_type;

    public String getMaterials_id() {
        return materials_id;
    }

    public void setMaterials_id(String materials_id) {
        this.materials_id = materials_id;
    }

    public String getMaterials_type() {
        return materials_type;
    }

    public void setMaterials_type(String materials_type) {
        this.materials_type = materials_type;
    }

    public Materials_db(){}

    public Materials_db(MaterialItem materialItem){
        materials_id = materialItem.getId();
        materials_type = materialItem.getName();
    }

    /**
     * Persist MaterialType_db to local database
     * @param materialItems json array of LocationItems
     *
     */

    public static void persistToDatabase(List<MaterialItem> materialItems){
        if(materialItems == null) return;

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        int count= materialItems.size();
        for(int index = 0; index< count; index++){
            Materials_db materialsDb = new Materials_db(materialItems.get(index));
            realm.insertOrUpdate(materialsDb);
        }
        realm.commitTransaction();
    }
}
