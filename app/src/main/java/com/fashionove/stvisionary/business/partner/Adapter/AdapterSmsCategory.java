package com.fashionove.stvisionary.business.partner.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.fashionove.stvisionary.business.partner.GetterSetter.SmsCategoryData;
import com.fashionove.stvisionary.business.partner.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by developer on 03/02/16.
 */
public class AdapterSmsCategory extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    public static final int ViewTypeCategory = 1;
    List<SmsCategoryData> listData = Collections.emptyList();
    LayoutInflater inflater;


    public AdapterSmsCategory(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setSmsCategoryData(ArrayList<SmsCategoryData> list) {
        this.listData = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        int view = 0;

        SmsCategoryData data = listData.get(position);
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
                view = inflater.inflate(R.layout.item_row_sms_category, parent, false);
                vh = new ViewHolderSmSCategoryItem(view);
                break;
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        SmsCategoryData data;

        switch (holder.getItemViewType()) {
            case ViewTypeCategory:
                data = listData.get(position);

                if (data.getCategoryName().isEmpty() == false) {
                    ((ViewHolderSmSCategoryItem) holder).categoryName.setText(data.getCategoryName());
                }

                loadFrancoImage(data.getCategoryImage(),((ViewHolderSmSCategoryItem) holder).categoryImage);


                break;

        }

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void loadFrancoImage(String urlImage, final SimpleDraweeView simpleDraweeView) {

        Uri uri = Uri.parse(urlImage);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();

        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setImageRequest(request)
                .setOldController(simpleDraweeView.getController())
                .build();
        simpleDraweeView.setController(draweeController);

    }


    class ViewHolderSmSCategoryItem extends RecyclerView.ViewHolder {

        SimpleDraweeView categoryImage;
        TextView categoryName;

        public ViewHolderSmSCategoryItem(View itemView) {
            super(itemView);

            categoryImage = (SimpleDraweeView) itemView.findViewById(R.id.categoryImage);
            categoryName = (TextView) itemView.findViewById(R.id.categoryName);

        }
    }

}
