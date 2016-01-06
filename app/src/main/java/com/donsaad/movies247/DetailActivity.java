package com.donsaad.movies247;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private final String LOG_TAG = DetailActivity.class.getSimpleName();

    private TextView synopsis;
    private TextView title;
    private TextView date;
    private TextView vote;
    private ImageView poster;

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
    }

    private void setDataIntoViews() {
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            synopsis.setText(extras.getString(MainActivity.MOVIE_OVERVIEW_KEY));
            title.setText(extras.getString(MainActivity.MOVIE_TITLE_KEY));
            date.setText(extras.getString(MainActivity.MOVIE_RELEASE_KEY));
            vote.setText(extras.getDouble(MainActivity.MOVIE_VOTE_AVG_KEY) + "/10");
            Picasso.with(this).load(extras.getString(MainActivity.POSTER_PATH_KEY)).into(poster);
        }
        else {
            Log.e(LOG_TAG, "Error getting extras!");
        }
    }
}
