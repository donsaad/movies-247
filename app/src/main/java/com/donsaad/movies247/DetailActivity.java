package com.donsaad.movies247;

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

import com.donsaad.movies247.movies.Movie;
import com.donsaad.movies247.networking.DataFetchTask;
import com.donsaad.movies247.networking.OnDataFetchListener;
import com.donsaad.movies247.trailers.Trailer;
import com.donsaad.movies247.trailers.TrailerParser;
import com.donsaad.movies247.trailers.TrailersListAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements OnDataFetchListener {

    private final String LOG_TAG = DetailActivity.class.getSimpleName();
    private static final String FETCH_TRAILERS = "http://api.themoviedb.org/3/movie/";
    private static final String TRAILER_PARAM = "/videos?api_key=";

    private ArrayList<Trailer> trailers;
    private TextView synopsis;
    private TextView title;
    private TextView date;
    private TextView vote;
    private ImageView poster;
    private String movieId;
    private ListView trailersListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
        setDataIntoViews();

        DataFetchTask dataFetchTask = new DataFetchTask();
        dataFetchTask.setOnDataFetchListener(this);
        dataFetchTask.execute(FETCH_TRAILERS + movieId + TRAILER_PARAM);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        trailers = new ArrayList<>();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= 21) {
            actionBar.setElevation(0f);
        }
        synopsis = (TextView) findViewById(R.id.tv_overview);
        title = (TextView) findViewById(R.id.tv_card_title);
        date = (TextView) findViewById(R.id.tv_release_date);
        vote = (TextView) findViewById(R.id.tv_vote);
        poster = (ImageView) findViewById(R.id.img_poster);
        trailersListView = (ListView) findViewById(R.id.lv_trailers);
        trailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: 1/9/2016 check Youtube URL
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailers.get(position).getKey()));
//                startActivity(intent);
            }
        });
    }

    private void setDataIntoViews() {
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            synopsis.setText(extras.getString(Movie.MOVIE_OVERVIEW_KEY));
            title.setText(extras.getString(Movie.MOVIE_TITLE_KEY));
            date.setText(extras.getString(Movie.MOVIE_RELEASE_KEY));
            vote.setText(extras.getDouble(Movie.MOVIE_VOTE_AVG_KEY) + "/10");
            movieId =  "" + extras.getInt(Movie.MOVIE_ID_KEY);
            Picasso.with(this).load(extras.getString(Movie.MOVIE_POSTER_PATH_KEY)).into(poster);
        }
        else {
            Log.e(LOG_TAG, "Error getting extras!");
        }
    }

    @Override
    public void onDataFetched(String data) {
        TrailerParser parser = new TrailerParser();
        try {
            trailers = parser.parseJson(data);
            trailersListView.setAdapter(new TrailersListAdapter(DetailActivity.this, trailers));
        } catch (JSONException e) {
            e.printStackTrace();
            // TODO: 1/9/2016 notify user of exception
        }

    }

    @Override
    public void onDataError(int errorCode) {
        // TODO: 1/9/2016 notify user of error
    }
}
