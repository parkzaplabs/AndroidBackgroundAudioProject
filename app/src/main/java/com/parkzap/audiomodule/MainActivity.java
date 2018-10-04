package com.parkzap.audiomodule;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parkzap.library.audiomodule.AudioModule;

import java.util.Locale;


public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private AudioModule mAudioModule;

    private TextToSpeech tts;
    private Button mButton;
    private EditText mEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAudioModule = new AudioModule(this);

        tts = new TextToSpeech(this, this);
        mButton = findViewById(R.id.mButton);
        mEditText = findViewById(R.id.mEditText);


        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                speakOut();
            }

        });

    }


    public void playMusic(View view) {

        mAudioModule.playMusic(); //Service/Music is started
    }

    public void pauseMusic(View view) {

        mAudioModule.pauseMusic();  //Service/Music is paused
    }

    public void stopMusic(View view) {

        mAudioModule.stopMusic();  //Service/Music is stopped
    }

    public void onSpeak(View view) {

    }


    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {

                speakOut();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    private void speakOut() {

        String text = mEditText.getText().toString();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}
