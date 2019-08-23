package com.example.matt.podcastapplication;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class AudioPlayer extends AppCompatActivity {
    private ImageButton forwardbtn, backwardbtn, pausebtn, playbtn;
    private TextView songName, startTime, songTime;
    private SeekBar songPrgs;
    private static int oTime =0, sTime =0, eTime =0, fTime = 5000, bTime = 5000;
    private Handler hdlr = new Handler();
    private String title;
    private String audio;
    private String image;
    AudioService player;
    private String url;
    private ImageView imageView;
    URL ImageUrl = null;
    InputStream is = null;
    Bitmap bmImg = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.audio_player_layout);
        Bundle b = getIntent().getExtras();
        audio = b.getString("audio");
        title = b.getString("title");
        image = b.getString("image");

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
        imageView = findViewById(R.id.imageView3);
        AsyncTaskExample asyncTask=new AsyncTaskExample();
        asyncTask.execute(image);
        //asyncTask.execute("https://www.tutorialspoint.com/images/tp-logo-diamond.png");

        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMyServiceRunning(AudioService.class))
                player.playMedia();

            }
        });
        pausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(AudioService.class))
                    player.pauseMedia();
            }
        });
        forwardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(AudioService.class))
                    player.forward();

            }
        });
        backwardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(AudioService.class))
                    player.back();
            }
        });
    }
    private class AsyncTaskExample extends AsyncTask<String, String, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                ImageUrl = new URL(strings[0]);
                HttpURLConnection conn = (HttpURLConnection) ImageUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                is = conn.getInputStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                bmImg = BitmapFactory.decodeStream(is, null, options);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmImg;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            // Cast and assign background service's onBind method returned iBander object.
            AudioService.LocalBinder binder = (AudioService.LocalBinder) service;
            player = binder.getService();
            eTime = player.getDurTime();
            sTime = player.getCurrTime();
            songPrgs.setMax(eTime);
            songTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(eTime),
                    TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(eTime))) );
            startTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime),
                    TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(sTime))) );
            songPrgs.setProgress(sTime);
            hdlr.postDelayed(UpdateSongTime, 100);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Toast.makeText(getApplicationContext(), "Disconnect", Toast.LENGTH_SHORT).show();

        }
    };
   private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            sTime = player.getCurrTime();
            startTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime),
                    TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime))) );
            songPrgs.setProgress(sTime);
            hdlr.postDelayed(this, 100);
        }
    };
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

