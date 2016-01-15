package com.donsaad.movies247.movies;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.donsaad.movies247.R;
import com.donsaad.movies247.moviedetails.DetailsFragment;
import com.donsaad.movies247.networking.DataFetchTask;
import com.donsaad.movies247.networking.OnDataFetchListener;

import java.util.ArrayList;

/**
 * Created by donsaad on 1/15/2016.
 * fragment for the main screen
 */
public class MoviesFragment extends Fragment implements OnDataFetchListener {

    private static final String FETCH_MOVIES_BY_POPULARITY = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=";
    private static final String FETCH_MOVIES_BY_RATE = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=";

    private ArrayList<Movie> moviesList;
    private GridView mGridView;

    public MoviesFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        DataFetchTask dataFetchTask = new DataFetchTask();
        dataFetchTask.setOnDataFetchListener(this);
        dataFetchTask.execute(FETCH_MOVIES_BY_POPULARITY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.grid_movies);

        moviesList = new ArrayList<>();
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle args = new Bundle();
                args.putString(Movie.MOVIE_POSTER_PATH_KEY, moviesList.get(position).getPoster());
                args.putString(Movie.MOVIE_OVERVIEW_KEY, moviesList.get(position).getOverview());
                args.putString(Movie.MOVIE_RELEASE_KEY, moviesList.get(position).getReleaseDate());
                args.putString(Movie.MOVIE_TITLE_KEY, moviesList.get(position).getTitle());
                args.putDouble(Movie.MOVIE_VOTE_AVG_KEY, moviesList.get(position).getVoteAverage());
                args.putInt(Movie.MOVIE_ID_KEY, moviesList.get(position).getId());
                ((Callback) getActivity()).onItemSelected(args);
            }
        });
        return rootView;
    }

    @Override
    public void onDataFetched(String data) {
        MovieParser parser = new MovieParser();
        moviesList = parser.parseJson(data);
        mGridView.setAdapter(new MovieGridAdapter(getContext(), moviesList));
        if(MoviesActivity.mTwoPane)
            ((Callback)getActivity()).onItemSelected(moviesList.get(0).asBundle());
    }

    @Override
    public void onDataError(int errorCode) {
        Log.e("MoviesFragment", "Error: DataFetchTask Error Code: " + errorCode);
        // TODO: 1/15/2016 propagate error to user
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
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
            SharedPreferences preferences = getActivity()
                    .getSharedPreferences(DetailsFragment.MOVIES_PREF_NAME,
                            Context.MODE_PRIVATE);
            MovieParser parser = new MovieParser();
            moviesList = parser.parseJson(preferences.getString(Movie.MOVIE_FAV_PREF_KEY, null));
            mGridView.setAdapter(new MovieGridAdapter(getContext(), moviesList));
            if(MoviesActivity.mTwoPane)
                ((Callback)getActivity()).onItemSelected(moviesList.get(0).asBundle());
        }
        return super.onOptionsItemSelected(item);
    }

    public interface Callback {
        void onItemSelected(Bundle data);
    }

}
