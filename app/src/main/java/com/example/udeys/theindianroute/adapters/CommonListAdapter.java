package com.example.udeys.theindianroute.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.udeys.theindianroute.R;
import com.example.udeys.theindianroute.helperClasses.CommonListClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gitesh on 28-07-2016.
 */
public class CommonListAdapter extends ArrayAdapter {
    List list = new ArrayList();
    String status;

    public CommonListAdapter(Context context, int resource) {
        super(context, resource);
    }

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        final itemHolder itemHolder;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.commonlistlayout, parent, false);
            itemHolder = new itemHolder();
            itemHolder.username = (TextView) row.findViewById(R.id.list_username);
            itemHolder.pp = (com.makeramen.roundedimageview.RoundedImageView) row.findViewById(R.id.cuserProfilePicture);
            itemHolder.status = (Button)row.findViewById(R.id.follow_status);
            row.setTag(itemHolder);
        } else {
            itemHolder = (itemHolder) row.getTag();
        }
        final CommonListClass commonListClass = (CommonListClass) this.getItem(position);
        itemHolder.username.setText(commonListClass.getUsername());
        String user_pp = commonListClass.getProfilepic();

        if (user_pp.equals("http://theindianroute.net/uploads/users_profile_pictures/")) {
            Log.e("user_pp-if", user_pp);
            Picasso.with(getContext()).load(R.drawable.dummyprofile).placeholder(R.drawable.ppplaceholder).into(itemHolder.pp);
        } else
            Picasso.with(getContext()).load(user_pp).into(itemHolder.pp);
        status = commonListClass.getStatus();
        if(status.contentEquals("1")){
            itemHolder.status.setText("Following");
            itemHolder.status.setBackgroundResource(R.drawable.roundedbutton);
            itemHolder.status.setTextColor(Color.parseColor("#000000"));
        }
        else {
            itemHolder.status.setText("Follow");
            itemHolder.status.setBackgroundResource(R.drawable.roundedbutton_empty);
            itemHolder.status.setTextColor(Color.parseColor("#ffdf32"));
        }

        return row;
    }

    static class itemHolder {
        TextView username;
        com.makeramen.roundedimageview.RoundedImageView pp;
        Button status;
    }

}
