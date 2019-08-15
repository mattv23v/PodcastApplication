package com.example.matt.podcastapplication;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import android.os.Handler;
import android.os.IBinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class AudioPlayer extends AppCompatActivity {
    private ImageButton forwardbtn, backwardbtn, pausebtn, playbtn;
    private TextView songName, startTime, songTime;
    private SeekBar songPrgs;
    private static int oTime =0, sTime =0, eTime =0, fTime = 5000, bTime = 5000;
    private Handler hdlr = new Handler();
    private String title;
    private String audio;
    AudioService player;
    public static final String Broadcast_PLAY_NEW_AUDIO = "com.example.matt.podcastapplication.AudioPlayer.PlayNewAudio";

    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.audio_player_layout);
        Bundle b = getIntent().getExtras();
        audio = b.getString("audio");
        title = b.getString("title");
        backwardbtn = findViewById(R.id.btnBackward);
        forwardbtn = findViewById(R.id.btnForward);
        playbtn = findViewById(R.id.btnPlay);
        pausebtn = findViewById(R.id.btnPause);
        songName = findViewById(R.id.txtSname);
        startTime = findViewById(R.id.txtStartTime);
        songTime = findViewById(R.id.txtSongTime);
        songName.setText(title);
        url = audio; // your URL here
        songPrgs = findViewById(R.id.sBar);
        songPrgs.setClickable(false);
      //  pausebtn.setEnabled(false);
        /*eTime = mPlayer.getDuration();
        sTime = mPlayer.getCurrentPosition();
        songPrgs.setMax(eTime);
        oTime =1;
        songTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(eTime),
                TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(eTime))) );
        startTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime),
                TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(sTime))) );
        songPrgs.setProgress(sTime);
        hdlr.postDelayed(UpdateSongTime, 100);*/

        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.playMedia();
               /* if (isMyServiceRunning(AudioService.class)) {

                    Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
                    broadcastIntent.putExtra("PODCAST_URL", url);
                    sendBroadcast(broadcastIntent);

                }*/

              //  pausebtn.setEnabled(true);
               // playbtn.setEnabled(false);
            }
        });
        pausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(AudioService.class)) {
                    player.pauseMedia();
                }
            //    pausebtn.setEnabled(false);
             //   playbtn.setEnabled(true);
            //    Toast.makeText(getApplicationContext(),"Pausing Audio", Toast.LENGTH_SHORT).show();
            }
        });
        forwardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((sTime + fTime) <= eTime)
                {
                    sTime = sTime + fTime;
               //     mPlayer.seekTo(sTime);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
                }
                if(!playbtn.isEnabled()){
                    playbtn.setEnabled(true);
                }
            }
        });
        backwardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((sTime - bTime) > 0)
                {
                    sTime = sTime - bTime;
         //           mPlayer.seekTo(sTime);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
                }
                if(!playbtn.isEnabled()){
                    playbtn.setEnabled(true);
                }
            }
        });
    }
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            // Cast and assign background service's onBind method returned iBander object.
            AudioService.LocalBinder binder = (AudioService.LocalBinder) service;
            player = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Toast.makeText(getApplicationContext(), "Disconnect", Toast.LENGTH_SHORT).show();

        }
    };
   /* private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            sTime = mPlayer.getCurrentPosition();
            startTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime),
                    TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime))) );
            songPrgs.setProgress(sTime);
            hdlr.postDelayed(this, 100);
        }
    };*/
   private boolean isMyServiceRunning(Class<?> serviceClass) {
       ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
       for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
           if (serviceClass.getName().equals(service.service.getClassName())) {
               return true;
           }
       }
       return false;
   }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
            Intent intent = new Intent(AudioPlayer.this, AudioService.class);
            intent.putExtra("PODCAST_URL", url);
            startService(intent);
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        // Unbound background audio service when activity is destroyed.
        super.onDestroy();
        unbindService(serviceConnection);

    }


}

