package com.example.ducvu212.demomusicfromresource;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.ImageView;

public class MusicPlayer extends MediaPlayer {

    private MediaPlayer mMediaPlayer;
    private final int TIME_FORWARD = 5000;
    private final int TIME_BACKWARD = -5000;
    private Context mContext;

    public MediaPlayer createMediaPlayer(Context context, int id){
        mMediaPlayer = MediaPlayer.create(context, id);
        mContext = context;
        return mMediaPlayer;
    }

    public void pause(ImageView imageView){
        mMediaPlayer.pause();
        imageView.setImageResource(R.drawable.ic_pause);
    }

    public long getDrurationMusic(){
        return mMediaPlayer.getDuration();
    }

    public long getCurenPositionMusic(){
        return mMediaPlayer.getCurrentPosition();
    }

    public void play(ImageView imageView){
        mMediaPlayer.start();
        imageView.setImageResource(R.drawable.ic_play);
    }

    public void seekToMusic(long position){
        mMediaPlayer.seekTo((int) position);
    }

    public void play(){
        mMediaPlayer.start();
    }

    public void backWard(){
        mMediaPlayer.seekTo(TIME_BACKWARD);
    }

    public void forward(){
        mMediaPlayer.seekTo(TIME_FORWARD);
    }

    public void stopMusic(){
        mMediaPlayer.stop();
        mMediaPlayer.release();
    }

}
