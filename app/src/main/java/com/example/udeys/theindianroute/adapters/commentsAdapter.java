package com.example.udeys.theindianroute.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.udeys.theindianroute.R;
import com.example.udeys.theindianroute.helperClasses.comments;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Gitesh on 22-06-2016.
 */
public class commentsAdapter extends ArrayAdapter {
    List list = new ArrayList();


    public commentsAdapter(Context context, int resource) {
        super(context, resource);
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
        final comments comments = (comments) this.getItem(position);
        commentHolder.commentersComments.setText(comments.getComments());
        commentHolder.post_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentHolder.writecomment.getText().toString();

                try {
                    RequestParams params = new RequestParams();
                    params.put("post_id", 3);
                    params.put("user_id", 22);
                    params.put("comment", comment);
                    AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
                    client.get("http://indianroute.roms4all.com/post_comment.php", params, new TextHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, String res) {
                                    Toast.makeText(getContext(), "" + res, Toast.LENGTH_LONG).show();

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
        });
        return row;
    }

    static class commentHolder {
        TextView commentersComments;
        EditText writecomment;
        Button post_comment;
    }
}
