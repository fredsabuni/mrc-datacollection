package com.mrc.reports.model;

import com.mrc.reports.database.Service_db;

public class ServiceItem {
    String Id;
    String Name;

    public ServiceItem(String Id, String Name){
        this.Id = Id;
        this.Name = Name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public ServiceItem(Service_db service_db){
        this.Id = service_db.get_id();
        this.Name = service_db.get_type();
    }


}
