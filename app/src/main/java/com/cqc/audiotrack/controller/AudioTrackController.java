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


@RequiresApi(api = Build.VERSION_CODES.M)
public class AudioTrackController {

    private String TAG = "AudioTrackController";

    AudioTrack audioTrack;
    Context context;
    BufferedInputStream bufferedInputStream;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    int minBufferSize;

    public AudioTrackController(Context context) {
        initAudioTrack();
        this.context = context;
    }

    public void initAudioTrack() {
        minBufferSize = AudioTrack.getMinBufferSize(48000, AudioFormat.CHANNEL_IN_STEREO,
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
                .setBufferSizeInBytes(minBufferSize)
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
                            audioTrack.release();
                        }
                    }
                }
        );
    }


    private void writeData() throws IOException {
        byte[] bytes = new byte[minBufferSize];
        int length;
        audioTrack.play();
        while ((audioTrack!= null && audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING &&(length = bufferedInputStream.read(bytes)) != -1)){
            audioTrack.write(bytes,0,length);//offsetInBytes表示bytes要写入数据开始的偏移量
        }
    }

    private void loadFile() {
        InputStream inputStream = context.getResources().openRawResource(R.raw.music);
        bufferedInputStream = new BufferedInputStream(inputStream);
    }

    public void stop(){
        audioTrack.stop();
    }

    public void realease(){
        audioTrack.release();   //release 调用了stop
    }
}
