package com.example.matt.podcastapplication;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;

import static com.example.matt.podcastapplication.AudioPlayer.Broadcast_PLAY_NEW_AUDIO;

public class AudioService extends Service {
    private MediaPlayer mediaPlayer;
    private final IBinder iBinder = new LocalBinder();
    final String audioFileUrl = "https://upload.wikimedia.org/wikipedia/commons/6/6c/Grieg_Lyric_Pieces_Kobold.ogg";
    private String url;
    public AudioService() {

    }

    /**
     * Service Binder
     */
    public class LocalBinder extends Binder {
        public AudioService getService() {
            // Return this instance of LocalService so clients can call public methods
            return AudioService.this;
        }
    }

    /**
     * Service lifecycle methods
     */
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        register_playNewAudio();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        url = intent.getExtras().getString("PODCAST_URL");
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
        initMediaPlayer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service destroyed by user.", Toast.LENGTH_LONG).show();
    }

    /**
     * Play new Audio
     */
    private BroadcastReceiver playNewAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            url = intent.getExtras().getString("PODCAST_URL");

            initMediaPlayer();


        }
    };

    /**
     * MediaPlayer actions
     */
    private void initMediaPlayer() {
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();//new MediaPlayer instance
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        playMedia();
    }

    private void register_playNewAudio() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(Broadcast_PLAY_NEW_AUDIO);
        registerReceiver(playNewAudio, filter);
    }


    private void playMedia() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }

    }
}