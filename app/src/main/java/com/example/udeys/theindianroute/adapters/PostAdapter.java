package com.example.udeys.theindianroute.adapters;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.udeys.theindianroute.CommentActivity;
import com.example.udeys.theindianroute.CommonList;
import com.example.udeys.theindianroute.R;
import com.example.udeys.theindianroute.fragments.ProfileFragment;
import com.example.udeys.theindianroute.helperClasses.posts;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Gitesh on 14-06-2016.
 */
public class PostAdapter extends ArrayAdapter {
    public static String user_id;
    static int state;
    List list = new ArrayList();
    Typeface samarn, fa;
    PostHolder postHolder;

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


        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.timelinerowlayout, parent, false);
            postHolder = new PostHolder();
            postHolder.username = (TextView) row.findViewById(R.id.username);
            postHolder.story = (TextView) row.findViewById(R.id.post_story);
            postHolder.reaction = (ImageView) row.findViewById(R.id.icon_like);
            postHolder.comment = (ImageView) row.findViewById(R.id.icon_comment);
            postHolder.no_of_reactions = (TextView) row.findViewById(R.id.likes);
            postHolder.no_of_comments = (TextView) row.findViewById(R.id.comments);
            postHolder.userPostImage = (ImageView) row.findViewById(R.id.userpostimage);
            postHolder.userprofilePicture = (RoundedImageView) row.findViewById(R.id.userProfilePicture);
            postHolder.post_time = (TextView) row.findViewById(R.id.post_time);

            row.setTag(postHolder);
        } else {
            postHolder = (PostHolder) row.getTag();
        }

        final posts Posts = (com.example.udeys.theindianroute.helperClasses.posts) this.getItem(position);
        state = Posts.getstate();
        user_id = Posts.getUser_id();
        postHolder.username.setTypeface(samarn);
        postHolder.username.setText(Posts.getUsername());
        postHolder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ProfileFragment fragment = new ProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putString("user_name", Posts.getUsername());
                bundle.putString("user_id", Posts.getUser_id());
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_1, fragment);
                fragmentTransaction.commit();
            }
        });
        postHolder.story.setText(Posts.getStory());
        postHolder.no_of_comments.setText(String.valueOf(Posts.getComment()));

        if (state == 1) {
            postHolder.reaction.setImageResource(R.drawable.liked);
        } else {
            postHolder.reaction.setImageResource(R.drawable.not_liked);
        }
        postHolder.reaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == 0) {
                    postHolder.reaction.setImageResource(R.drawable.liked);
                    state = 1;
                } else {
                    postHolder.reaction.setImageResource(R.drawable.not_liked);
                    state = 0;
                }
                post_reaction(state, user_id, Posts.getPost_id());

            }
        });
        postHolder.no_of_reactions.setText(String.valueOf(Posts.getReaction()));
        postHolder.no_of_reactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CommonList.class);
                intent.putExtra("post_id",Posts.getPost_id());
                intent.putExtra("type","p");
                intent.putExtra("list_type","likes");
                getContext().startActivity(intent);
            }
        });
        postHolder.comment.setImageResource(R.drawable.comment);
        postHolder.comment.setFocusableInTouchMode(false);
        postHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CommentActivity.class);
                intent.putExtra("username", Posts.getUsername());
                intent.putExtra("post_id", Posts.getPost_id());
                getContext().startActivity(intent);
            }
        });
        postHolder.post_time.setText(post_time(Posts.getPost_time()));
        String user_pp = Posts.getUserProfilePicture();

        if (user_pp.equals("http://theindianroute.net/uploads/users_profile_pictures/"))
            Picasso.with(getContext()).load(R.drawable.dummyprofile).placeholder(R.drawable.ppplaceholder)
                    .into(postHolder.userprofilePicture);

        else
            Picasso.with(getContext()).load(Posts.getUserProfilePicture()).placeholder(R.drawable.ppplaceholder)
                .into(postHolder.userprofilePicture);
        Picasso.with(getContext())
                .load(Posts.getPictue())
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
            client.get("http://theindianroute.net/post_reaction.php", params, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            decodeJson(res);
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

    private void decodeJson(String result) {
        try {
            JSONArray jArr = new JSONArray(result);
            JSONObject obj = jArr.getJSONObject(0);
            String num = obj.getString("reaction");
            postHolder.no_of_reactions.setText(num);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String post_time(String post_time) {
        String time = "";
        String start;
        start = post_time;
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        Date d1 = null;
        Date d2 = null;
        long diffHours = 0;

        try {
            d1 = format.parse(start);
            d2 = format.parse(timeStamp);
        } catch (Exception e) {
            Log.e("date catch", e.toString());
        }

        long diff = 0;
        try {
            diff = d2.getTime() - d1.getTime();
        } catch (Exception e) {
            Log.e("catch", e.toString());
        }

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = 0;
        long elapsedHours = 0;
        long elapsedMinutes = 0;
        long elapsedSeconds = 0;

        elapsedDays = diff / daysInMilli;
        diff = diff % daysInMilli;

        elapsedHours = diff / hoursInMilli;
        diff = diff % hoursInMilli;

        elapsedMinutes = diff / minutesInMilli;
        diff = diff % minutesInMilli;

        elapsedSeconds = diff / secondsInMilli;

        if (elapsedDays == 0 && elapsedHours == 0 && elapsedMinutes == 0) {
            time = String.valueOf(elapsedSeconds);
            time += "secs";
        } else if (elapsedDays == 0 && elapsedHours == 0 && elapsedMinutes > 0) {
            time = String.valueOf(elapsedMinutes);
            time += "mins";
        } else if (elapsedDays == 0 && elapsedHours > 0) {
            time = String.valueOf(elapsedHours);
            time += "hrs";
        } else if (elapsedDays > 0) {
            time = String.valueOf(elapsedDays);
            time += "d";
        }

        return time;
    }


    static class PostHolder {
        TextView username, no_of_reactions, no_of_comments, story, post_time;
        ImageView userPostImage,reaction,comment;
        com.makeramen.roundedimageview.RoundedImageView userprofilePicture;
        ProgressBar progressBar;

    }


}


