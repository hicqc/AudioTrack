package com.cqc.audiotrack.controller;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.cqc.audiotrack.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.jar.Attributes;


public class AudioTrackController {

    private String TAG = "AudioTrackController";

    AudioTrack audioTrack;
    Context context;
    BufferedInputStream bufferedInputStream;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    int minBufferSize;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    public AudioTrackController(Context context) {
        initAudioTrack();
        this.context = context;
    }



    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void initAudioTrack() {
        AudioFormat audioFormat = new AudioFormat.Builder()   //用于访问多个音频格式和信道配置常量。
                .setEncoding(AudioFormat.ENCODING_MP3)
                .setSampleRate(44100)   //采样率
                .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)     //信道掩码
                .build();
        AudioAttributes audioAttributes = new AudioAttributes.Builder()   //用于封装描述有关音频流的信息的属性集合的类.
                        .setUsage(AudioAttributes.USAGE_MEDIA)  //set声音用途
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC) //set播放内容的类型
                .build();
//
//        boolean offloadedPlaybackSupported = AudioManager.isOffloadedPlaybackSupported(audioFormat,audioAttributes);
//        Log.i(TAG,"offloadedPlaybackSupported is "+offloadedPlaybackSupported);
        minBufferSize = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);

        audioTrack = new AudioTrack.Builder()
                .setAudioAttributes(audioAttributes)
                .setAudioFormat(audioFormat)
                .setBufferSizeInBytes(minBufferSize*3)
//                .setTransferMode(AudioTrack.MODE_STREAM)
                .setOffloadedPlayback(true)
                .build();

        Log.i(TAG,"audiotrack is create");
    }


    public void play() {
        Log.i(TAG,"in play() and pre loadfile() ");
        loadFile(); //把文件读入bis里面
        executorService.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            writeData();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally {
                            audioTrack.stop();
                        }
                    }
                }
        );
    }


    private void writeData() throws IOException {
        audioTrack.play();
        byte[] buffer = new byte[minBufferSize*15];
        while ((audioTrack!= null && audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING)){
            int read = bufferedInputStream.read(buffer, 0, buffer.length);
            if (read < 0){
                break;
            }
            audioTrack.write(buffer,0,read);
            Log.i(TAG,"writeData() pre write data is "+read);
        }
    }

    private void loadFile() {
        InputStream inputStream = context.getResources().openRawResource(R.raw.mp31);
        bufferedInputStream = new BufferedInputStream(inputStream);
    }


    public void stop(){
        audioTrack.stop();
    }

    public void realease(){
        if (audioTrack != null){
            audioTrack.release();
        }

    }
}
