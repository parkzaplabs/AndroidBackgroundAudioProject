package com.parkzap.library.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompatBase;
import android.util.Log;
import android.widget.RemoteViews;

import com.parkzap.library.R;

import java.util.List;


public class BackgroundSoundService extends Service {


    private final static String NOTIFICATION_CHANNEL_ID = "1";
    private static final String NOTIFICATION_CHANNEL_NAME = "webservice";
    private static final String TAG = "TAG";
    public static MediaPlayer mMediaPlayer;

    public IBinder onBind(Intent arg0) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        // Initialising MediaPlayer and setting MP3 from Raw Directory

        mMediaPlayer = MediaPlayer.create(this, R.raw.classical_flute);
        mMediaPlayer.setLooping(true); // Set looping of MP3
        mMediaPlayer.setVolume(70, 70); // Set default volume

        // MP3 Link: https://www.zedge.net/ringtone/b023aace-fa15-303b-9c74-8ff06fd3cc4e

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


//        PackageManager pm = getPackageManager();
//        pm.getLaunchIntentForPackage(getPackageName()).getClass();
//        startActivity(intent);

        mMediaPlayer.start(); // Starts MediaPlayer
        Intent notificationIntent = new Intent(this, getLauncherActivityClass());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Print Server running..")
                .setContentText("")
                .setTicker("")
                .setContentIntent(pendingIntent);
        Notification notification = builder.build();
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        startForeground(Integer.parseInt(NOTIFICATION_CHANNEL_ID), notification);
        return START_STICKY;


//        startForegroundService();
//        return super.onStartCommand(intent, flags, startId);


//        mMediaPlayer.start(); // Starts MediaPlayer
//        RemoteViews notificationView = new RemoteViews(getPackageName(),
//                R.layout.custom_notfication);
//
//        Intent notificationIntent = new Intent(getApplicationContext(), BackgroundSoundService.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
//                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        Notification notification = new NotificationCompat.Builder(this)
//                .setCustomBigContentView(notificationView)
//                .setContentIntent(pendingIntent)
//                .build();
//
//        startForeground(001, notification);
//        return START_STICKY;


    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TO DO
    }

    public IBinder onUnBind(Intent arg0) {
        // TO DO Auto-generated method
        return null;
    }


    @Override
    public void onDestroy() {
        // Stopping MediaPlayer for playing Music
        mMediaPlayer.stop();
        mMediaPlayer.release();
        stopForeground(true);
    }

    @Override
    public void onLowMemory() {

    }

    private void startForegroundService() {
        Log.d("TAG_FOREGROUND_SERVICE", "Start foreground service.");

        mMediaPlayer.start(); // Starts MediaPlayer

        // Create notification default intent.
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Create notification builder.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // Make notification show big text.
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Music player implemented by foreground service.");
        bigTextStyle.bigText("Android foreground service is a android service which can run in foreground always, it can be controlled by user via notification.");
        // Set big text style.
        builder.setStyle(bigTextStyle);

        builder.setWhen(System.currentTimeMillis());
//        builder.setSmallIcon(R.mipmap.ic_launcher);
//        Bitmap largeIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_music_32);
//        builder.setLargeIcon(largeIconBitmap);

        // Make the notification max priority.
        builder.setPriority(Notification.PRIORITY_MAX);
        // Make head-up notification.
        builder.setFullScreenIntent(pendingIntent, true);

        // Add Play button intent in notification.
        Intent playIntent = new Intent(this, BackgroundSoundService.class);
        playIntent.setAction("ACTION_PAUSE");
        PendingIntent pendingPlayIntent = PendingIntent.getService(this, 0, playIntent, 0);
        NotificationCompat.Action playAction = new NotificationCompat.Action(android.R.drawable.ic_media_play, "Play", pendingPlayIntent);
        builder.addAction(playAction);

        // Add Pause button intent in notification.
        Intent pauseIntent = new Intent(this, BackgroundSoundService.class);
        pauseIntent.setAction("ACTION_PAUSE");
        PendingIntent pendingPrevIntent = PendingIntent.getService(this, 0, pauseIntent, 0);
        NotificationCompat.Action prevAction = new NotificationCompat.Action(android.R.drawable.ic_media_pause, "Pause", pendingPrevIntent);
        builder.addAction(prevAction);

        // Build the notification.
        Notification notification = builder.build();

        // Start foreground service

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, notification);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {

        String NOTIFICATION_CHANNEL_ID = "com.parkzap.library.service";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    private Class getLauncherActivityClass() {
        Class obj = null;
        final PackageManager pm = getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(getPackageName());
        List<ResolveInfo> activityList = pm.queryIntentActivities(intent, 0);
        if (activityList != null) {
            obj = activityList.get(0).getClass();
        }
        return obj;
    }

}
