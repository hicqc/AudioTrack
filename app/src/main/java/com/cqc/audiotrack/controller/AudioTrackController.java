package com.cqc.audiotrack.controller;



import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.cqc.audiotrack.R;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;



public class AudioTrackController {

    private String TAG = "AudioTrackController";

    AudioTrack audioTrack;
    Context context;

    ExecutorService executorService = Executors.newSingleThreadExecutor();
    int minBufferSize;

    ByteArrayInputStream byteArrayInputStream;
    BufferedInputStream bufferedInputStream;




    @RequiresApi(api = Build.VERSION_CODES.M)
    public AudioTrackController(Context context) throws IOException {
        this.context = context;
        initAudioTrack();
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public void initAudioTrack() throws IOException {
        AudioFormat audioFormat = new AudioFormat.Builder()   //用于访问多个音频格式和信道配置常量。
                .setEncoding(2)
                .setSampleRate(48000)   //采样率
                .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)     //信道掩码
                .build();
        AudioAttributes audioAttributes = new AudioAttributes.Builder()   //用于封装描述有关音频流的信息的属性集合的类.
                        .setUsage(AudioAttributes.USAGE_MEDIA)  //set声音用途
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC) //set播放内容的类型
                .build();

        minBufferSize = AudioTrack.getMinBufferSize(48000, AudioFormat.CHANNEL_OUT_STEREO,
                2);

        audioTrack = new AudioTrack.Builder()
                .setAudioAttributes(audioAttributes)
                .setAudioFormat(audioFormat)
                .setBufferSizeInBytes(minBufferSize)
                .build();

        Log.i(TAG,"audiotrack is create and minBufferSize is "+minBufferSize);
        loadFile(); //把文件读入bis里面
    }


    public void play() throws IOException {

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
        byte[] buffer = new byte[minBufferSize*10];
        while ((audioTrack!= null && audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING)){
            int read = bufferedInputStream.read(buffer, 0, buffer.length);
            if (read < 0){
                break;
            }
            audioTrack.write(buffer,0,read);
            Log.i(TAG,"writeData() pre write data is "+read);
        }
    }


    /**
     * load file and reverse bytes order
     * @throws IOException
     */
    private void loadFile() throws IOException {
        InputStream inputStream = context.getResources().openRawResource(R.raw.nightfever);
         bufferedInputStream= new BufferedInputStream(inputStream);


//
//    byte[] bytesArray = new byte[bufferedInputStream.available()];
//    Log.i(TAG,"bytesArray length is "+bytesArray.length+" bufferedInputStream.available()"+bufferedInputStream.available());
//    FileOutputStream fileOutputStream = context.openFileOutput("nightR.pcm",Context.MODE_APPEND);
//    byte[] buffer = new byte[1];
//
//        for (int i =bytesArray.length-1; i >=0  ; i--) {
//            int read = bufferedInputStream.read(buffer, 0,1);
//            if (read < 1){
//                break;
//            }
//            bytesArray[i] = buffer[0];
//            Log.i(TAG,"i = "+ i);
//
//        }
//        fileOutputStream.write(bytesArray);








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
