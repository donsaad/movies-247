package com.donsaad.movies247.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.donsaad.movies247.networking.DataFetchTask;
import com.donsaad.movies247.DetailActivity;
import com.donsaad.movies247.networking.OnDataFetchListener;
import com.donsaad.movies247.R;

import java.util.ArrayList;

public class MoviesActivity extends AppCompatActivity implements OnDataFetchListener {

    public final String LOG_TAG = MoviesActivity.class.getSimpleName();

    public static final String API_KEY = "425c4970c74d68d62b533df1a9f65f67"; // TODO: replace with your key
    private static final String FETCH_MOVIES_BY_POPULARITY = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=";
    private static final String FETCH_MOVIES_BY_RATE = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=";

    private GridView gridView;
    private ArrayList<Movie> moviesList;

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
        gridView = (GridView) findViewById(R.id.grid_movies);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MoviesActivity.this, DetailActivity.class);
                intent.putExtra(Movie.MOVIE_POSTER_PATH_KEY, moviesList.get(position).getPoster());
                intent.putExtra(Movie.MOVIE_OVERVIEW_KEY, moviesList.get(position).getOverview());
                intent.putExtra(Movie.MOVIE_VOTE_AVG_KEY, moviesList.get(position).getVoteAverage());
                intent.putExtra(Movie.MOVIE_RELEASE_KEY, moviesList.get(position).getReleaseDate());
                intent.putExtra(Movie.MOVIE_TITLE_KEY, moviesList.get(position).getTitle());
                intent.putExtra(Movie.MOVIE_ID_KEY, moviesList.get(position).getId());
                startActivity(intent);
            }
        });
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
        gridView.setAdapter(new MovieGridAdapter(MoviesActivity.this, moviesList));
    }

    @Override
    public void onDataError(int errorCode) {
        Log.e(LOG_TAG, "Error: DataFetchTask Error Code: " + errorCode);
    }


}
