package com.donsaad.movies247;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MoviesActivity extends AppCompatActivity implements OnDataFetchListener {

    public final String LOG_TAG = MoviesActivity.class.getSimpleName();

    public static final String API_KEY = "425c4970c74d68d62b533df1a9f65f67"; // TODO: replace with your key
    private static final String FETCH_MOVIES_BY_POPULARITY = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=";
    private static final String FETCH_MOVIES_BY_RATE = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=";
    public static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185";
    public static final String MOVIE_OVERVIEW_KEY = "overview";
    public static final String MOVIE_VOTE_AVG_KEY = "vote_average";
    public static final String MOVIE_RELEASE_KEY = "release_date";
    public static final String MOVIE_TITLE_KEY = "original_title";
    public static final String MOVIE_POSTER_PATH_KEY = "poster_path";
    public static final String MOVIES_KEY = "results";

    private RecyclerView recyclerView;
    private List<Movie> moviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        DataFetchTask dataFetchTask = new DataFetchTask();
        dataFetchTask.setOnDataFetchListener(this);
        dataFetchTask.execute(FETCH_MOVIES_BY_POPULARITY);

    }

    private void init() {
        moviesList = new ArrayList<>();
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
        if (id == R.id.action_sort_by_popular) {
            DataFetchTask dataFetchTask = new DataFetchTask();
            dataFetchTask.setOnDataFetchListener(this);
            dataFetchTask.execute(FETCH_MOVIES_BY_POPULARITY);
        } else if (id == R.id.action_sort_by_rate) {
            DataFetchTask dataFetchTask = new DataFetchTask();
            dataFetchTask.setOnDataFetchListener(this);
            dataFetchTask.execute(FETCH_MOVIES_BY_RATE);
        } else if (id == R.id.action_sort_by_fav) {
            // // TODO: 12/25/2015 fav 
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataFetched(String data) {
        MovieParser parser = new MovieParser();
        moviesList = parser.parseJson(data);
        recyclerView.setAdapter(new MovieRecyclerAdapter(MoviesActivity.this, moviesList));
    }

    @Override
    public void onDataError(int errorCode) {
        Log.e(LOG_TAG, "Error: DataFetchTask Error Code: " + errorCode);
    }


}
