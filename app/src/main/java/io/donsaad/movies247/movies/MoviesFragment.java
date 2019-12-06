package io.donsaad.movies247.movies;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import io.donsaad.movies247.R;
import io.donsaad.movies247.database.DatabaseSource;
import io.donsaad.movies247.networking.DataFetchTask;
import io.donsaad.movies247.networking.OnDataFetchListener;

/**
 * Created by donsaad on 1/15/2016.
 * fragment for the main screen
 */
public class MoviesFragment extends Fragment implements OnDataFetchListener {

    private static final String FETCH_MOVIES_BY_POPULARITY = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=";
    private static final String FETCH_MOVIES_BY_RATE = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=";

    private ArrayList<Movie> moviesList;
    private GridView mGridView;
    private Context mContext;

    public MoviesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        DataFetchTask dataFetchTask = new DataFetchTask();
        dataFetchTask.setOnDataFetchListener(this);
        dataFetchTask.execute(FETCH_MOVIES_BY_POPULARITY);
        mContext = getActivity();
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

                ((Callback) getActivity()).onItemSelected(moviesList.get(position));
            }
        });
        return rootView;
    }

    @Override
    public void onDataFetched(String data) {
        MovieParser parser = new MovieParser();
        moviesList = parser.parseJson(data);
        mGridView.setAdapter(new MovieGridAdapter(getContext(), moviesList));
        if (MoviesActivity.mTwoPane)
            ((Callback) getActivity()).onItemSelected(moviesList.get(0));
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
            DatabaseSource source = new DatabaseSource(mContext);
            moviesList = (ArrayList<Movie>) source.getFavorites();
            if (moviesList != null)
                mGridView.setAdapter(new MovieGridAdapter(mContext, moviesList));
            else
                Toast.makeText(mContext, "You have no saved favorites.", Toast.LENGTH_SHORT).show();
            if (MoviesActivity.mTwoPane)
                ((Callback) getActivity()).onItemSelected(moviesList.get(0));
        }
        return super.onOptionsItemSelected(item);
    }

    public interface Callback {
        void onItemSelected(Movie selectedMovie);
    }

}
