package me.ninjachen.util;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;

public final class APIClient {
    private static final String LogTag = "pm25";
    private static HttpClient mHttpClient = new DefaultHttpClient();

    //http post
    public String post(String uri, String param) {
        Log.d(LogTag, String.format("request post message:%s",
                param));
        String result = null;
        HttpPost httpPost = new HttpPost(uri);
        try {
            ArrayList<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
            list.add(new BasicNameValuePair("message", param));
            httpPost.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
            HttpResponse httpResponse = mHttpClient.execute(httpPost);
            Log.d(LogTag, String.format("post StatusCode:%s", httpResponse
                    .getStatusLine().getStatusCode()));
            result = EntityUtils.toString(httpResponse.getEntity());
            Log.d(LogTag, String.format("post HttpResponse:%s",
                    result));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //http-get
    public String request(String param) {
        Log.d(LogTag,
                String.format("request get uri:%s", param));
        HttpGet httpGet = new HttpGet(param);
        try {
            HttpResponse httpResponse = mHttpClient.execute(httpGet);
            Log.d(LogTag, String.format("get StatusCode:%s", httpResponse
                    .getStatusLine().getStatusCode()));
            return EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}