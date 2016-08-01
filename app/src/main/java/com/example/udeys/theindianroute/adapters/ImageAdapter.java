package com.example.udeys.theindianroute.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Siddharth Malhotra on 6/21/2016.
 */

public class ImageAdapter extends BaseAdapter {
    ArrayList<String> imagePath;
    private Context mContext;


    // Constructor
    public ImageAdapter(Context c, ArrayList<String> imagePath) {
        this.imagePath = imagePath;
        mContext = c;
    }

    public int getCount() {
        return imagePath.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        int width= mContext.getResources().getDisplayMetrics().widthPixels;
        width = (width - 10) / 3;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(width, width));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(4 , 0 , 4 , 4);
        } else {
            imageView = (ImageView) convertView;
        }
        Picasso.with(mContext).load(imagePath.get(position)).fit().into(imageView);
        return imageView;
    }
}