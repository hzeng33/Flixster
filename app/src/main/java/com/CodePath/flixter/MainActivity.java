package com.CodePath.flixter;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.CodePath.flixter.adapters.MovieAdapter;
import com.CodePath.flixter.models.Movie;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String TAG = "MainActivity";

    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RecyclerView rvMovies = findViewById(R.id.rvMovies);
        movies = new ArrayList<>();

        //Create the adapter.
        MovieAdapter movieAdapter = new MovieAdapter(this,movies);

        //Set the adapter on the recycle view.
        rvMovies.setAdapter(movieAdapter);

        //Set a Layout Manager on the recycle view.
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client = new AsyncHttpClient();
        //Make a get request on the URL.
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG,"onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG,"Results:"+results.toString());
                    movies.addAll( Movie.fromJsonArray(results));//keep modifying, don't need brand new movie list because the adapter handles that for us.
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG,"Movies:"+ movies.size());
                } catch (JSONException e) {
                    Log.e(TAG,"Hit JSON exception", e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                 Log.d(TAG,"onFailure");
            }
        });
    }
}