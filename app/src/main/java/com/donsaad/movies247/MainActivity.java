package com.donsaad.movies247;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185";
    public static final String MOVIE_OVERVIEW_KEY = "overview";
    public static final String MOVIE_TITLE_KEY = "original_title";
    public static final String POSTER_PATH_KEY = "poster_path";
    private static final String MOVIES_KEY = "results";

    private GridView mGridView;
    private List<String> posters;
    private MoviesGridAdapter adapter;
    private JSONArray movies;
    private JSONObject movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        // after this task is executed, posters paths would be ready in "posters"
        new FetchMoviesTask().execute("YOUR KEY HERE"); // TODO: replace with your key

    }

    private void init() {
        mGridView = (GridView) findViewById(R.id.grid_movies);
        posters = new ArrayList<>();
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    movie = movies.getJSONObject(position);
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra(MOVIE_OVERVIEW_KEY, movie.getString(MOVIE_OVERVIEW_KEY));
                    intent.putExtra(MOVIE_TITLE_KEY, movie.getString(MOVIE_TITLE_KEY));
                    intent.putExtra(POSTER_PATH_KEY, posters.get(position));
                    startActivity(intent);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error getting movie from within GridView listner", e);
                }
            }
        });
    }

    /**
     * AsyncTask parameter is the API key
     * and it return the JSON response as a String
     */
    public class FetchMoviesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            // verifying parameters
            if (params.length == 0) {
                return null;
            }
            HttpURLConnection httpURLConnection = null;
            BufferedReader reader = null;

            try {
                String baseUrl = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=";
                baseUrl += params[0];
                URL url = new URL(baseUrl);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer buffer = new StringBuffer();

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // no data
                    return null;
                }

                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Connection Error: ", e);
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                try {
                    movies = new JSONObject(s).getJSONArray(MOVIES_KEY);
                    movie = null;
                    for (int i = 0; i < 20; i++) {
                        movie = movies.getJSONObject(i);
                        posters.add((BASE_POSTER_URL +
                                movie.getString(POSTER_PATH_KEY)));
                    }
                    // updating ui after getting posters
                    adapter = new MoviesGridAdapter(MainActivity.this, R.layout.grid_item_movies, posters);
                    mGridView.setAdapter(adapter);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error parsing JSON: ", e);
                }
            } else {
                Log.e(LOG_TAG, "Error: FetchMoviesTask null result.");
            }

        } // end of onPostExecute

    } // end of FetchMoviesTask

}
