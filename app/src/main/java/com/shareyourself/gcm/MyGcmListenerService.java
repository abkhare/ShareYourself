/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shareyourself.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.shareyourself.base.StartSharring;
import com.shareyourself.eventbus.LocationEvent;
import com.shareyourself.eventbus.EventBusSingleton;
import com.shareyourself.eventbus.MessageEvent;

import org.json.JSONException;
import org.json.JSONObject;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        try {
            if (data.containsKey("message")) {
                JSONObject messageObject = new JSONObject(data.getString("message"));
                String topicName = messageObject.getString("topicName");
                MessageEvent messageData = new MessageEvent(messageObject.getString("phno"), messageObject.getString("textMessage"), topicName);
                EventBusSingleton.instance().postEvent(messageData);
            }
            else {
                JSONObject locationObject = new JSONObject(data.getString("location"));
                String phno = locationObject.getString("phno");
                String topicName = locationObject.getString("topicName");
                double latitude = locationObject.getDouble("lat");
                double logitude = locationObject.getDouble("long");
                LocationEvent locationEvent = new LocationEvent(topicName, phno, latitude, logitude);
                EventBusSingleton.instance().postEvent(locationEvent);
            }

            if (from.startsWith("/topics/")) {
                // message received from some topic.
            } else {
                // normal downstream message.
            }
            //sendNotification(name);
        }
        catch (JSONException e){
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        Intent intent = new Intent(this, StartSharring.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(com.google.android.gms.R.drawable.common_signin_btn_icon_dark)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
