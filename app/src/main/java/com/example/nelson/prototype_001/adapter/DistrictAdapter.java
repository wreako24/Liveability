package com.example.nelson.prototype_001.adapter;

/**
 * Created by Nelson on 26/9/2017.
 */

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nelson.prototype_001.R;
import com.example.nelson.prototype_001.entity.DataModel;

public class DistrictAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener{

    private ArrayList<DataModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtScore;
        TextView txtLoc;
        TextView txtVersion;
    }

    public DistrictAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.district_item, data);
        this.dataSet = data;
        this.mContext=context;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.district_item, parent, false);
            viewHolder.txtScore = (TextView) convertView.findViewById(R.id.score);
            viewHolder.txtLoc = (TextView) convertView.findViewById(R.id.loc);
            viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.rank);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        if(dataModel.getRanking().equals("1"))
        viewHolder.txtVersion.setTextColor(Color.parseColor("#ea5050"));

        if(dataModel.getRanking().equals("2"))
            viewHolder.txtVersion.setTextColor(Color.parseColor("#0092b7"));

        if(dataModel.getRanking().equals("3"))
            viewHolder.txtVersion.setTextColor(Color.parseColor("#18782c"));

        if (dataModel.isSelected()) {
            // set your color
             convertView.setBackgroundColor(Color.LTGRAY);
        }else{
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }


        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtScore.setText(dataModel.getScore());
        viewHolder.txtLoc.setText(dataModel.getDistrict());
        viewHolder.txtVersion.setText(dataModel.getRanking());
        // Return the completed view to render on screen
        return convertView;
    }
}