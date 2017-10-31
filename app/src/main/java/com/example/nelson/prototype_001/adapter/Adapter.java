package com.example.nelson.prototype_001.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nelson.prototype_001.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter extends ArrayAdapter<Data> {

    final int INVALID_ID = -1;

    final Map<Data, Integer> mIdMap = new HashMap<>();
    ArrayList<Data>dList=new ArrayList<>();

    public Adapter(Context context, ArrayList<Data> list) {
        super(context, 0, list);
        for (int i = 0; i < list.size(); ++i) {
            mIdMap.put(list.get(i), i);
        }
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        Context context = getContext();
        final Data data = getItem(position);



        if(null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.menulist, null);
        }
        final RelativeLayout row = (RelativeLayout) view.findViewById(R.id.lytPattern);
        ImageView imageView = (ImageView)view.findViewById(R.id.imageViewGrab);
        TextView textView = (TextView)view.findViewById(R.id.textViewTitle);
        textView.setText(data.title);
        imageView.setImageResource(data.imgid);



        return view;
    }

    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return INVALID_ID;
        }
        Data item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
