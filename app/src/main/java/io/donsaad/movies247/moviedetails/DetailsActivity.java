package io.donsaad.movies247.moviedetails;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import io.donsaad.movies247.movies.Movie;

public class DetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(io.donsaad.movies247.R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            setTitle(extras.getString(Movie.MOVIE_TITLE_KEY));
            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(extras);
            getSupportFragmentManager().beginTransaction()
                    .add(io.donsaad.movies247.R.id.movie_detail_container, fragment)
                    .commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
