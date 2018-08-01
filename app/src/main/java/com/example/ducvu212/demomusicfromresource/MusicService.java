package com.example.ducvu212.demomusicfromresource;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MusicService extends Service implements SeekBar.OnSeekBarChangeListener {

    private SeekBar mSeekbar;
    private MusicPlayer mMusicPlayer;
    private Handler mHandler;
    private int position;
    private final IBinder iBinder = new LocalBinder();
    private TextView mTextviewRunning;
    private Handler mHandlerRunable;

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        mMusicPlayer.play();
        return iBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
        mMusicPlayer = new MusicPlayer();
        mMusicPlayer.createMediaPlayer(getApplicationContext(), R.raw.xaem);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mMusicPlayer.stopMusic();
        return super.onUnbind(intent);
    }

    @SuppressLint("DefaultLocale")
    public void updateSeekBar(SeekBar seekBar, TextView textViewRunning, TextView textViewTotal) {
        mSeekbar = seekBar;
        mTextviewRunning = textViewRunning;
        long totalTTime = mMusicPlayer.getDrurationMusic();
        textViewTotal.setText(String.format("%d min, %d second",
                TimeUnit.MILLISECONDS.toMinutes((long) totalTTime),
                TimeUnit.MILLISECONDS.toSeconds((long) totalTTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                toMinutes((long) totalTTime))));
        seekBar.setMax((int) mMusicPlayer.getDrurationMusic());
        mHandler.postDelayed(runnable, 300);
        seekBar.setOnSeekBarChangeListener(this);
    }

    private Runnable runnable = new Runnable() {
        long timeRunning;

        @SuppressLint({"DefaultLocale", "HandlerLeak"})
        @Override
        public void run() {
            timeRunning = mMusicPlayer.getCurenPositionMusic();
            mSeekbar.setProgress((int) timeRunning);
            mTextviewRunning.setText(String.format("%d min, %d second",
                    TimeUnit.MILLISECONDS.toMinutes((long) timeRunning),
                    TimeUnit.MILLISECONDS.toSeconds((long) timeRunning) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) timeRunning))));
            mHandler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        position = i;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mTextviewRunning.setText(String.format("%d min, %d second",
                TimeUnit.MILLISECONDS.toMinutes((long) position),
                TimeUnit.MILLISECONDS.toSeconds((long) position) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                toMinutes((long) position))));
        mMusicPlayer.seekToMusic(position);
    }

    public class LocalBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }
}
