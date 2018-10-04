package com.parkzap.library.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.parkzap.library.R;


public class BackgroundSoundService extends Service {

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
        mMediaPlayer.start(); // Starts MediaPlayer

        Intent notificationIntent = new Intent(this,BackgroundSoundService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Parkzap")
                .setContentText("Doing some work...")
                .setContentIntent(pendingIntent)
                .build();

        startForeground(001, notification);
        return START_STICKY;
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
}
