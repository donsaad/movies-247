package com.donsaad.movies247;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

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

    public final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String API_KEY = ""; // TODO: replace with your key
    private static final String SORT_BY_POPULARITY = "popularity.desc";
    private static final String SORT_BY_RATE = "vote_average.desc";
    private static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185";
    public static final String MOVIE_OVERVIEW_KEY = "overview";
    public static final String MOVIE_VOTE_AVG_KEY = "vote_average";
    public static final String MOVIE_RELEASE_KEY = "release_date";
    public static final String MOVIE_TITLE_KEY = "original_title";
    public static final String POSTER_PATH_KEY = "poster_path";
    private static final String MOVIES_KEY = "results";

    private RecyclerView recyclerView;
    private List<Movie> movieList;
    private RecyclerAdapter adapter;
    private JSONArray movies;
    private JSONObject movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        new FetchMoviesTask().execute(SORT_BY_POPULARITY);

    }

    private void init() {
        movieList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_sort_by_popular) {
            new FetchMoviesTask().execute(SORT_BY_POPULARITY);
        }
        else if(id == R.id.action_sort_by_rate) {
            new FetchMoviesTask().execute(SORT_BY_RATE);
        }
        else if(id == R.id.action_sort_by_fav) {
            // // TODO: 12/25/2015 fav 
        }
        return super.onOptionsItemSelected(item);
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
                String baseUrl = "http://api.themoviedb.org/3/discover/movie?sort_by=" +
                        params[0] + "&api_key=" + API_KEY;
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
                    movieList.clear();
                    Movie m;
                    for (int i = 0; i < 20; i++) {
                        movie = movies.getJSONObject(i);
                        m = new Movie();
                        m.setPoster(BASE_POSTER_URL +
                                movie.getString(POSTER_PATH_KEY));
                        m.setOverview(movie.getString(MOVIE_OVERVIEW_KEY));
                        m.setVoteAverage(movie.getDouble(MOVIE_VOTE_AVG_KEY));
                        m.setReleaseDate(movie.getString(MOVIE_RELEASE_KEY));
                        m.setTitle(movie.getString(MOVIE_TITLE_KEY));
                        movieList.add(m);
                    }
                    // updating ui after getting posters
                    adapter = new RecyclerAdapter(MainActivity.this, movieList);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error parsing JSON: ", e);
                }
            } else {
                Log.e(LOG_TAG, "Error: FetchMoviesTask null result.");
            }

        } // end of onPostExecute

    } // end of FetchMoviesTask

}
