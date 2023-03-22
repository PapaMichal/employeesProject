package com.example.employeesassignment;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;


public class MusicService extends Service {

    public MediaPlayer myPlayer;
    private float leftVolume, rightVolume;

    private final LocalBinder binder = new LocalBinder();
    public class LocalBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    @Override
    public void onCreate() {
        leftVolume = 1;
        rightVolume = 1;
        myPlayer = MediaPlayer.create(this, R.raw.music);
        myPlayer.setVolume(1,1);
        myPlayer.setLooping(true); // Set looping
        playMusic();
    }

    public void playMusic() {
        myPlayer.start();
    }

    public void stopMusic() {
        myPlayer.pause();
    }

    @Override
    public void onDestroy() {
        myPlayer.stop();
    }

    public float getVolume() {
        return leftVolume;
    }

    public void setVolume(float leftVolume, float rightVolume) {
        this.leftVolume = leftVolume;
        this.rightVolume = rightVolume;
        myPlayer.setVolume(leftVolume, rightVolume);
    }
}
