package com.example.matt.podcastapplication;

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
    public AsyncResponse delegate = null;
    public String test = "hi";
    private Context mContext;


    public SearchPodcasts(Context context){
        this.mContext = context;
        this.delegate = (AsyncResponse) context;

    }

    @Override
    protected HttpResponse<JsonNode> doInBackground(String... searchWord) {

        Log.e("mytag","doInBackground");
        HttpResponse<JsonNode> request = null;
        String search=searchWord[0];
        Log.e("mytag",search);

        try {
            Log.e("mytag","try start");
           /* request = Unirest.get("http://httpbin.org/get")
                    .header("Accept", "application/json")
                    .asJson();*/
            request = Unirest.get("https://api.listennotes.com/api/v1/search?language=English&offset=0&only_in=title&published_after=0" +
                    "&published_before=0&q="+search+"&sort_by_date=0&type=podcast")
                    .header("X-Mashape-Key", "4aIkQzrwdWmshzRSBreoEcbXwaEHp1tqNTGjsndU6yVzoGANFc")
                    .header("Accept", "application/json")
                    .asJson();

            Log.e("mytag","try end");

        } catch (UnirestException e) {
            Log.e("mytag",e.toString());
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.e("mytag",request.getBody().toString());

        return request;
    }


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
                String titleOriginal = currentResult.getString("title_original");
                String descriptionOriginal = currentResult.getString("description_original");
                String audio = currentResult.getString("listennotes_url");
                descriptionOriginal = descriptionOriginal.substring(0, Math.min(descriptionOriginal.length(), 150));
                if (descriptionOriginal.length()==150)
                    descriptionOriginal = descriptionOriginal+"...";
                Podcast currentCast = new Podcast(titleOriginal,descriptionOriginal,audio);

                podcastArrayList.add(currentCast);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        delegate.processFinish(podcastArrayList);

    }


}