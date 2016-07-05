package com.example.udeys.theindianroute.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.udeys.theindianroute.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Gitesh on 04-07-2016.
 */
public class ViewPostFragment extends Fragment {
    View view;
    String posts_id, username, profile_pic, post_pic, story, check_in, reaction;
    com.makeramen.roundedimageview.RoundedImageView pp;
    ImageView post;
    TextView user_name, post_story, like, comment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.viewpostfragment, container, false);
        posts_id = "1";
        extractData();


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pp = (com.makeramen.roundedimageview.RoundedImageView) view.findViewById(R.id.vpuserProfilePicture);
        post = (ImageView) view.findViewById(R.id.vpuserpostimage);
        user_name = (TextView) view.findViewById(R.id.vpusername);
        Picasso.with(getActivity()).load(profile_pic).into(pp);
        Picasso.with(getActivity()).load(post_pic).into(post);
        user_name.setText(username);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    public void extractData() {
        try {
            RequestParams params = new RequestParams();
            params.put("post_id", posts_id);
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            client.post("http://indianroute.roms4all.com/view_post.php", params, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            jsonExtract(res);

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                            Toast.makeText(getActivity(), "" + res, Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        } catch (Exception e) {
            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void jsonExtract(String res) {
        try {
            JSONArray jArr = new JSONArray(res);

            for (int count = 0; count < jArr.length(); count++) {
                JSONObject obj = jArr.getJSONObject(count);
                username = obj.getString("username");
                profile_pic = obj.getString("picture");
                post_pic = obj.getString("post_picture");
                check_in = obj.getString("check_in");
                story = obj.getString("story");
                reaction = obj.getString("reaction_no");
                Log.d("pic",""+profile_pic);
            }
            Picasso.with(getActivity()).load(profile_pic).into(pp);
            Picasso.with(getActivity()).load(post_pic).resize(320, 240).into(post);
            user_name.setText(username);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
