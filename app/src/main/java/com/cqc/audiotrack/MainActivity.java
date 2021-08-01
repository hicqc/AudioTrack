package com.cqc.audiotrack;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioTrack;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cqc.audiotrack.controller.AudioTrackController;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button button_play;
    AudioTrackController audioTrackController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_play = findViewById(R.id.button_play);

        button_play.setOnClickListener(this);
        initAudioTrackPlayer();
    }

    private void initAudioTrackPlayer() {

    }


    @Override
    public void onClick(View v) {

    }
}