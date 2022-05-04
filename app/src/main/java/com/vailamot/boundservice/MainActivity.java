package com.vailamot.boundservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout layout;
    private TextView tvTime;
    private ImageView imageView;

    private MyService mMyService;
    private boolean isServiceConnection;


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyService.MyBinder myBinder = (MyService.MyBinder) iBinder;
            mMyService = myBinder.getMyService();
            isServiceConnection = true;
            hienThi();


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


    public void hienThi() {
        layout.setVisibility(View.VISIBLE);
        if(isServiceConnection) {
            new CountDownTimer(mMyService.getMax(), 5) {
                @Override
                public void onTick(long l) {
                    tvTime.setText((mMyService.getTime()) / 60000 + ":" + (mMyService.getTime()) / 1000);
                }

                @Override
                public void onFinish() {
                    tvTime.setText(mMyService.getMax() + "");
                }
            }.start();
        }
    }
}