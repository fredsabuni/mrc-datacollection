package com.mrc.reports.model;

import com.mrc.reports.database.Category_db;
import com.mrc.reports.database.Competitor_db;

public class CategoryItem {
    String id;
    String name;

    public CategoryItem(String id, String name){
        this.id = id;
        this.name = name;
    }

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

    public CategoryItem(Category_db category_db){
        this.id = category_db.get_id();
        this.name = category_db.get_name();
    }
}
