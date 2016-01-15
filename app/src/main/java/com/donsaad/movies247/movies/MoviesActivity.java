package com.donsaad.movies247.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.donsaad.movies247.moviedetails.DetailsActivity;
import com.donsaad.movies247.moviedetails.DetailsFragment;
import com.donsaad.movies247.R;

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
    public void onItemSelected(Bundle data) {
        if(mTwoPane) {
            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(data);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();
        }
        else {
            Intent intent = new Intent(this, DetailsActivity.class)
                    .putExtras(data);
            startActivity(intent);
        }
    }
}