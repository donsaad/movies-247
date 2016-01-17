package com.donsaad.movies247.moviedetails;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.donsaad.movies247.R;
import com.donsaad.movies247.movies.Movie;
import com.donsaad.movies247.networking.DataFetchTask;
import com.donsaad.movies247.networking.OnDataFetchListener;
import com.donsaad.movies247.reviews.ReviewParser;
import com.donsaad.movies247.trailers.Trailer;
import com.donsaad.movies247.trailers.TrailerParser;
import com.donsaad.movies247.trailers.TrailersListAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by donsaad on 1/15/2016.
 * fragment for the detail screen
 */
public class DetailsFragment extends Fragment {

    public static final String MOVIES_PREF_NAME = "MOVIES_PREF";
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
    private Button fav;
    private ImageView poster;
    private String movieID;
    private String posterPath;
    private ListView trailersListView;
    private Context mContext;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        init(rootView);

        Bundle arguments = getArguments();
        if (arguments != null) {
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
        if (movieID != null)
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
        if (movieID != null)
            reviewFetchTask.execute(DATA_FETCH_URL + movieID + REVIEW_PARAM);
        return rootView;
    }

    private void init(View rootView) {
        mContext = getContext();
        trailers = new ArrayList<>();
        posterPath = null;

        synopsis = (TextView) rootView.findViewById(R.id.tv_overview);
        title = (TextView) rootView.findViewById(R.id.tv_title_detail);
        date = (TextView) rootView.findViewById(R.id.tv_release_date);
        vote = (TextView) rootView.findViewById(R.id.tv_vote);
        fav = (Button) rootView.findViewById(R.id.btn_fav);
        poster = (ImageView) rootView.findViewById(R.id.img_poster);
        trailersListView = (ListView) rootView.findViewById(R.id.lv_trailers);
        /**
         * listeners for views
         */
        trailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(BASE_YOUTUBE_URL + trailers.get(position).getKey()));
                startActivity(intent);
            }
        });

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getActivity()
                        .getSharedPreferences(MOVIES_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                JSONObject jsonObj = new JSONObject();
                JSONObject jsonInner = new JSONObject();
                JSONArray array = new JSONArray();
                String jsonStr = null;
                String toBeInserted = null;
                /**
                 * appending a movie as a JSON string to existing JSON string
                 */
                if (preferences.contains(Movie.MOVIE_FAV_PREF_KEY)) {
                    try {
                        jsonStr = preferences.getString(Movie.MOVIE_FAV_PREF_KEY, null);
                        jsonStr = jsonStr.substring(0, jsonStr.length() - 2);
                        jsonStr += ",";

                        jsonInner.put(Movie.MOVIE_ID_KEY, Integer.parseInt(movieID.trim()));
                        jsonInner.put(Movie.MOVIE_TITLE_KEY, title.getText().toString().trim());
                        jsonInner.put(Movie.MOVIE_OVERVIEW_KEY, synopsis.getText().toString().trim());
                        jsonInner.put(Movie.MOVIE_RELEASE_KEY, date.getText().toString().trim());
                        jsonInner.put(Movie.MOVIE_VOTE_AVG_KEY, Double.parseDouble((vote.getText()
                                .toString().trim())));
                        jsonInner.put(Movie.MOVIE_POSTER_PATH_KEY, posterPath.trim());

                        jsonStr += jsonInner.toString().trim();
                        jsonStr += "]}"; // closing the json array and the json object
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.i(LOG_TAG, jsonStr);
                    editor.putString(Movie.MOVIE_FAV_PREF_KEY, jsonStr);
                }
                /**
                 * this else is a first time to mark a fav,
                 * so I make a json array inside a json object
                 * to use the @parseJson method in the @MovieParser class
                 */
                else {
                    try {
                        jsonInner.put(Movie.MOVIE_ID_KEY, movieID.trim());
                        jsonInner.put(Movie.MOVIE_TITLE_KEY, title.getText().toString().trim());
                        jsonInner.put(Movie.MOVIE_OVERVIEW_KEY, synopsis.getText().toString().trim());
                        jsonInner.put(Movie.MOVIE_RELEASE_KEY, date.getText().toString().trim());
                        jsonInner.put(Movie.MOVIE_VOTE_AVG_KEY, Double.parseDouble((vote.getText()
                                .toString().trim())));
                        jsonInner.put(Movie.MOVIE_POSTER_PATH_KEY, posterPath.trim());
                        array.put(jsonInner);
                        jsonObj.put(Movie.MOVIES_KEY, array);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    editor.putString(Movie.MOVIE_FAV_PREF_KEY, jsonObj.toString().trim());
                }
                editor.apply();
                Toast.makeText(mContext, "Added to favorites", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDataIntoViews(Bundle arguments) {
        if (arguments != null) {
            synopsis.setText(arguments.getString(Movie.MOVIE_OVERVIEW_KEY));
            title.setText(arguments.getString(Movie.MOVIE_TITLE_KEY));
            date.setText(arguments.getString(Movie.MOVIE_RELEASE_KEY));
            vote.setText(arguments.getDouble(Movie.MOVIE_VOTE_AVG_KEY)+"");
            movieID = "" + arguments.getInt(Movie.MOVIE_ID_KEY);
            posterPath = arguments.getString(Movie.MOVIE_POSTER_PATH_KEY).trim();
            Picasso.with(mContext).load(posterPath).into(poster);
        } else {
            Log.e(LOG_TAG, "Error getting extras!");
            // TODO: 1/15/2016 notify user to know what went wrong
        }
    }

}
