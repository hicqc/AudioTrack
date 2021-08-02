package com.cqc.audiotrack.controller;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.cqc.audiotrack.R;

import java.io.BufferedInputStream;
import java.io.InputStream;


@RequiresApi(api = Build.VERSION_CODES.M)
public class AudioTrackController {
    AudioTrack audioTrack;
    Context context;
    BufferedInputStream bufferedInputStream;

    public AudioTrackController(Context context) {
        initAudioTrack();
        this.context = context;
    }

    public void initAudioTrack() {
        int minBufferSize = AudioTrack.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);
        /**
         * 该构造器已经被弃用
         * AudioTrack(int streamType, int sampleRateInHz, int channelConfig,int audioFormat, int bufferSizeInBytes, int mode)
         * AudioTrack(int streamType, int sampleRateInHz, int channelConfig, int audioFormat, int bufferSizeInBytes, int mode, int sessionId)
         *
         * 唯一的构造器：
         * AudioTrack(AudioAttributes attributes, AudioFormat format, int bufferSizeInBytes, int mode, int sessionId)
         *
         */
        audioTrack = new AudioTrack.Builder()
                .setAudioAttributes(new AudioAttributes.Builder()   //用于封装描述有关音频流的信息的属性集合的类.
                        .setUsage(AudioAttributes.USAGE_MEDIA)  //set声音用途
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC) //set播放内容的类型
                        .build())
                .setAudioFormat(new AudioFormat.Builder()   //用于访问多个音频格式和信道配置常量。
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)    //采样位深
                        .setSampleRate(48000)   //采样率
                        .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)     //信道掩码
                        .build())
                .setTransferMode(AudioTrack.MODE_STREAM)
                .setBufferSizeInBytes(minBufferSize)
                .build();
    }


    public void play() {
        loadFile();
        new Runnable() {
            @Override
            public void run() {
                //TODO
            }
        };
    }

    private void loadFile() {
        InputStream inputStream = context.getResources().openRawResource(R.raw.music);
        bufferedInputStream = new BufferedInputStream(inputStream);
    }
}
