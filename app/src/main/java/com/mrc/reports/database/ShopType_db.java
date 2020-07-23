package com.mrc.reports.database;

import com.mrc.reports.model.ShopItem;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ShopType_db extends RealmObject {
    @PrimaryKey
    private String shop_id;
    private String shop_type;

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_type() {
        return shop_type;
    }

    public void setShop_type(String shop_type) {
        this.shop_type = shop_type;
    }

    public ShopType_db(){}

    public ShopType_db(ShopItem shopItem){
        shop_id = shopItem.getId();
        shop_type = shopItem.getShop_type();
    }

    /**
     * Persist ShopType_db to local database
     * @param shopItems json array of LocationItems
     *
     */

    public static void persistToDatabase(List<ShopItem> shopItems){
        if(shopItems == null) return;

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        int count= shopItems.size();
        for(int index = 0; index< count; index++){
            ShopType_db shopTypeDb = new ShopType_db(shopItems.get(index));
            realm.insertOrUpdate(shopTypeDb);
        }
        realm.commitTransaction();
    }
}
