package io.donsaad.movies247.movies;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import io.donsaad.movies247.R;
import io.donsaad.movies247.moviedetails.DetailsActivity;
import io.donsaad.movies247.moviedetails.DetailsFragment;
import io.donsaad.movies247.utils.Constants;

public class MoviesActivity extends AppCompatActivity implements MoviesFragment.Callback {

    public static boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        if(findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if(savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailsFragment())
                        .commit();
            }
        }
        else {
            mTwoPane = false;
        }
    }


    @Override
    public void onItemSelected(Movie selectedMovie) {
        if(mTwoPane) {
            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(selectedMovie.asBundle());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();
        }
        else {
            Intent intent = new Intent(this, DetailsActivity.class);
                    intent.putExtra(Constants.MOVIE_PARCEL_KEY, selectedMovie);
            startActivity(intent);
        }
    }
}
