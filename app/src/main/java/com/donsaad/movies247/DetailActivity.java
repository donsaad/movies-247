package com.donsaad.movies247;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

public class DetailActivity extends AppCompatActivity {

    private final String LOG_TAG = DetailActivity.class.getSimpleName();

    private static final String TRAILER_KEY = "results";
    private static final String TRAILER_ID_KEY = "id";
    private static final String TRAILER_KEY_KEY = "key"; // key key lool
    public static final String BASE_TRAILER_URL = "https://www.youtube.com/watch?v=";
    private List<String> keys;
    private List<String> reviews;



    private TextView synopsis;
    private TextView title;
    private TextView date;
    private TextView vote;
    private ImageView poster;

    private ListView listViewTrailers;
    private ListView listViewReviews;
    private TrailerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initViews();

        setDataIntoViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        keys = new ArrayList<>();
        reviews = new ArrayList<>();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= 21) {
            actionBar.setElevation(0f);
        }
        synopsis = (TextView) findViewById(R.id.tv_overview);
        title = (TextView) findViewById(R.id.tv_title);
        date = (TextView) findViewById(R.id.tv_release_date);
        vote = (TextView) findViewById(R.id.tv_vote);
        poster = (ImageView) findViewById(R.id.img_poster);
        listViewTrailers = (ListView) findViewById(R.id.list_trailers);
        listViewReviews = (ListView) findViewById(R.id.list_reviews);

        listViewTrailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(keys.get(position)));
                startActivity(intent);
            }
        });


    }

    private void setDataIntoViews() {
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            new FetchTrailerTask().execute(extras.getInt(MainActivity.MOVIE_ID_KEY));
            new FetchReviewsTask().execute(extras.getInt(MainActivity.MOVIE_ID_KEY));
            synopsis.setText(extras.getString(MainActivity.MOVIE_OVERVIEW_KEY));
            title.setText(extras.getString(MainActivity.MOVIE_TITLE_KEY));
            date.setText(extras.getString(MainActivity.MOVIE_RELEASE_KEY));
            vote.setText(extras.getString(MainActivity.MOVIE_VOTE_AVG_KEY) + "/10");
            Picasso.with(this).load(extras.getString(MainActivity.POSTER_PATH_KEY)).into(poster);
        }
        else {
            Log.e(LOG_TAG, "Error getting extras!");
        }
    }

    public class FetchTrailerTask extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... params) {
            // verifying parameters
            if (params.length == 0) {
                return null;
            }
            HttpURLConnection httpURLConnection = null;
            BufferedReader reader = null;

            try {
                String baseUrl = "http://api.themoviedb.org/3/movie/" +
                        params[0] + "/videos?" + "&api_key=" + MainActivity.API_KEY;
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
            JSONArray trailers;
            JSONObject trailer;
            if (s != null) {
                try {
                    trailers = new JSONObject(s).getJSONArray(TRAILER_KEY);
                    trailer = null;
                    keys.clear();

                    for(int i =0; i < trailers.length(); i++) {
                        trailer = trailers.getJSONObject(0);
                        keys.add((BASE_TRAILER_URL +
                                trailer.getString(TRAILER_KEY_KEY)));
                    }

                    // updating ui after getting posters
                    adapter = new TrailerListAdapter(DetailActivity.this, R.layout.list_item_trailers, keys);
                    listViewTrailers.setAdapter(adapter);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error parsing JSON: ", e);
                }
            } else {
                Log.e(LOG_TAG, "Error: FetchMoviesTask null result.");
            }

        } // end of onPostExecute

    } // end of FetchMoviesTask

    public class FetchReviewsTask extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... params) {
            // verifying parameters
            if (params.length == 0) {
                return null;
            }
            HttpURLConnection httpURLConnection = null;
            BufferedReader reader = null;

            try {
                String baseUrl = "http://api.themoviedb.org/3/movie/" +
                        params[0] + "/reviews?" + "&api_key=" + MainActivity.API_KEY;
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
            JSONArray trailers;
            JSONObject trailer;
            if (s != null) {
                try {
                    trailers = new JSONObject(s).getJSONArray(TRAILER_KEY);
                    reviews.clear();

                    for (int i = 0; i < trailers.length(); i++) {
                        trailer = trailers.getJSONObject(i);
                        reviews.add(trailer.getString("content"));
                    }
                    // updating ui after getting posters
                    listViewReviews.setAdapter(new ReviewsListAdapter(DetailActivity.this, R.layout.list_item_reviews, reviews));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error parsing JSON: ", e);
                }
            } else {
                Log.e(LOG_TAG, "Error: FetchMoviesTask null result.");
            }

        } // end of onPostExecute

    } // end of FetchMoviesTask

}
