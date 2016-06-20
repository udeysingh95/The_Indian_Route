package com.example.udeys.theindianroute;

/**
 * Created by udeys on 6/17/2016.
 */

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.udeys.theindianroute.utils.PostAdapter;
import com.example.udeys.theindianroute.utils.posts;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    SharedPreferences sp = this.getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
    String username = sp.getString("username", null);
    String user_id = sp.getString("user_id", null);

    PostAdapter PostAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    ListView datalist;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.homefragment, container, false);
        return view;
    }

    /*
    * fragment is inflated after activity's layout is created
    * */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        datalist = (ListView) view.findViewById(R.id.users_posts);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        datalist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (datalist.getChildAt(0) != null) {
                    swipeRefreshLayout.setEnabled(datalist.getFirstVisiblePosition() == 0 && datalist.getChildAt(0).getTop() == 0);
                }
            }
        });
        Typeface cFont = Typeface.createFromAsset(getActivity().getAssets(), "SAMARN__.TTF");
        Typeface fontAwesome = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");
        PostAdapter = new PostAdapter(getActivity(), R.layout.timelinerowlayout, cFont, fontAwesome);
        datalist.setAdapter(PostAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        bindData();
                                    }
                                }
        );

    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        bindData();
    }


    public void bindData() {

        try {
            RequestParams params = new RequestParams();
            params.put("user_id",user_id);
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            client.get("http://indianroute.roms4all.com/fetch_post.php",params, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            decodeJson(res);

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

    private void decodeJson(String result) {
        try {
            swipeRefreshLayout.setRefreshing(true);
            JSONArray jArr = new JSONArray(result);
            String username;
            String story;
            String picture;
            String check_in;
            String userprofilePicture;
            String post_id,user_id;
            int reaction,state,comment;
            for (int count = 0; count < jArr.length(); count++) {
                JSONObject obj = jArr.getJSONObject(count);
                username = obj.getString("username");
                story = obj.getString("story");
                picture = obj.getString("picture");
                check_in = obj.getString("check_in");
                userprofilePicture = obj.getString("userprofilePicture");
                post_id = obj.getString("post_id");
                reaction = Integer.valueOf(obj.getString("reaction"));
                state = Integer.valueOf(obj.getString("state"));
                comment = Integer.valueOf(obj.getString("comment"));
                user_id = obj.getString("user_id");
                posts posts = new posts(username, story, picture, check_in, userprofilePicture,post_id,reaction,state,comment,user_id);
                swipeRefreshLayout.setRefreshing(false);
                PostAdapter.add(posts);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}