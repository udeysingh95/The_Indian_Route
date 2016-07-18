package com.example.udeys.theindianroute.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.udeys.theindianroute.R;
import com.example.udeys.theindianroute.adapters.commentsAdapter;
import com.example.udeys.theindianroute.helperClasses.comments;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Gitesh on 22-06-2016.
 */
public class CommentFragment extends Fragment {
    View view;
    Button post_comment;
    EditText write_comment;
    TextView user;
    commentsAdapter commentsAdapter;
    ListView commnetslists;
    String post_id, username;
    ImageView post_image, pp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        post_id = bundle.getString("post_id", null);
        username = bundle.getString("username", null);

        view = inflater.inflate(R.layout.commentfragment, container, false);

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        post_comment = (Button) view.findViewById(R.id.post_comment);
        write_comment = (EditText) view.findViewById(R.id.write_comment);
        requestComments();

        commnetslists = (ListView) view.findViewById(R.id.comments_lists);

        commentsAdapter = new commentsAdapter(getActivity(), R.layout.commentrowlayout, post_id);

        commnetslists.setAdapter(commentsAdapter);
        commnetslists.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        post_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String co = write_comment.getText().toString();
                write_comment.setText("");
                comments comments = new comments(co);
                commentsAdapter.add(comments);
                commentsAdapter.notifyDataSetChanged();


                try {
                    RequestParams params = new RequestParams();
                    params.put("post_id", post_id);
                    params.put("user_id", HomeFragment.user_id);
                    params.put("comment", co);
                    AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
                    client.get("http://indianroute.roms4all.com/post_comment.php", params, new TextHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, String res) {
                                    Log.d("success", "" + res);

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
                commentNotification(HomeFragment.user_id, post_id);

            }
        });


    }


    public void requestComments() {
        try {
            RequestParams params = new RequestParams();
            params.put("post_id", post_id);
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            client.post("http://indianroute.roms4all.com/fetch_comment.php", params, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            decodeCommentsJSON(res);

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

    public void decodeCommentsJSON(String res) {
        try {
            JSONArray jArr = new JSONArray(res);
            String commentersUsername, comment;
            for (int count = 0; count < jArr.length(); count++) {
                JSONObject obj = jArr.getJSONObject(count);
                comment = obj.getString("comment");
                comments Comments = new comments(comment);
                commentsAdapter.add(Comments);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void commentNotification(String user_id, String post_id) {
        try {
            RequestParams params = new RequestParams();
            params.put("user_id", user_id);
            params.put("post_id", post_id);
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            client.get("http://indianroute.roms4all.com/comment_push_notification.php", params, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            Log.d("on success", "" + res);

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

}
