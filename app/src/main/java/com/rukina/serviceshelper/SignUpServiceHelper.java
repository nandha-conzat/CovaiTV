package com.rukina.serviceshelper;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.rukina.R;
import com.rukina.activity.MainActivity;
import com.rukina.app.AppController;
import com.rukina.serviceinterfaces.IServiceListener;
import com.rukina.serviceinterfaces.ISignUpServiceListener;
import com.rukina.utils.CovaiTVConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by zahid.r on 10/30/2015.
 */
public class SignUpServiceHelper {
    private String TAG = MainActivity.class.getSimpleName();
    private Context context;
    ISignUpServiceListener signUpServiceListener;


    public SignUpServiceHelper(Context context) {
        this.context = context;
    }

    public void setSignUpServiceListener(ISignUpServiceListener signUpServiceListener) {
        this.signUpServiceListener = signUpServiceListener;
    }

    public void updateUserProfile(String url, final IServiceListener listener){
        Log.d(TAG,"updateprofile URL"+ url);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, (String) null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            //Parse the response and convert to Java class
                            listener.onSuccess(0,response);

                        }catch(Exception e){
                            Log.d(TAG,"Exception while parsing");
                            e.printStackTrace();
                            listener.onError("JSON Parser error");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.data != null) {

                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        JSONObject jsonObject = new JSONObject(responseBody);
                        listener.onError(jsonObject.getString(CovaiTVConstants.PARAM_MESSAGE));
                    } catch (UnsupportedEncodingException e) {
                        listener.onError(context.getResources().getString(R.string.error_occured));
                        e.printStackTrace();
                    } catch (JSONException e) {
                        listener.onError(context.getResources().getString(R.string.error_occured));
                        e.printStackTrace();
                    }

                } else {
                    listener.onError(context.getResources().getString(R.string.error_occured));
                }
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

}
