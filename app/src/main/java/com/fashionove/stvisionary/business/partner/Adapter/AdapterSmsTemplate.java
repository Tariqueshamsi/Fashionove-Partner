package com.fashionove.stvisionary.business.partner.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fashionove.stvisionary.business.partner.GetterSetter.SmsTemplateData;
import com.fashionove.stvisionary.business.partner.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by developer on 03/02/16.
 */
public class AdapterSmsTemplate extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;
    public static final int ViewTypeCategory = 1;
    List<SmsTemplateData> listData = Collections.emptyList();
    LayoutInflater inflater;


    public AdapterSmsTemplate(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setSmsTemplateData(ArrayList<SmsTemplateData> list) {
        this.listData = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        int view = 0;

        SmsTemplateData data = listData.get(position);
        switch (data.getViewType()) {
            case ViewTypeCategory:
                view = ViewTypeCategory;
                break;
        }

        return view;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = null;
        View view = null;

        switch (viewType) {
            case ViewTypeCategory:
                view = inflater.inflate(R.layout.item_row_sms_template, parent, false);
                vh = new ViewHolderSmSCategoryItem(view);
                break;
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        SmsTemplateData data;

        switch (holder.getItemViewType()) {
            case ViewTypeCategory:
                data = listData.get(position);

                if (data.getTemplateName().isEmpty() == false) {
                    ((ViewHolderSmSCategoryItem) holder).templateName.setText(data.getTemplateName());
                }

                break;

        }

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    class ViewHolderSmSCategoryItem extends RecyclerView.ViewHolder {

       TextView templateName;

        public ViewHolderSmSCategoryItem(View itemView) {
            super(itemView);

            templateName = (TextView) itemView.findViewById(R.id.templateName);

        }
    }

}
