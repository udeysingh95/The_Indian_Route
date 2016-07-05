package com.example.udeys.theindianroute.adapters;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.udeys.theindianroute.R;
import com.example.udeys.theindianroute.fragments.CommentFragment;
import com.example.udeys.theindianroute.fragments.HomeFragment;
import com.example.udeys.theindianroute.helperClasses.posts;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Gitesh on 14-06-2016.
 */
public class PostAdapter extends ArrayAdapter {
    public static String user_id;
    static int state;
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
            postHolder.no_of_comments = (TextView) row.findViewById(R.id.comments);
            postHolder.userPostImage = (ImageView) row.findViewById(R.id.userpostimage);
            postHolder.userprofilePicture = (RoundedImageView) row.findViewById(R.id.userProfilePicture);
            row.setTag(postHolder);
        } else {
            postHolder = (PostHolder) row.getTag();
        }

        final posts Posts = (posts) this.getItem(position);
        state = Posts.getstate();
        user_id = Posts.getUser_id();
        postHolder.username.setTypeface(samarn);
        postHolder.username.setText(Posts.getUsername());
        postHolder.reaction.setTypeface(fa);
        postHolder.reaction.setTextSize(30);
        postHolder.no_of_comments.setText(String.valueOf(Posts.getComment()));
        if (state == 1) {
            postHolder.reaction.setText(R.string.icon_heart_filled);
            postHolder.reaction.setTextColor(Color.RED);
        } else {
            postHolder.reaction.setText(R.string.icon_heart_empty);
            postHolder.reaction.setTextColor(Color.BLACK);
        }
        postHolder.reaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == 0) {
                    postHolder.reaction.setText(R.string.icon_heart_filled);
                    postHolder.reaction.setTextColor(Color.RED);
                    postHolder.no_of_reactions.setText(String.valueOf(Posts.getReaction() + 1));
                    state = 1;
                    Log.d("user_id", HomeFragment.user_id);
                    likeNotification(HomeFragment.user_id, Posts.getPost_id());
                } else {
                    postHolder.reaction.setText(R.string.icon_heart_empty);
                    postHolder.reaction.setTextColor(Color.BLACK);
                    postHolder.no_of_reactions.setText(String.valueOf(Posts.getReaction() - 1));
                    state = 0;
                }
                post_reaction(state, user_id, Posts.getPost_id());

            }
        });
        postHolder.no_of_reactions.setText(String.valueOf(Posts.getReaction()));
        postHolder.comment.setTypeface(fa);
        postHolder.comment.setTextSize(30);
        postHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                CommentFragment fragment = new CommentFragment();
                Bundle bundle = new Bundle();
                bundle.putString("username", Posts.getUsername());
                bundle.putString("post_id", Posts.getPost_id());
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_1, fragment);
                fragmentTransaction.commit();
            }
        });

        Picasso.with(getContext())
                .load(Posts.getUserProfilePicture()).placeholder(R.drawable.ppplaceholder)
                .into(postHolder.userprofilePicture);
        Picasso.with(getContext())
                .load(Posts.getPictue()).resize(250, 300).centerCrop()
                .into(postHolder.userPostImage);
        return row;
    }

    public void post_reaction(int setstate, String user_id, String post_id) {

        try {
            RequestParams params = new RequestParams();
            params.put("state", setstate);
            params.put("user_id", user_id);
            params.put("post_id", post_id);
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            client.get("http://indianroute.roms4all.com/post_reaction.php", params, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            Log.d("on success", "" + res);

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                            Toast.makeText(getContext(), "" + res, Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        } catch (Exception e) {
            Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void likeNotification(String user_id, String post_id) {
        try {
            RequestParams params = new RequestParams();
            params.put("user_id", user_id);
            params.put("post_id", post_id);
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            client.get("http://indianroute.roms4all.com/like_push_notification.php", params, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            Log.d("on success", "" + res);

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                            Toast.makeText(getContext(), "" + res, Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        } catch (Exception e) {
            Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    static class PostHolder {
        TextView username, reaction, comment, no_of_reactions, no_of_comments;
        ImageView userPostImage;
        com.makeramen.roundedimageview.RoundedImageView userprofilePicture;

    }
}


