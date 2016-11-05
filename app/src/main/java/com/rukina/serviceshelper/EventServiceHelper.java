package com.rukina.serviceshelper;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rukina.R;
import com.rukina.activity.MainActivity;
import com.rukina.app.AppController;
import com.rukina.serviceinterfaces.IEventServiceListener;
import com.rukina.utils.CovaiTVConstants;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by zahid.r on 10/30/2015.
 */
public class EventServiceHelper {
    private String TAG = MainActivity.class.getSimpleName();
    private Context context;
    IEventServiceListener eventServiceListener;

    public EventServiceHelper(Context context) {
        this.context = context;
    }

    public void setEventServiceListener(IEventServiceListener eventServiceListener) {
        this.eventServiceListener = eventServiceListener;
    }

    public void makeGetEventServiceCall(String URL) {
        Log.d(TAG, "Events URL" + URL);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                URL, (String) null,
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "ajaz : " + response.toString());
                        if(response != null) {
                            eventServiceListener.onEventResponse(response);
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "error is" + error.getLocalizedMessage());
                if (error.networkResponse != null && error.networkResponse.data != null) {

                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        Log.d(TAG, "error response body is" + responseBody);
                        JSONObject jsonObject = new JSONObject(responseBody);
                        eventServiceListener.onEventError(jsonObject.getString(CovaiTVConstants.PARAM_MESSAGE));
                    } catch (UnsupportedEncodingException e) {
                        eventServiceListener.onEventError(context.getResources().getString(R.string.error_occured));
                        e.printStackTrace();
                    } catch (JSONException e) {
                        eventServiceListener.onEventError(context.getResources().getString(R.string.error_occured));
                        e.printStackTrace();
                    }

                } else {
                    eventServiceListener.onEventError(context.getResources().getString(R.string.error_occured));
                }
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
}
