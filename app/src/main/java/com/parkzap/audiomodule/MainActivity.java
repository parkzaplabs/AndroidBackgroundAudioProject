package com.parkzap.audiomodule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.parkzap.library.audiomodule.AudioModule;

public class MainActivity extends AppCompatActivity {

    private AudioModule module;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        module = new AudioModule(this);

    }


    public void playMusic(View view) {

        module.playMusic(); //Service/Music is started
    }

    public void pauseMusic(View view) {

        module.pauseMusic();  //Service/Music is stopped
    }
}
