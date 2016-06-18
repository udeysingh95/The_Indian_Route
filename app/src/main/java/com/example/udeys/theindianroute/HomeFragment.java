package com.example.udeys.theindianroute;

/**
 * Created by udeys on 6/17/2016.
 */

import android.app.Fragment;
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
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    PostAdapter PostAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    ListView datalist;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.homefragment, container, false);
        datalist = (ListView) view.findViewById(R.id.users_posts);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
            AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
            client.get("http://indianroute.roms4all.com/fetch_post.php", new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String res) {
                            decodeJson(res);

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
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
            String username, story, picture, check_in, userprofilePicture;
            for (int count = 0; count < jArr.length(); count++) {
                JSONObject obj = jArr.getJSONObject(count);
                username = obj.getString("username");
                story = obj.getString("story");
                picture = obj.getString("picture");
                check_in = obj.getString("check_in");
                userprofilePicture = obj.getString("userprofilePicture");
                posts posts = new posts(username, story, picture, check_in, userprofilePicture);
                swipeRefreshLayout.setRefreshing(false);
                PostAdapter.add(posts);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}