package com.example.udeys.theindianroute.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gitesh on 22-06-2016.
 */
public class commentsAdapter extends ArrayAdapter {
    List list = new ArrayList();

    public commentsAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }


}