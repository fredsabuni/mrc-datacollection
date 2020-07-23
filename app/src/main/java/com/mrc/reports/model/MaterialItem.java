package com.mrc.reports.model;

import com.mrc.reports.database.Materials_db;

public class MaterialItem {
    String id;
    String name;
    String quantity;
    String status;
    String indexID;




    public MaterialItem(){}

    public MaterialItem(String name, String status){
        this.name = name;
        this.status = status;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MaterialItem other = (MaterialItem) obj;
        if (name != other.name)
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        return true;
    }




    public MaterialItem(String id, String name, String quantity, String status, String indexID){
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.status = status;
        this.indexID = indexID;
    }

    public String getIndexID() {
        return indexID;
    }

    public void setIndexID(String indexID) {
        this.indexID = indexID;
    }

    public MaterialItem(Materials_db materials_db){
        this.id = materials_db.getMaterials_id();
        this.name = materials_db.getMaterials_type();
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
