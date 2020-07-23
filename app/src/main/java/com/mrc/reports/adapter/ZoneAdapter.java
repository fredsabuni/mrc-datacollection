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
import com.mrc.reports.model.ZoneItem;

import java.util.ArrayList;

public class ZoneAdapter extends ArrayAdapter<ZoneItem> {
    Context mContext;
    TextView mtitle;

    public ZoneAdapter(@NonNull Context context, ArrayList<ZoneItem> zoneItems) {
        super(context,0, zoneItems);
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

        ZoneItem currentItem = getItem(position);
        mtitle = convertView.findViewById(R.id.title);
        mtitle.setText(currentItem.getLocation_name());

        return convertView;
    }
}
