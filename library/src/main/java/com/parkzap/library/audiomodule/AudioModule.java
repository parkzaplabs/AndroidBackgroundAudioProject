package com.parkzap.library.audiomodule;

import android.content.Context;
import android.content.Intent;

import com.parkzap.library.service.BackgroundSoundService;

public class AudioModule {

    private Context context;

    public AudioModule(Context context) {
        this.context = context;
    }

    public void playMusic() {

        Intent svc = new Intent(context, BackgroundSoundService.class);
        context.startService(svc);
    }

    public void pauseMusic() {

        Intent svc = new Intent(context, BackgroundSoundService.class);
        context.stopService(svc);
    }
}
