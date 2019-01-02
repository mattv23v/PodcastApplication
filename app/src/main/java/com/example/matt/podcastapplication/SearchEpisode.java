package com.example.matt.podcastapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class SearchEpisode extends AsyncTask<String, Integer, HttpResponse<JsonNode>> {

    private ArrayList<Episode> episodeArrayList = new ArrayList<Episode>();
    private JSONObject myObj = null;
    public EpisodeResponse delegate = null;
    private Context mContext;
    ProgressDialog mProgress;
    private String title;
    private String id;

    public SearchEpisode(Context context) {
        this.mContext = context;
        this.delegate = (EpisodeResponse) context;

    }

    @Override
    protected HttpResponse<JsonNode> doInBackground(String... search) {
        HttpResponse<JsonNode> request = null;
        title=search[0];
        id=search[1];
        try {

            request = Unirest.get("https://listennotes.p.mashape.com/api/v1/search?language=English&ocid=f2dbc548f2f54604a6262bca5333d2e1&offset=0&q=sam&sort_by_date=0&type=episode")
                    .header("X-Mashape-Key", "4aIkQzrwdWmshzRSBreoEcbXwaEHp1tqNTGjsndU6yVzoGANFc")
                    .header("Accept", "application/json")
                    .asJson();


        } catch (UnirestException e) {
            Log.e("mytag",e.toString());
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return request;    }

    @Override
    protected void onPostExecute(HttpResponse<JsonNode> response) {

        myObj = response.getBody().getObject();
        JSONArray results = null;

        try {
            results = myObj.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int index=0; index<results.length(); ++index){
            try {
                JSONObject currentResult = results.getJSONObject(index);
                String audio = currentResult.getString("audio");
                String episodeTitle = currentResult.getString("title_original");
                String date = currentResult.getString("pub_date_ms");
                long longDate = Long.parseLong(date);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(longDate);
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                String pudDate = mMonth+"/"+mDay+"/"+mYear;
                Episode currentEpisode = new Episode(audio,episodeTitle,pudDate);

                episodeArrayList.add(currentEpisode);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        delegate.processFinish(episodeArrayList);
    }
}
