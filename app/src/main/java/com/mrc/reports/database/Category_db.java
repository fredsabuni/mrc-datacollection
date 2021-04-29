package com.mrc.reports.database;



import com.mrc.reports.model.CategoryItem;
import com.mrc.reports.model.CompetitorItem;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Category_db extends RealmObject {
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

    public Category_db(CategoryItem categoryItem){
        _id = categoryItem.getId();
        _name = categoryItem.getName();
    }

    public Category_db(){}

    /**
     * Persist Category_db to local database
     * @param categoryItems
     *
     */

    public static void persistToDatabase(List<CategoryItem> categoryItems){
        if(categoryItems == null) return;

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        int count= categoryItems.size();
        for(int index = 0; index< count; index++){
            Category_db category_db = new Category_db(categoryItems.get(index));
            realm.insertOrUpdate(category_db);
        }
        realm.commitTransaction();
    }
}
