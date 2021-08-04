package com.cqc.audiotrack;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioTrack;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cqc.audiotrack.controller.AudioTrackController;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    Button button_play;
    AudioTrackController audioTrackController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_play = findViewById(R.id.button_play);
        button_play.setOnClickListener(this);
        initAudioTrackPlayer(this); // AudioTrack is create
    }


    private void initAudioTrackPlayer(Context context) {
        audioTrackController = new AudioTrackController(context);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_play:
                Log.i(TAG,"PRE PLAY");
                audioTrackController.play();
                break;
            default:
                break;
        }

    }
}