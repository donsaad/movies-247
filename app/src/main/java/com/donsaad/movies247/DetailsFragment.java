package com.donsaad.movies247;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.donsaad.movies247.movies.Movie;
import com.donsaad.movies247.networking.DataFetchTask;
import com.donsaad.movies247.networking.OnDataFetchListener;
import com.donsaad.movies247.reviews.ReviewParser;
import com.donsaad.movies247.trailers.Trailer;
import com.donsaad.movies247.trailers.TrailerParser;
import com.donsaad.movies247.trailers.TrailersListAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by donsaad on 1/15/2016.
 * fragment for the detail screen
 */
public class DetailsFragment extends Fragment {

    private final String LOG_TAG = DetailsFragment.class.getSimpleName();
    private static final String DATA_FETCH_URL = "http://api.themoviedb.org/3/movie/";
    private static final String TRAILER_PARAM = "/videos?api_key=";
    private static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    private static final String REVIEW_PARAM = "/reviews?api_key=";


    private ArrayList<Trailer> trailers;
    private TextView synopsis;
    private TextView title;
    private TextView date;
    private TextView vote;
    private ImageView poster;
    private String movieID;
    private ListView trailersListView;
    private Context mContext;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        init(rootView);

        Bundle arguments = getArguments();
        if(arguments != null) {
            setDataIntoViews(arguments);
        }

        DataFetchTask trailerFetchTask = new DataFetchTask();
        trailerFetchTask.setOnDataFetchListener(new OnDataFetchListener() {
            @Override
            public void onDataFetched(String data) {
                TrailerParser parser = new TrailerParser();
                try {
                    trailers = parser.parseJson(data);
                    trailersListView.setAdapter(new TrailersListAdapter(getContext(), trailers));
                } catch (JSONException e) {
                    e.printStackTrace();
                    // TODO: 1/9/2016 notify user of exception
                }
            }

            @Override
            public void onDataError(int errorCode) {
                // TODO: 1/9/2016 notify user of errors
            }
        });
        if(movieID != null)
            trailerFetchTask.execute(DATA_FETCH_URL + movieID + TRAILER_PARAM);

        DataFetchTask reviewFetchTask = new DataFetchTask();
        reviewFetchTask.setOnDataFetchListener(new OnDataFetchListener() {
            @Override
            public void onDataFetched(String data) {
                ReviewParser parser = new ReviewParser();
                try {
                    parser.parseJson(data);
                    // TODO: 1/9/2016 hook UI to data
                } catch (JSONException e) {
                    e.printStackTrace();
                    // TODO: 1/9/2016 notify user
                }
            }

            @Override
            public void onDataError(int errorCode) {
                // TODO: 1/9/2016 notify user
            }
        });
        if(movieID != null)
            reviewFetchTask.execute(DATA_FETCH_URL + movieID + REVIEW_PARAM);
        return rootView;
    }
    private void init(View rootView) {
        mContext = getContext();
        trailers = new ArrayList<>();

        synopsis = (TextView) rootView.findViewById(R.id.tv_overview);
        title = (TextView) rootView.findViewById(R.id.tv_title_detail);
        date = (TextView) rootView.findViewById(R.id.tv_release_date);
        vote = (TextView) rootView.findViewById(R.id.tv_vote);
        poster = (ImageView) rootView.findViewById(R.id.img_poster);
        trailersListView = (ListView) rootView.findViewById(R.id.lv_trailers);
        trailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(BASE_YOUTUBE_URL + trailers.get(position).getKey()));
                startActivity(intent);
            }
        });
    }

    private void setDataIntoViews(Bundle arguments) {
        if (arguments != null) {
            synopsis.setText(arguments.getString(Movie.MOVIE_OVERVIEW_KEY));
            title.setText(arguments.getString(Movie.MOVIE_TITLE_KEY));
            date.setText(arguments.getString(Movie.MOVIE_RELEASE_KEY));
            vote.setText(arguments.getDouble(Movie.MOVIE_VOTE_AVG_KEY) + "/10");
            movieID = "" + arguments.getInt(Movie.MOVIE_ID_KEY);
            Picasso.with(mContext).load(arguments.getString(Movie.MOVIE_POSTER_PATH_KEY)).into(poster);
        } else {
            Log.e(LOG_TAG, "Error getting extras!");
            // TODO: 1/15/2016 notify user to know what went wrong
        }
    }

}
