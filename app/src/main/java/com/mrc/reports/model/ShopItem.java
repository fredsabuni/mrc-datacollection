package com.mrc.reports.model;


import com.mrc.reports.database.ShopType_db;

public class ShopItem {
    String id;
    String shop_type;

    public ShopItem(){}

    public ShopItem(ShopType_db shopType_db){
        this.id = shopType_db.getShop_id();
        this.shop_type = shopType_db.getShop_type();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShop_type() {
        return shop_type;
    }

    public void setShop_type(String shop_type) {
        this.shop_type = shop_type;
    }
}
