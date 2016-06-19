package com.example.udeys.theindianroute.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.udeys.theindianroute.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gitesh on 14-06-2016.
 */
public class PostAdapter extends ArrayAdapter {
    int liker = 0;
    List list = new ArrayList();
    Typeface samarn, fa;


    public PostAdapter(Context context, int resource, Typeface cFont, Typeface FontAwesome) {
        super(context, resource);
        samarn = cFont;
        fa = FontAwesome;
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
        final PostHolder postHolder;

        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.timelinerowlayout, parent, false);
            postHolder = new PostHolder();
            postHolder.username = (TextView) row.findViewById(R.id.username);
            postHolder.reaction = (TextView) row.findViewById(R.id.icon_like);
            postHolder.comment = (TextView) row.findViewById(R.id.icon_comment);
            postHolder.no_of_reactions = (TextView) row.findViewById(R.id.likes);
            postHolder.userPostImage = (ImageView) row.findViewById(R.id.userpostimage);
            postHolder.userprofilePicture = (RoundedImageView) row.findViewById(R.id.userProfilePicture);
            row.setTag(postHolder);
        } else {
            postHolder = (PostHolder) row.getTag();
        }

        final posts posts = (posts) this.getItem(position);
        postHolder.username.setTypeface(samarn);
        postHolder.username.setText(posts.getUsername());
        postHolder.reaction.setTypeface(fa);
        postHolder.reaction.setTextColor(Color.BLACK);
        postHolder.reaction.setTextSize(24);
        postHolder.no_of_reactions.setText(String.valueOf(posts.getReaction()));
        postHolder.reaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liker == 0) {
                    postHolder.reaction.setText(R.string.icon_heart_filled);
                    postHolder.reaction.setTextColor(Color.RED);
                    postHolder.no_of_reactions.setText(String.valueOf(posts.getReaction() + 1));
                    liker = 1;
                } else {
                    postHolder.reaction.setText(R.string.icon_heart_empty);
                    postHolder.reaction.setTextColor(Color.BLACK);
                    postHolder.no_of_reactions.setText(String.valueOf(posts.getReaction()));
                    liker = 0;
                }
            }
        });
        postHolder.comment.setTypeface(fa);
        postHolder.comment.setTextSize(24);
        Picasso.with(getContext())
                .load(posts.getUserProfilePicture()).placeholder(R.drawable.ppplaceholder)
                .into(postHolder.userprofilePicture);
        Picasso.with(getContext())
                .load(posts.getPictue()).resize(250, 300).centerCrop()
                .into(postHolder.userPostImage);


        return row;
    }


    static class PostHolder {
        TextView username, reaction, comment, no_of_reactions;
        ImageView userPostImage;
        com.makeramen.roundedimageview.RoundedImageView userprofilePicture;

    }


}
