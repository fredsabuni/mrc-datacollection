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
import com.mrc.reports.model.CompetitorItem;
import com.mrc.reports.model.ServiceItem;

import java.util.ArrayList;
import java.util.UUID;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {
    private ItemClickListener onItemClickListener;
    private LayoutInflater layoutInflater;
    public static ArrayList<ServiceItem> mList;
    private Context mContext;
    String indexID;
    String ID;
    ArrayList<ServiceItem> serviceItems = new ArrayList<>();

    public void setItemClickListener(ItemClickListener clickListener) {
        onItemClickListener = clickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, ArrayList<ServiceItem> serviceItems);
        void onRemoveItemClick(View view, ServiceItem position);
    }

    public ServicesAdapter(Context context, ArrayList<ServiceItem> list){
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
        mList = list;
    }


    @NonNull
    @Override
    public ServicesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.universal_item, parent, false);
        ViewHolder Holder = new ViewHolder(view);
        return Holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ServicesAdapter.ViewHolder holder, final int position) {
        holder.mCheckBox.setText(mList.get(position).getName());

        holder.mCheckBox.setOnCheckedChangeListener(null);

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, boolean isChecked) {
                holder.mCheckBox.setChecked(isChecked);
                if(isChecked){
                    ID = UUID.randomUUID().toString();
                    serviceItems.clear();
                    serviceItems.add(new ServiceItem(ID,mList.get(position).getName()));
                }else {
                    ServiceItem serviceItem = new ServiceItem(ID, mList.get(position).getName());
                    onItemClickListener.onRemoveItemClick(compoundButton,serviceItem);
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = (LinearLayout)itemView;
            mCheckBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
