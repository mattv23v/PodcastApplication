package com.example.matt.podcastapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends Activity implements PodcastResponse{

    private Button btn;
    private boolean playPause;
    private MediaPlayer mediaPlayer;
    private ProgressDialog progressDialog;
    private boolean initialStage = true;
    private EditText edittext;
    private SearchPodcasts search;
    private ArrayList podcastList;
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.audioStreamBtn);
        edittext = (EditText) findViewById(R.id.editText);
        podcastList=new ArrayList<Podcast>();
        mListView = (ListView) findViewById(R.id.listView);



        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String searchWord = edittext.getText().toString();

                new SearchPodcasts(MainActivity.this).execute(searchWord);

            }
        });

    }


   @Override
   public void processFinish(ArrayList<Podcast> output) {

       podcastList=output;
       arrayAdapterListView();


   }


   private void arrayAdapterListView() {


       final PodcastListAdapter adapter = new PodcastListAdapter(this, R.layout.podcast_view_layout, podcastList);
       mListView.setAdapter(adapter);

           mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               public void onItemClick(AdapterView<?> parent, View view,
                                       int position, long id) {
                   Podcast podCast=adapter.getItem(position);

                   Toast.makeText(getApplicationContext(),
                           podCast.getTitle().toString(), Toast.LENGTH_LONG).show();

                   EpisodeDisplay display = new EpisodeDisplay();

                   Intent myIntent = new Intent(MainActivity.this,display.getClass());
                   myIntent.putExtra("id",podCast.getID());
                   myIntent.putExtra("title",podCast.getTitle());
                   startActivity(myIntent);
               }

           });

    }


}