package com.m2comm.prs2019f.modules.common;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.views.ContentsActivity;

import java.util.List;
import java.util.Map;

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private Globar g;

    public ComponentName getCurrentActivity() {

        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

        ComponentName topActivity = taskInfo.get(0).topActivity;

        return topActivity ;
    }

    // 메시지 수신
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i(TAG, "onMessageReceived");
        this.g = new Globar(this);
        Map<String, String> data = remoteMessage.getData();
        String sid = data.get("sid");
        final String title = data.get("title");
        final String messagae = data.get("message");
        final String tempMsg = "Newly Registered.";

        if (getCurrentActivity().getPackageName().equals(getPackageName())) {
            Bundle bun = new Bundle();

            Intent popupIntent = new Intent(getApplicationContext(), ServiceAlertActivity.class);
            popupIntent.putExtra("title", title);
            if (messagae != null && messagae.equals("")) {
                popupIntent.putExtra("content", tempMsg);
            } else {
                popupIntent.putExtra("content", messagae);
            }
            popupIntent.putExtra("paramUrl", this.g.urls.get("detailNoti") + "&sid=" + sid);
            popupIntent.putExtras(bun);
            PendingIntent pie= PendingIntent.getActivity(getApplicationContext(), 0, popupIntent, PendingIntent.FLAG_ONE_SHOT);
            try {
                pie.send();
            } catch (PendingIntent.CanceledException e) {

                Log.d("serviceError",e.toString());
            }

        } else {
            sendNotification(title, messagae , sid);
        }


    }

    private void sendNotification(String title, String message , String sid) {
        //O버전이상 은 채널등록을 해줘야 푸시가 간다....
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getResources().getString(R.string.channel_name), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(this, ContentsActivity.class);
        intent.putExtra("paramUrl", this.g.urls.get("detailNoti") + "&sid=" + sid);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, getResources().getString(R.string.channel_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setSound(defaultSoundUri)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, mBuilder.build());
    }

}
