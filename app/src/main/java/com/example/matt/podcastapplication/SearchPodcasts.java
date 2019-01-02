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


class SearchPodcasts extends AsyncTask<String, Integer, HttpResponse<JsonNode>> {
    private ArrayList<Podcast> podcastArrayList = new ArrayList<Podcast>();
    private JSONObject myObj = null;
    public PodcastResponse delegate = null;
    private Context mContext;
    ProgressDialog mProgress;



    public SearchPodcasts(Context context){
        this.mContext = context;
        this.delegate = (PodcastResponse) context;

    }

    @Override
    public void onPreExecute() {
        mProgress = new ProgressDialog(mContext);
        mProgress.setMessage("Searching...");
        mProgress.show();
    }


    @Override
    protected HttpResponse<JsonNode> doInBackground(String... searchWord) {

        HttpResponse<JsonNode> request = null;
        String search=searchWord[0];
        search = search.replaceAll(" ", "+");

        try {

            request = Unirest.get("https://api.listennotes.com/api/v1/search?language=English&offset=0&only_in=title&published_after=0" +
                    "&published_before=0&q="+search+"&sort_by_date=0&type=podcast")
                    .header("X-Mashape-Key", "4aIkQzrwdWmshzRSBreoEcbXwaEHp1tqNTGjsndU6yVzoGANFc")
                    .header("Accept", "application/json")
                    .asJson();


        } catch (UnirestException e) {
            Log.e("mytag",e.toString());
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return request;
    }


    @Override
    protected void onPostExecute(HttpResponse<JsonNode> response) {
        mProgress.dismiss();

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
                String titleOriginal = currentResult.getString("title_original");
                String descriptionOriginal = currentResult.getString("description_original");
                String id = currentResult.getString("id");
                descriptionOriginal = descriptionOriginal.substring(0, Math.min(descriptionOriginal.length(), 150));
                if (descriptionOriginal.length()==150)
                    descriptionOriginal = descriptionOriginal+"...";
                descriptionOriginal = descriptionOriginal.replace("&nbsp;","");
                Podcast currentCast = new Podcast(titleOriginal,descriptionOriginal,id);

                podcastArrayList.add(currentCast);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        delegate.processFinish(podcastArrayList);
    }


}