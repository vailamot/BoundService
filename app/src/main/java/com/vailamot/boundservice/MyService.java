package com.vailamot.boundservice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class MyService extends Service {
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;

    private MyBinder myBinder = new MyBinder();

    public class MyBinder extends Binder{
        MyService getMyService(){
            return MyService.this;
        }
    }
    public MyService() {
    }



    @Override
    public IBinder onBind(Intent intent) {
        startMusic();
        return myBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    // Phát nhạc
    private void startMusic() {
        if(mediaPlayer == null){
            mediaPlayer= MediaPlayer.create(getApplicationContext(), R.raw.song);
        }
        mediaPlayer.start();
        isPlaying = true;
    }
    public void pauseMusic() {
        if(mediaPlayer != null && isPlaying){
            mediaPlayer.pause();
            isPlaying = false;
        }
    }
    public void resumeMusic() {
        if(mediaPlayer != null && !isPlaying){
            mediaPlayer.start();
            isPlaying = true;
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public int getTime(){
        if(mediaPlayer!=null){
            int a = mediaPlayer.getCurrentPosition();
            return a;
        }
        return 0;

    }
    public int getMax(){
        if(mediaPlayer!=null) {
            int b = mediaPlayer.getDuration();
            return b;
        }
        return 0;
    }
}