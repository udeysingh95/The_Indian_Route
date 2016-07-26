package com.example.udeys.theindianroute.filters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.udeys.theindianroute.R;


/**
 * Created by mnafian on 6/16/15.
 */
public class FilterAdapterFactory extends RecyclerView.Adapter<FilterAdapterFactory.FilterHolder> {

    private String itemData[] = {
            "No Effect",
            "Autofix",
            "BW",
            "Brightness",
            "Contrast",
            "CP",
            "Doc",
            "Duotone",
            "Fillight",
            "FishEye",
            "Flipert",
            "Fliphor",
            "Grain",
            "Grayscale",
            "Lomoish",
            "Negative",
            "Posterize",
            "Rotate",
            "Saturate",
            "Sepia",
            "Sharpen",
            "Temp",
            "TintEffect",
            "Vignette"};
    private Context mContext;

    public FilterAdapterFactory(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    public FilterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.imf_filter_item, parent, false);
        FilterHolder viewHolder = new FilterHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FilterHolder holder, int position) {
        holder.filterName.setText(itemData[position]);
        holder.imFilter.setImageResource(R.drawable.filter_effetcs);
    }

    @Override
    public int getItemCount() {
        return itemData.length;
    }

    public class FilterHolder extends RecyclerView.ViewHolder {
        public ImageView imFilter;
        public TextView filterName;

        public FilterHolder(View itemView) {
            super(itemView);
            imFilter = (ImageView) itemView.findViewById(R.id.effectsviewimage_item);
            filterName = (TextView) itemView.findViewById(R.id.filter_name);
        }
    }
}
