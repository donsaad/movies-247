package io.donsaad.movies247.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.donsaad.movies247.moviedetails.DetailsActivity;
import io.donsaad.movies247.moviedetails.DetailsFragment;

public class MoviesActivity extends AppCompatActivity implements MoviesFragment.Callback {

    public static boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(io.donsaad.movies247.R.layout.activity_movies);

        if(findViewById(io.donsaad.movies247.R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if(savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(io.donsaad.movies247.R.id.movie_detail_container, new DetailsFragment())
                        .commit();
            }
        }
        else {
            mTwoPane = false;
        }
    }


    @Override
    public void onItemSelected(Bundle data) {
        if(mTwoPane) {
            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(data);
            getSupportFragmentManager().beginTransaction()
                    .replace(io.donsaad.movies247.R.id.movie_detail_container, fragment)
                    .commit();
        }
        else {
            Intent intent = new Intent(this, DetailsActivity.class)
                    .putExtras(data);
            startActivity(intent);
        }
    }
}
