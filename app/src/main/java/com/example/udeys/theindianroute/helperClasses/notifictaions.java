package com.example.udeys.theindianroute.helperClasses;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

/**
 * Created by Gitesh on 30-06-2016.
 */
public class notifictaions extends AsyncHttpClient {
    @Override
    public RequestHandle post(String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        return super.post(url, params, responseHandler);
    }
}
