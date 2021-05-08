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
import com.mrc.reports.model.CategoryItem;
import com.mrc.reports.model.CompetitorItem;

import java.util.ArrayList;
import java.util.UUID;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
    private CategoryClickListener categoryClickListener;
    private LayoutInflater layoutInflater;
    public static ArrayList<CategoryItem> mList;
    private Context mContext;
    String indexID;
    String ID;
    ArrayList<CategoryItem> categoryItems = new ArrayList<>();

    public void setItemClickListener(CategoryClickListener clickListener) {
        categoryClickListener = clickListener;
    }

    public interface CategoryClickListener {
        void onCategoryItemClick(View view, ArrayList<CategoryItem> competitorItems);
        void onCategoryRemoveItemClick(View view, CategoryItem position);
    }

    public CategoryAdapter(Context context, ArrayList<CategoryItem> list){
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
        mList = list;
    }


    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.universal_item, parent, false);
        CategoryAdapter.ViewHolder Holder = new CategoryAdapter.ViewHolder(view);
        return Holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryAdapter.ViewHolder holder, final int position) {

        holder.mCheckBox.setText(mList.get(position).getName());

        holder.mCheckBox.setOnCheckedChangeListener(null);

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, boolean isChecked) {
                holder.mCheckBox.setChecked(isChecked);
                if(isChecked){
                    ID = UUID.randomUUID().toString();
                    categoryItems.clear();
                    categoryItems.add(new CategoryItem(ID,mList.get(position).getName()));
                    categoryClickListener.onCategoryItemClick(compoundButton,categoryItems);
                }else {
                    CategoryItem categoryItem = new CategoryItem(ID, mList.get(position).getName());
                    categoryClickListener.onCategoryRemoveItemClick(compoundButton,categoryItem);
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
