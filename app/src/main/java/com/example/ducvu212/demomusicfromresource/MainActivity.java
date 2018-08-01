package com.example.ducvu212.demomusicfromresource;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private MusicService mMusicService;
    private SeekBar mSeekBar;
    private TextView mTimeRunning;
    private TextView mTotalTime;
    private boolean mIsConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewByIds();
        initComponents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        connectService();
    }

    private void connectService() {
        final Intent intent = new Intent(this, MusicService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void findViewByIds() {
        mSeekBar = findViewById(R.id.seekBar_time);
        mTimeRunning = findViewById(R.id.textView_time_running);
        mTotalTime = findViewById(R.id.textview_time_total);
    }

    private void initComponents() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mIsConnect) {
            unbindService(mConnection);
            mIsConnect = false;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) iBinder;
            mMusicService = binder.getService();
            if (mMusicService != null) {
                mIsConnect = true;
                mMusicService.updateSeekBar(mSeekBar, mTimeRunning, mTotalTime);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mIsConnect = false;
            connectService();
        }
    };

}
