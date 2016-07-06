package com.example.udeys.theindianroute.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.udeys.theindianroute.R;
import com.example.udeys.theindianroute.helperClasses.Comments;
import com.example.udeys.theindianroute.helperClasses.Posts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gitesh on 22-06-2016.
 */
public class commentsAdapter extends ArrayAdapter {
    List list = new ArrayList();
    Posts p;
    String post_id;

    public commentsAdapter(Context context, int resource, String p) {
        super(context, resource);
        post_id = p;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        final commentHolder commentHolder;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.commentrowlayout, parent, false);
            commentHolder = new commentHolder();
            commentHolder.commentersComments = (TextView) row.findViewById(R.id.commenters_comments);
            commentHolder.post_comment = (Button) row.findViewById(R.id.post_comment);
            commentHolder.writecomment = (EditText) row.findViewById(R.id.write_comment);

            row.setTag(commentHolder);
        } else {
            commentHolder = (commentHolder) row.getTag();
        }
        final Comments Comments = (Comments) this.getItem(position);
        commentHolder.commentersComments.setText(Comments.getComments());


        return row;
    }

    static class commentHolder {
        TextView commentersComments;
        EditText writecomment;
        Button post_comment;
    }
}
