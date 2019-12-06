package io.donsaad.movies247.moviedetails;

import android.os.Bundle;

import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import io.donsaad.movies247.R;
import io.donsaad.movies247.movies.Movie;
import io.donsaad.movies247.utils.Constants;

public class DetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(savedInstanceState == null) {
            Movie movie = getIntent().getExtras().getParcelable(Constants.MOVIE_PARCEL_KEY);
            setTitle(movie.getTitle());
            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(movie.asBundle());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
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
