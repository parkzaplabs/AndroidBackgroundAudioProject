package com.parkzap.audiomodule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.parkzap.library.audiomodule.AudioModule;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void playMusic(View view) {

        AudioModule module = new AudioModule(this);
        module.playMusic();
    }

    public void pauseMusic(View view) {

        AudioModule module = new AudioModule(this);
        module.pauseMusic();
    }
}
