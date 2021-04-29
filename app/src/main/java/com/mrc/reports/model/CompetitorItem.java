package com.mrc.reports.model;


import com.mrc.reports.database.Competitor_db;

public class CompetitorItem {
    String id;
    String Name;

    public CompetitorItem(){}

    public CompetitorItem(String id, String Name){
        this.id = id;
        this.Name = Name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public CompetitorItem(Competitor_db competitor_db){
        this.id = competitor_db.get_id();
        this.Name = competitor_db.get_name();
    }


}
