package com.mrc.reports.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mrc.reports.R;
import com.mrc.reports.model.ShopItem;

import java.util.ArrayList;


public class ShopAdapter  extends ArrayAdapter<ShopItem> {

    Context mContext;
    TextView mtitle;

    public ShopAdapter(@NonNull Context context, ArrayList<ShopItem> shopItems) {
        super(context,0, shopItems);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_row_item, parent, false
            );
        }

        ShopItem currentItem = getItem(position);
        mtitle = convertView.findViewById(R.id.title);
        mtitle.setText(currentItem.getShop_type());

        return convertView;
    }
}
