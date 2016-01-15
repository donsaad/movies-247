package com.donsaad.movies247.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.donsaad.movies247.DetailsActivity;
import com.donsaad.movies247.DetailsFragment;
import com.donsaad.movies247.R;

public class MainActivity extends AppCompatActivity implements MoviesFragment.Callback {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            Toast.makeText(MainActivity.this, "OnePane :D", Toast.LENGTH_SHORT).show();
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
