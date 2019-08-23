package com.example.matt.podcastapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class EpisodeDisplay extends Activity implements EpisodeResponse {

    private Button btn;
    private MediaPlayer mediaPlayer;
    private ProgressDialog progressDialog;
    private String title;
    private String id;
    private ArrayList episodeList;
    private ListView mListView;
    private AudioPlayer player;

    public EpisodeDisplay(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.episode_display_layout);
        btn = (Button) findViewById(R.id.audioStreamBtn);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        progressDialog = new ProgressDialog(this);
        episodeList =new ArrayList<Episode>();
        Bundle b = getIntent().getExtras();
        if (b != null) {
            id = b.getString("id");
            title = b.getString("title");
        }
        mListView = (ListView) findViewById(R.id.listView);

        player = new AudioPlayer();

        new SearchEpisode(EpisodeDisplay.this).execute(title,id);

     //   playAudio("https://upload.wikimedia.org/wikipedia/commons/6/6c/Grieg_Lyric_Pieces_Kobold.ogg");

    }


    @Override
    public void processFinish(ArrayList<Episode> output) {

        episodeList=output;
        arrayAdapterListView();

    }

    private void arrayAdapterListView() {

        final EpisodeListAdapter adapter = new EpisodeListAdapter(this, R.layout.episode_view_layout, episodeList);
        mListView.setAdapter(adapter);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Episode episode=adapter.getItem(position);

                Intent myIntent = new Intent(EpisodeDisplay.this,player.getClass());
                myIntent.putExtra("title",episode.getTitle());
                myIntent.putExtra("audio",episode.getAudio());
                myIntent.putExtra("image",episode.getImage());
                startActivity(myIntent);
            }

        });

    }


    }
