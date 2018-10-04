package com.parkzap.library.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.parkzap.library.R;


public class BackgroundSoundService extends Service {
    private static final String TAG = null;
    MediaPlayer player;

    public IBinder onBind(Intent arg0) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialising MediaPlayer and setting MP3 from Raw Directory
        player = MediaPlayer.create(this, R.raw.classical_flute);
        player.setLooping(true); // Set looping of MP3
        player.setVolume(100, 100); // Set default volume

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start(); // Starts MediaPlayer
        return 1;
    }

    public void onStart(Intent intent, int startId) {
        // TO DO
    }

    public IBinder onUnBind(Intent arg0) {
        // TO DO Auto-generated method
        return null;
    }

    public void onStop() {
        // Add code when Service stops
    }

    public void onPause() {
        // Add code when Service paused
    }

    @Override
    public void onDestroy() {
        // Stopping MediaPlayer for playing Music
        player.stop();
        player.release();
    }

    @Override
    public void onLowMemory() {

    }
}
