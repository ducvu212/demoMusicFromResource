package com.example.ducvu212.demomusicfromresource;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener,
        MusicService.updateUI {

    private static final long TIME_DELAY_LOOP = 300;
    private static final long TIME_UPDATE_SEEKBAR = 1000;
    private SeekBar mSeekBar;
    private TextView mTextViewTimeRunning;
    private TextView mTextViewTotalTime;
    private boolean mIsConnect;
    private Handler mHandler;
    private int mPosition;
    private MusicPlayer mMusicPlayer;

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
        mTextViewTimeRunning = findViewById(R.id.textView_time_running);
        mTextViewTotalTime = findViewById(R.id.textview_time_total);
    }

    private void initComponents() {
        mHandler = new Handler();
        mSeekBar.setOnSeekBarChangeListener(this);
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
            MusicService mMusicService = binder.getService();
            if (mMusicService != null) {
                mIsConnect = true;
                mMusicService.registerClient(MainActivity.this);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mIsConnect = false;
            connectService();
        }
    };

    private Runnable runnable = new Runnable() {
        long timeRunning;

        @SuppressLint({"DefaultLocale", "HandlerLeak"})
        @Override
        public void run() {
            timeRunning = mMusicPlayer.getCurenPositionMusic();
            mSeekBar.setProgress((int) timeRunning);
            mTextViewTimeRunning.setText(String.format("%d min, %d second",
                    TimeUnit.MILLISECONDS.toMinutes(timeRunning),
                    TimeUnit.MILLISECONDS.toSeconds(timeRunning) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes(timeRunning))));
            mHandler.postDelayed(this, TIME_UPDATE_SEEKBAR);
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        mPosition = i;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mTextViewTimeRunning.setText(String.format("%d min, %d second",
                TimeUnit.MILLISECONDS.toMinutes((long) mPosition),
                TimeUnit.MILLISECONDS.toSeconds((long) mPosition) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                toMinutes((long) mPosition))));
        mMusicPlayer.seekToMusic(mPosition);
    }

    @SuppressLint("DefaultLocale")
    public void updateSeekBar(SeekBar seekBar, TextView textViewRunning, TextView textViewTotal) {
        mSeekBar = seekBar;
        mTextViewTimeRunning = textViewRunning;
        long totalTTime = mMusicPlayer.getDrurationMusic();
        textViewTotal.setText(String.format("%d min, %d second",
                TimeUnit.MILLISECONDS.toMinutes(totalTTime),
                TimeUnit.MILLISECONDS.toSeconds(totalTTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                toMinutes(totalTTime))));
        seekBar.setMax((int) mMusicPlayer.getDrurationMusic());
        mHandler.postDelayed(runnable, TIME_DELAY_LOOP);
        seekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void updateSeekBar(MusicPlayer musicPlayer) {
        mMusicPlayer = musicPlayer;
        updateSeekBar(mSeekBar, mTextViewTimeRunning, mTextViewTotalTime);
    }
}
