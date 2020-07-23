package com.mrc.reports.database;
public class MaterialList {

    String id;
    String name;
    String quantity;
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MaterialList(){}

    public MaterialList(MaterialTypeList materialTypeList){
        this.id = materialTypeList.getId();
        this.name = materialTypeList.getType();
        this.quantity = materialTypeList.getQuantity();
        this.status = materialTypeList.getStatus();

    }

    public MaterialList(String id, String name, String quantity, String status){
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.status = status;
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
}
