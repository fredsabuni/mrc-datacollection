package com.mrc.reports.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.mrc.reports.R;
import com.mrc.reports.database.MaterialList;
import com.mrc.reports.database.MaterialTypeList;
import com.mrc.reports.model.MaterialItem;

import java.util.ArrayList;

import io.realm.RealmList;

public class UpdateRemovedAdapter extends RecyclerView.Adapter<UpdateRemovedAdapter.ViewHolder>  {
    public static final String MATERIAL_REMOVED = "REMOVED";


    private ItemClickListener onItemClickListener;
    private LayoutInflater layoutInflater;
    private static ArrayList<MaterialItem> mList;
    private Context mContext;
    private ArrayList<MaterialTypeList> new_materialList ;
    private RealmList<MaterialTypeList> materialList = new RealmList<>() ;

    public void setItemClickListener(ItemClickListener clickListener) {
        onItemClickListener = clickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, RealmList<MaterialTypeList> materialItems);
        void onRemoveItemClick(View view, int position);
        void onCurrentItems(View view, ArrayList<MaterialItem> materialItems);
    }

    public UpdateRemovedAdapter(Context context, ArrayList<MaterialItem> list, ArrayList<MaterialTypeList> materialTypeLists){
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
        mList = list;
        new_materialList = materialTypeLists;
    }

    @NonNull
    @Override
    public UpdateRemovedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.material_item, parent, false);
        ViewHolder Holder = new ViewHolder(view);
        return Holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final UpdateRemovedAdapter.ViewHolder holder, final int position) {

        holder.mCheckBox.setText(mList.get(position).getName());

        holder.mCheckBox.setOnCheckedChangeListener(null);

        //Check if checkbox has been checked before


        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, boolean isChecked) {
                holder.mCheckBox.setChecked(isChecked);

                if(isChecked){

                    //Enable Quantity
                    holder.mQuantity.setFocusable(true);
                    holder.mQuantity.setFocusableInTouchMode(true);
                    holder.mQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);


                    holder.mQuantity.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                        if(charSequence.length() != 0)
//                            quantityMap.put(mList.get(position).getId(),holder.mQuantity.getText().toString());

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            materialList.add(new MaterialTypeList(mList.get(position).getId(),mList.get(position).getName(),holder.mQuantity.getText().toString(), MATERIAL_REMOVED));
                            if(!((holder.mQuantity.getText().toString()).isEmpty()))
                                onItemClickListener.onItemClick(compoundButton, materialList);
                        }
                    });

//                    Log.d("mInstalled", materialMap.get(mList.get(position).getId()));


                }else{
                    holder.mQuantity.setText("");
                    //Enable Quantity
                    holder.mQuantity.setFocusable(false);
                    holder.mQuantity.setFocusableInTouchMode(false);
                    holder.mQuantity.setInputType(InputType.TYPE_NULL);

                    onItemClickListener.onRemoveItemClick(compoundButton,position);
                }

            }


        });

    }

    @Override
    public int getItemCount() {
         return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout mView;

        protected CheckBox mCheckBox;
        private TextInputEditText mQuantity;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = (LinearLayout)itemView;
            mCheckBox = itemView.findViewById(R.id.checkbox);
            mQuantity = itemView.findViewById(R.id.quantity);
        }
    }
}
