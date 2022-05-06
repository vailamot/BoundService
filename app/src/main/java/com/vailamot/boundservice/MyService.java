package com.vailamot.boundservice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;

    private final MyBinder myBinder = new MyBinder();

    private IUpdateUiFromService mIUpdateUiFromService;

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
        mIUpdateUiFromService = null;
    }

    // Phát nhạc
    private void startMusic() {
        if(mediaPlayer == null){
            mediaPlayer= MediaPlayer.create(getApplicationContext(), R.raw.song);
           ;

//            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
//                @Override
//                public void onBufferingUpdate(MediaPlayer mp, int percent) {
//                    Log.d("DoanhTq", "onTimedText: time " + percent);
//                }
//            });
//            mediaPlayer.setOnTimedTextListener(new MediaPlayer.OnTimedTextListener() {
//                @Override
//                public void onTimedText(MediaPlayer mediaPlayer, TimedText timedText) {
//                    Log.d("DoanhTq", "onTimedText: time " + timedText.getText());
//                }
//            });
        }
        int maxDur = getMax();
        new CountDownTimer(maxDur*1000, 1000) {
            @Override
            public void onTick(long l) {

                if (mIUpdateUiFromService != null) {
                    mIUpdateUiFromService.updateCurrentTimeSong(getTime());
                }

            }

            @Override
            public void onFinish() {
            }
        }.start();
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
            return mediaPlayer.getCurrentPosition()/1000;
        }
        return 0;

    }
    public int getMax(){
        if(mediaPlayer!=null) {
            return mediaPlayer.getDuration()/1000;
        }
        return 0;
    }

    public void setIUpdateUiFromService(IUpdateUiFromService mIUpdateUiFromService) {
        this.mIUpdateUiFromService = mIUpdateUiFromService;
    }
}