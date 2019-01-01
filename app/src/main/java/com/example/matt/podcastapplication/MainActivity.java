package com.example.matt.podcastapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends Activity implements AsyncResponse{

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
      //  mediaPlayer = new MediaPlayer();
        // mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
 //       progressDialog = new ProgressDialog(this);
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


               // asyncTask.execute(searchWord);

               // asyncTask.cancel(true);


              /*  if (!playPause) {
                    btn.setText("Pause Streaming");
                    if (initialStage) {
                        new Player().execute("https://www.ssaurel.com/tmp/mymusic.mp3");
                    } else {
                        if (!mediaPlayer.isPlaying())
                            mediaPlayer.start();
                    }

                    playPause = true;

                } else {

                    btn.setText("Launch Streaming");
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }

                    playPause = false;
                }*/


   /* @Override
    protected void onPause() {
        super.onPause();

        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }



    class Player extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean prepared = false;

            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        initialStage = true;
                        playPause = false;
                        btn.setText("Launch Streaming");
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });

                mediaPlayer.prepare();
                prepared = true;

            } catch (Exception e) {
                Log.e("MyAudioStreamingApp", e.getMessage());
                prepared = false;
            }

            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }

            mediaPlayer.start();
            initialStage = false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Buffering...");
            progressDialog.show();
        }
    }*/

   @Override
   public void processFinish(ArrayList<Podcast> output) {

       podcastList=output;
       arrayAdapterListView();
   }
       private void arrayAdapterListView() {


       PodcastListAdapter adapter = new PodcastListAdapter(this, R.layout.adapter_view_layout, podcastList);
       mListView.setAdapter(adapter);



    }


}