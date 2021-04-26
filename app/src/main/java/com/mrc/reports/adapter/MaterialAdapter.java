package com.mrc.reports.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mrc.reports.R;
import com.mrc.reports.model.MaterialItem;

import java.util.ArrayList;
import java.util.UUID;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.ViewHolder> {

    public static final String MATERIAL_INSTALLED = "INSTALLED";
    public static final String QUANTITY = "0";

    private MaterialClickListener materialClickListener;
    private LayoutInflater layoutInflater;
    public static ArrayList<MaterialItem> mList;
    private Context mContext;
    String ID;
    String indexID;
    ArrayList<MaterialItem> materialList_list = new ArrayList<>();

    public MaterialAdapter(Context context, ArrayList<MaterialItem> list){
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
        mList = list;
    }

    public void setMaterialClickListener(MaterialClickListener clickListener) {
        materialClickListener = clickListener;
    }

    public interface MaterialClickListener {
        void onMaterialClick(View view, ArrayList<MaterialItem> materialItems);
        void onMaterialRemoveClick(View view, MaterialItem materialItem);
    }

    @NonNull
    @Override
    public MaterialAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.universal_item, parent, false);
        MaterialAdapter.ViewHolder viewHolder = new MaterialAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.mCheckBox.setText(mList.get(position).getName());

        holder.mCheckBox.setOnCheckedChangeListener(null);

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, boolean isChecked) {
                holder.mCheckBox.setChecked(isChecked);
                if(isChecked){
                    ID = UUID.randomUUID().toString();
                    indexID = mList.get(position).getName() + MATERIAL_INSTALLED;
                    materialList_list.clear();
                    materialList_list.add(new MaterialItem(ID,mList.get(position).getName(),QUANTITY ,MATERIAL_INSTALLED, indexID));
                }else {
                    MaterialItem materialItem = new MaterialItem(mList.get(position).getName(),MATERIAL_INSTALLED);
                    materialClickListener.onMaterialRemoveClick(compoundButton,materialItem);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout mView;

        protected CheckBox mCheckBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = (LinearLayout)itemView;
            mCheckBox = itemView.findViewById(R.id.checkbox);
        }
    }

}