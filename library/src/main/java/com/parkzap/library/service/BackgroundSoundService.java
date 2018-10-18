package com.parkzap.library.service;

import android.annotation.TargetApi;
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
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
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

    /**
     * Static string variables for media palayers actions
     */
    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_PAUSE = "action_pause";
    public static final String ACTION_REWIND = "action_rewind";
    public static final String ACTION_FAST_FORWARD = "action_fast_foward";
    public static final String ACTION_NEXT = "action_next";
    public static final String ACTION_PREVIOUS = "action_previous";
    public static final String ACTION_STOP = "action_stop";

    private MediaSessionManager mManager;
    private MediaSession mSession;
    private MediaController mController;
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

    private void handleIntent(Intent intent) {
        if (intent == null || intent.getAction() == null)
            return;

        String action = intent.getAction();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (action.equalsIgnoreCase(ACTION_PLAY)) {
                mController.getTransportControls().play();
            } else if (action.equalsIgnoreCase(ACTION_PAUSE)) {
                mController.getTransportControls().pause();
            } else if (action.equalsIgnoreCase(ACTION_FAST_FORWARD)) {
                mController.getTransportControls().fastForward();
            } else if (action.equalsIgnoreCase(ACTION_REWIND)) {
                mController.getTransportControls().rewind();
            } else if (action.equalsIgnoreCase(ACTION_PREVIOUS)) {
                mController.getTransportControls().skipToPrevious();
            } else if (action.equalsIgnoreCase(ACTION_NEXT)) {
                mController.getTransportControls().skipToNext();
            } else if (action.equalsIgnoreCase(ACTION_STOP)) {
                mController.getTransportControls().stop();
            }
        }


    }


    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    private Notification.Action generateAction(int icon, String title, String intentAction) {
        Intent intent = new Intent(getApplicationContext(), BackgroundSoundService.class);
        intent.setAction(intentAction);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
        return new Notification.Action.Builder(icon, title, pendingIntent).build();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void buildNotification(Notification.Action action ) {
        Notification.MediaStyle style = new Notification.MediaStyle();

        Intent intent = new Intent( getApplicationContext(), BackgroundSoundService.class );
        intent.setAction( ACTION_STOP );
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
        Notification.Builder builder = new Notification.Builder( this )
                .setSmallIcon(R.drawable.ic_stop)
                .setContentTitle( "Media Title" )
                .setContentText( "Media Artist" )
                .setDeleteIntent( pendingIntent )
                .setStyle(style);

        builder.addAction( generateAction( android.R.drawable.ic_media_previous, "Previous", ACTION_PREVIOUS ) );
        builder.addAction( generateAction( android.R.drawable.ic_media_rew, "Rewind", ACTION_REWIND ) );
        builder.addAction( action );
        builder.addAction( generateAction( android.R.drawable.ic_media_ff, "Fast Foward", ACTION_FAST_FORWARD ) );
        builder.addAction( generateAction( android.R.drawable.ic_media_next, "Next", ACTION_NEXT ) );
        style.setShowActionsInCompactView(0,1,2,3,4);

        NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
        notificationManager.notify( 1, builder.build() );
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
