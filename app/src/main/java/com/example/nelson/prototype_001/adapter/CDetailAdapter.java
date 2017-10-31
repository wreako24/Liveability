package com.example.nelson.prototype_001.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nelson.prototype_001.PinClick;
import com.example.nelson.prototype_001.R;
import com.example.nelson.prototype_001.entity.DataModel;

import java.util.ArrayList;

/**
 * Created by Nelson on 3/10/2017.
 */

public class CDetailAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener{

    private ArrayList<DataModel> dataSet;
    Context mContext;
    PinClick mListerner;

    // View lookup cache
    private static class ViewHolder {
        TextView txtAddr;
        TextView txtDetail;
        ImageView ppBut;
    }

    public CDetailAdapter(ArrayList<DataModel> data, Context context, PinClick listener) {
        super(context, R.layout.criteria_detail_row_item, data);
        this.dataSet = data;
        this.mContext=context;

        mListerner=listener;
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModel dataModel=(DataModel)object;

        switch (v.getId())
        {

            /*case R.id.item_info:
                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;*/
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.criteria_detail_row_item, parent, false);
            viewHolder.txtAddr = (TextView) convertView.findViewById(R.id.addr);
            viewHolder.txtDetail = (TextView) convertView.findViewById(R.id.addrDet);
            viewHolder.ppBut=(ImageView) convertView.findViewById(R.id.imageView);

            viewHolder.ppBut.setImageResource(R.drawable.map_icon);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtAddr.setText(dataModel.getDistrict());
        viewHolder.txtDetail.setText(dataModel.getDesc());
        viewHolder.ppBut.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
              mListerner.onClick(v,position);


            }


        });

        // Return the completed view to render on screen
        return convertView;
    }
}