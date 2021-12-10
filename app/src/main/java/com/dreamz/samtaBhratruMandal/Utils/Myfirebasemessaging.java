package com.dreamz.samtaBhratruMandal.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.dreamz.samtaBhratruMandal.Activities.MainActivity;
import com.dreamz.samtaBhratruMandal.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

import static com.dreamz.samtaBhratruMandal.Server.serverConstants.AddNotificationToServer;


public class Myfirebasemessaging extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        UserSessionManager session = new UserSessionManager(this);
        HashMap<String, Object> user = session.getUserDetails();
        //int userid = Integer.parseInt(String.valueOf(user.get(UserSessionManager.USERID)));

        /*try {
            //sendNotificationToServer(userid,remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        } catch (UnsupportedEncodingException | JSONException e) {
            e.printStackTrace();
        }*/

    }

    private void sendNotificationToServer(Integer userid, String title, String body) throws UnsupportedEncodingException, JSONException {

//            ShowLoader showLoader = ShowLoader.getProgressDialog(Myfirebasemessaging.this);
        AsyncHttpClient client = new AsyncHttpClient();
        // send parameters in json body
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("userId", userid);
        jsonParams.put("title", title);
        jsonParams.put("message", body);
        StringEntity entity = new StringEntity(jsonParams.toString());
//            showLoader.show();
        client.post(this, AddNotificationToServer, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
//
                try {
                    if (response.getString("result").equals("Success")) {

                        Toast.makeText(getApplicationContext(), response.getString("result").toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), response.getString("message").toString(), Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(), response.getString("message").toString(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);


            }
        });


        //TODO Call Api and pass title and bodyto api and get all notification list into notification fragment
    }

   /* public void getFirebaseMessage(String title, String msg) {

        PendingIntent pendingIntent
                = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notification_channel")
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentTitle(title)
                .setContentText(msg).setAutoCancel(true);
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(101, builder.build());
    }*/

    private RemoteViews getCustomDesign(String title,
                                        String message) {
        RemoteViews remoteViews = new RemoteViews(
                getApplicationContext().getPackageName(),
                R.layout.notification);

        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.message, message);
        remoteViews.setImageViewResource(R.id.icon,
                R.drawable.samta_logo);
        return remoteViews;
    }

    public void showNotification(String title,
                                 String message) {
        // Pass the intent to switch to the MainActivity
        Intent intent
                = new Intent(this, MainActivity.class);
        // Assign channel ID
        String channel_id = "notification_channel";
        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
        // the activities present in the activity stack,
        // on the top of the Activity that is to be launched
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Pass the intent to PendingIntent to start the
        // next Activity
        PendingIntent pendingIntent
                = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
        NotificationCompat.Builder builder
                = new NotificationCompat
                .Builder(getApplicationContext(),
                channel_id)
                .setSmallIcon(R.drawable.samta_logo)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000,
                        1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        // A customized design for the notification can be
        // set only for Android versions 4.1 and above. Thus
        // condition for the same is checked here.
        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.JELLY_BEAN) {
            /*builder = builder.setContent(
                    getCustomDesign(title, message));*/
            builder = builder.setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(message))
                    .setContentText(message)
                    .setSmallIcon(R.drawable.samta_logo);
        } // If Android Version is lower than Jelly Beans,
        // customized layout cannot be used and thus the
        // layout is set as follows
        else {
            //NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            //bigText.bigText(message);
            builder = builder.setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(message))
                    .setContentText(message)
                    .setSmallIcon(R.drawable.samta_logo);
        }
        // Create an object of NotificationManager class to
        // notify the
        // user of events that happen in the background.
        NotificationManager notificationManager
                = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel
                    = new NotificationChannel(
                    channel_id, "web_app",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(
                    notificationChannel);
        }

        notificationManager.notify(0, builder.build());
    }

}
