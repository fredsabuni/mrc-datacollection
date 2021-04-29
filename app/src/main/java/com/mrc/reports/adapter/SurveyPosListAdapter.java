package com.mrc.reports.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mrc.reports.R;
import com.mrc.reports.database.MaterialTypeList;
import com.mrc.reports.database.SurveyMaterialList;
import com.mrc.reports.model.MaterialItem;
import com.mrc.reports.model.SurveyItem;
import com.mrc.reports.ui.Update_ui;
import com.mrc.reports.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

import static com.mrc.reports.ui.Update_ui.CURRENT_MATERIAL;
import static com.mrc.reports.ui.Update_ui.CURRENT_MRC;

public class SurveyPosListAdapter extends RecyclerView.Adapter<SurveyPosListAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private static ArrayList<SurveyItem> mList;
    private static RealmList<SurveyMaterialList> surveyMaterialListRealmList;
    private Context mContext;
    Realm mRealm;

    public SurveyPosListAdapter(Context context, ArrayList<SurveyItem> surveyItems, Realm realm){
        mContext = context;
        mList = surveyItems;
        layoutInflater = LayoutInflater.from(context);
        mRealm = realm;
    }

    @NonNull
    @Override
    public SurveyPosListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.pos_item, parent, false);
        SurveyPosListAdapter.ViewHolder Holder = new SurveyPosListAdapter.ViewHolder(view);
        return Holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SurveyPosListAdapter.ViewHolder holder, final int position) {
        holder.mPosName.setText(mList.get(position).getPos_name());
        holder.mPosCode.setText(mList.get(position).getPos_code());

        if(mList.get(position).getStatus() == 1){
            holder.mStatus.setTextColor(ContextCompat.getColor(mContext, R.color.orange_700));
            holder.mStatus.setText("Status: Pending");
        }
        else if(mList.get(position).getStatus() == 2){
            holder.mStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green_700));
            holder.mStatus.setText("Status: uploaded");
        }

        Bitmap photo = Utils.base64ToBitmap(mList.get(position).get_img());
        holder.mImg.setImageBitmap(photo);


        final List<SurveyMaterialList> materialType = new ArrayList<>();
        materialType.addAll(mRealm.copyFromRealm(mList.get(position).getSurveyMaterialLists()));


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Update_ui.class);
                intent.putExtra(CURRENT_MRC, mList.get(position));
                intent.putParcelableArrayListExtra(CURRENT_MATERIAL, (ArrayList<? extends Parcelable>) materialType);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final CardView mView;

        private TextView mPosName;
        private TextView mPosCode;
        private TextView mStatus;
        private ImageView mImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = (CardView)itemView;
            mPosName = itemView.findViewById(R.id.edt_pos_name);
            mPosCode = itemView.findViewById(R.id.edt_pos_code);
            mStatus = itemView.findViewById(R.id.edt_status);
            mImg = itemView.findViewById(R.id.pos_img);
        }
    }
}
