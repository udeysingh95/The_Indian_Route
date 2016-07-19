package com.example.udeys.theindianroute.adapters;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.udeys.theindianroute.R;
import com.example.udeys.theindianroute.fragments.CommentFragment;
import com.example.udeys.theindianroute.helperClasses.posts;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

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
            postHolder.story_username = (TextView) row.findViewById(R.id.story_username);
            postHolder.story = (TextView) row.findViewById(R.id.post_story);
            postHolder.reaction = (TextView) row.findViewById(R.id.icon_like);
            postHolder.comment = (TextView) row.findViewById(R.id.icon_comment);
            postHolder.no_of_reactions = (TextView) row.findViewById(R.id.likes);
            postHolder.no_of_comments = (TextView) row.findViewById(R.id.comments);
            postHolder.userPostImage = (ImageView) row.findViewById(R.id.userpostimage);
            postHolder.userprofilePicture = (RoundedImageView) row.findViewById(R.id.userProfilePicture);
            postHolder.post_time = (TextView) row.findViewById(R.id.post_time);
            postHolder.progressBar = (ProgressBar) row.findViewById(R.id.loader);
            row.setTag(postHolder);
        } else {
            postHolder = (PostHolder) row.getTag();
        }

        final posts Posts = (com.example.udeys.theindianroute.helperClasses.posts) this.getItem(position);
        state = Posts.getstate();
        user_id = Posts.getUser_id();
        postHolder.progressBar.setVisibility(View.VISIBLE);
        postHolder.username.setTypeface(samarn);
        postHolder.username.setText(Posts.getUsername());
        postHolder.story_username.setText(Posts.getUsername());
        postHolder.story.setText(Posts.getStory());
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
                    state = 1;
                } else {
                    postHolder.reaction.setText(R.string.icon_heart_empty);
                    postHolder.reaction.setTextColor(Color.BLACK);
                    state = 0;
                }
                post_reaction(state, user_id, Posts.getPost_id());

            }
        });
        postHolder.no_of_reactions.setText(String.valueOf(Posts.getReaction()));
        postHolder.comment.setTypeface(fa);
        postHolder.comment.setTextSize(30);
        postHolder.comment.setFocusableInTouchMode(false);
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
        postHolder.post_time.setText(post_time(Posts.getPost_time()));
        Picasso.with(getContext())
                .load(Posts.getUserProfilePicture()).placeholder(R.drawable.ppplaceholder)
                .into(postHolder.userprofilePicture);
        Picasso.with(getContext())
                .load(Posts.getPictue())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        try {
                            Log.e("loaded", "success");
                            scaleImage(postHolder.userPostImage, bitmap);
                        } catch (Exception e) {
                            Log.e("pa catch", e.toString());
                        }
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
        postHolder.progressBar.setVisibility(View.GONE);
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

            }catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String post_time(String post_time) {
        String time;
        String start;
        start = post_time;
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date());
        Log.e("timestamp", timeStamp);
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
        // Get msec from each, and subtract.
        long diff = 0;
        try {
            diff = d2.getTime() - d1.getTime();
        } catch (Exception e) {
            Log.e("catch", e.toString());
        }

        if (diff < 60) {
            time = Long.toString(diff / 1000 % 60);  //<60 print this value.
            time += " seconds ago";
            Log.d("sec", "" + time);
        } else if (diff < 3600) {
            time = Long.toString(diff / (60 * 1000) % 60); // <3600 print this value
            time += " minutes ago";
            Log.d("min", "" + time);
        } else if (diff < 86400) {
            time = Long.toString(diff / (60 * 60 * 1000));
            time += " hours ago";
            Log.d("hr", "" + time);
        } else {
            time = Long.toString((diff / (60 * 60 * 1000)) / 24);
            time += " days ago";
            Log.d("day", "" + time);
        }

        return time;
    }

    private void scaleImage(ImageView view, Bitmap bitmap) throws NoSuchElementException {
        try {
            Drawable drawing = view.getDrawable();
            bitmap = ((BitmapDrawable) drawing).getBitmap();
            Log.e("log", "" + bitmap);
        } catch (NullPointerException e) {
            throw new NoSuchElementException("No drawable on given view");
        } catch (ClassCastException e) {
            // Check bitmap is Ion drawable

        }

        // Get current dimensions AND the desired bounding box
        int width = 0;

        try {
            width = bitmap.getWidth();
            Log.d("width", "" + width);
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Can't find bitmap on given view/drawable");
        }

        int height = bitmap.getHeight();
        int bounding = dpToPx(250);
        Log.e("Test", "original width = " + Integer.toString(width));
        Log.e("Test", "original height = " + Integer.toString(height));
        Log.e("Test", "bounding = " + Integer.toString(bounding));

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;
        Log.e("Test", "xScale = " + Float.toString(xScale));
        Log.e("Test", "yScale = " + Float.toString(yScale));
        Log.e("Test", "scale = " + Float.toString(scale));

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        Log.i("Test", "scaled width = " + Integer.toString(width));
        Log.i("Test", "scaled height = " + Integer.toString(height));

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);

        Log.i("Test", "done");
    }

    private int dpToPx(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    static class PostHolder {
        TextView username, reaction, comment, no_of_reactions, no_of_comments, story, story_username, post_time;
        ImageView userPostImage;
        com.makeramen.roundedimageview.RoundedImageView userprofilePicture;
        ProgressBar progressBar;

    }



}


