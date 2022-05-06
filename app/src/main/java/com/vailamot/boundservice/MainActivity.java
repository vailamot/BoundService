package com.vailamot.boundservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements IUpdateUiFromService{
    private RelativeLayout layout;
    private TextView tvTime, tvTotal;
    private ImageView imageView;

    private MyService mMyService;
    private boolean isServiceConnection;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyService.MyBinder myBinder = (MyService.MyBinder) iBinder;
            mMyService = myBinder.getMyService();
            mMyService.setIUpdateUiFromService(MainActivity.this);
            isServiceConnection = true;
            display();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mMyService = null;
            isServiceConnection = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.layout_bound);
        tvTime = findViewById(R.id.tv_time);
        tvTotal = findViewById(R.id.tv_total);
        imageView = findViewById(R.id.img_play_or_pause);


    }

    public void startService(View view) {
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void stopService(View view) {
        if (isServiceConnection) {
            unbindService(mServiceConnection);
            isServiceConnection = false;
            layout.setVisibility(View.GONE);
        }
    }

    public void changeMusic(View view) {
        if (mMyService.isPlaying()) {
            mMyService.pauseMusic();
            imageView.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        } else {
            mMyService.resumeMusic();
            imageView.setImageResource(R.drawable.ic_baseline_pause_24);
        }
    }

    public void display() {
        layout.setVisibility(View.VISIBLE);
        int maxDur = mMyService.getMax();
        tvTotal.setText("/   " + (maxDur/60) + ":" + (maxDur%60));
    }

    @Override
    public void updateCurrentTimeSong(long time) {
        Log.d("DoanhTq", "updateCurrentTimeSong: time " + time);
        tvTime.setText((time/60) + ":" + (time%60));
    }
}