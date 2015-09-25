package com.shareyourself.base;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shareyourself.share_volley.VolleyNetwork;
import com.shareyourself.share_volley.VolleyRequest;

import org.json.JSONObject;

import java.util.HashMap;


public class GcmSender
{

    public static final String API_KEY = "AIzaSyBwYFFQYhqRAS6yPrXzfvM3kH8-BCx6V_U";
    public static final String TAG = "GcmSender";

    public void sendToGCM(Context context, JSONObject payload)
    {
        try {
            // Prepare JSON containing the GCM message content. What to send and where to send.
            JSONObject jGcmData = new JSONObject();
//            JSONObject jData = new JSONObject();
//            jData.put("name", name);
//            jData.put("lat", latitude);
//            jData.put("long", longitude);
            jGcmData.put("to", "/topics/global");
            jGcmData.put("data", payload);

            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Authorization", "key=" + API_KEY);
            headers.put("Content-Type", "application/json");

            // Create connection to send GCM Message request.
            VolleyRequest<JSONObject> volleyRequest = new VolleyRequest<JSONObject>(Request.Method.POST, "https://gcm-http.googleapis.com/gcm/send", JSONObject.class, headers, null, jGcmData.toString(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i(TAG, "Got volley response");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, error.getMessage());
                }
            });

            VolleyNetwork.getInstance(context.getApplicationContext()).addToRequestQueue(volleyRequest);




//            URL url = new URL("https://gcm-http.googleapis.com/gcm/send");
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestProperty("Authorization", "key=" + API_KEY);
//            conn.setRequestProperty("Content-Type", "application/json");
//            conn.setRequestMethod("POST");
//            conn.setDoOutput(true);
//
//            // Send GCM message content.
//            OutputStream outputStream = conn.getOutputStream();
//            outputStream.write(jGcmData.toString().getBytes());
//
//            // Read GCM response.
//            InputStream inputStream = conn.getInputStream();
//            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
//            //String resp = IOUtils.toString(inputStream);
//            System.out.println(br.readLine());
//            System.out.println("Check your device/emulator for notification or logcat for " +
//                    "confirmation of the receipt of the GCM message.");
        } catch (Exception e) {
            System.out.println("Unable to send GCM message.");
            e.printStackTrace();
        }
    }
}