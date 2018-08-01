package com.example.ducvu212.demomusicfromresource;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MusicService extends Service {

    private MusicPlayer mMusicPlayer;
    private final IBinder iBinder = new LocalBinder();

    public MusicService() {
    }

    public void registerClient(updateUI ui) {
        ui.updateSeekBar(mMusicPlayer);
    }

    @Override
    public IBinder onBind(Intent intent) {
        mMusicPlayer.play();
        return iBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
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

    public class LocalBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public interface updateUI {
        void updateSeekBar(MusicPlayer musicPlayer);
    }

}
