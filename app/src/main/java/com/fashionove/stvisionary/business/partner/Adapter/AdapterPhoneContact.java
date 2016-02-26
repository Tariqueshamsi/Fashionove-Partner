package com.fashionove.stvisionary.business.partner.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.fashionove.stvisionary.business.partner.GetterSetter.ContactData;
import com.fashionove.stvisionary.business.partner.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by developer on 10/02/16.
 */
public class AdapterPhoneContact extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    public static final int ViewTypeCategory = 1;
    List<ContactData> listData = Collections.emptyList();
    LayoutInflater inflater;


    public AdapterPhoneContact(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setPhoneContactData(ArrayList<ContactData> list) {
        this.listData = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        int view = 0;

        ContactData data = listData.get(position);
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
                view = inflater.inflate(R.layout.item_row_phone_contact, parent, false);
                vh = new ViewHolderContact(view);
                break;
        }

        return vh;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        final ContactData data;
        final int pos = position;

        switch (holder.getItemViewType()) {
            case ViewTypeCategory:
                data = listData.get(position);

                String c = data.getName().charAt(0) + "";
                setDrawable(((ViewHolderContact) holder).drawableImage, c.trim());
                ((ViewHolderContact) holder).contactName.setText(data.getName());

                ((ViewHolderContact) holder).checkBox.setChecked(data.getIsSelected());
                ((ViewHolderContact) holder).checkBox.setTag(data);

                ((ViewHolderContact) holder).checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CheckBox cb = (CheckBox) v;
                        ContactData contact = (ContactData) cb.getTag();

                        contact.setIsSelected(cb.isChecked());
                        listData.get(pos).setIsSelected(cb.isChecked());

                    }
                });

            /*    if (data.getChecked() == 0) {
                    ((ViewHolderContact) holder).checkBox.setChecked(false);
                } else if (data.getChecked() == 1) {
                    ((ViewHolderContact) holder).checkBox.setChecked(true);
                }

                ((ViewHolderContact) holder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isChecked == true ) {
                            ((ViewHolderContact) holder).checkBox.setChecked(true);
                            data.setChecked(1);

                        } else if(isChecked == false ){
                            ((ViewHolderContact) holder).checkBox.setChecked(false);
                            data.setChecked(0);
                        }
                    }
                });
                */

                break;
        }
    }

    public void setDrawable(ImageView imageView, String alphabet) {
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color1 = generator.getRandomColor();


        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .fontSize(40) /* size in px */
                .toUpperCase()
                .height(80)
                .width(80)
                .endConfig()
                .buildRound(alphabet, color1);

        imageView.setImageDrawable(drawable);

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    class ViewHolderContact extends RecyclerView.ViewHolder {

        ImageView drawableImage;
        TextView contactName;
        CheckBox checkBox;

        public ViewHolderContact(View itemView) {
            super(itemView);

            drawableImage = (ImageView) itemView.findViewById(R.id.drawable_image);
            contactName = (TextView) itemView.findViewById(R.id.contactName);
            checkBox = (CheckBox) itemView.findViewById(R.id.contactChooser);

        }
    }
}
