package io.donsaad.movies247.moviedetails;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;

import io.donsaad.movies247.R;
import io.donsaad.movies247.database.DatabaseSource;
import io.donsaad.movies247.movies.Movie;
import io.donsaad.movies247.networking.DataFetchTask;
import io.donsaad.movies247.networking.OnDataFetchListener;
import io.donsaad.movies247.reviews.Review;
import io.donsaad.movies247.reviews.ReviewParser;
import io.donsaad.movies247.trailers.Trailer;
import io.donsaad.movies247.trailers.TrailerParser;
import io.donsaad.movies247.utils.Constants;

/**
 * Created by donsaad on 1/15/2016.
 * fragment for the detail screen
 */
public class DetailsFragment extends Fragment implements View.OnClickListener, ListView.OnItemClickListener {


    private final String LOG_TAG = DetailsFragment.class.getSimpleName();

    private ArrayList<Trailer> trailers;
    private ArrayList<ListItem> items;
    private TextView synopsis;
    private TextView title;
    private TextView date;
    private TextView vote;
    private ImageView poster;
    private String movieID;
    private ListView listView;
    private Context mContext;
    private Movie mMovie;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        init(rootView);

        Bundle arguments = getArguments();
        if (arguments != null) {
            makeMovie(arguments);
            setDataIntoViews();
        }

        final DataFetchTask reviewFetchTask = new DataFetchTask();
        reviewFetchTask.setOnDataFetchListener(new OnDataFetchListener() {
            @Override
            public void onDataFetched(String data) {
                ReviewParser parser = new ReviewParser();
                ArrayList<Review> reviews = new ArrayList<>();
                try {
                    reviews = parser.parseJson(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                    // TODO: 1/9/2016 notify user
                }
                for (Review v : reviews) {
                    items.add(v);
                }
                listView.setAdapter(new MovieDetailsListAdapter(getContext(), items));
            }

            @Override
            public void onDataError(int errorCode) {
                // TODO: 1/9/2016 notify user
            }
        });

        DataFetchTask trailerFetchTask = new DataFetchTask();
        trailerFetchTask.setOnDataFetchListener(new OnDataFetchListener() {
            @Override
            public void onDataFetched(String data) {
                TrailerParser parser = new TrailerParser();
                try {
                    trailers = parser.parseJson(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                    // TODO: 1/9/2016 notify user of exception
                }
                items = new ArrayList<>();
                for (Trailer t : trailers) {
                    items.add(t);
                }
                reviewFetchTask.execute(Constants.DATA_FETCH_URL + movieID + Constants.REVIEW_ENDPOINT);
            }

            @Override
            public void onDataError(int errorCode) {
                // TODO: 1/9/2016 notify user of errors
            }
        });
        if (movieID != null)
            trailerFetchTask.execute(Constants.DATA_FETCH_URL + movieID + Constants.TRAILER_ENDPOINT);

        return rootView;
    }

    private void makeMovie(Bundle arguments) {
        mMovie = new Movie();
        mMovie.setOverview(arguments.getString(Constants.MOVIE_OVERVIEW_KEY));
        mMovie.setTitle(arguments.getString(Constants.MOVIE_TITLE_KEY));
        mMovie.setReleaseDate(arguments.getString(Constants.MOVIE_RELEASE_KEY));
        mMovie.setVoteAverage(arguments.getDouble(Constants.MOVIE_VOTE_AVG_KEY));
        mMovie.setId(arguments.getInt(Constants.MOVIE_ID_KEY));
        mMovie.setPoster(arguments.getString(Constants.MOVIE_POSTER_PATH_KEY));
    }

    private void init(View rootView) {
        mContext = getContext();
        trailers = new ArrayList<>();

        listView = (ListView) rootView.findViewById(R.id.lv_trailers_reviews);
        listView.addHeaderView(View.inflate(mContext, R.layout.list_header_details, null));
        synopsis = (TextView) listView.findViewById(R.id.tv_overview);
        title = (TextView) listView.findViewById(R.id.tv_title_detail);
        date = (TextView) listView.findViewById(R.id.tv_release_date);
        vote = (TextView) listView.findViewById(R.id.tv_vote);
        Button fav = (Button) listView.findViewById(R.id.btn_fav);
        poster = (ImageView) listView.findViewById(R.id.img_poster);
        /**
         * listeners for views
         */
        listView.setOnItemClickListener(this);
        fav.setOnClickListener(this);

//        fav.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences preferences = getActivity()
//                        .getSharedPreferences(Constants.MOVIES_PREF_NAME, Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = preferences.edit();
//                JSONObject jsonObj = new JSONObject();
//                JSONObject jsonInner = new JSONObject();
//                JSONArray array = new JSONArray();
//                String jsonStr = null;
//                String toBeInserted = null;
//                /**
//                 * appending a movie as a JSON string to existing JSON string
//                 */
//                if (preferences.contains(Constants.MOVIE_FAV_PREF_KEY)) {
//                    try {
//                        jsonStr = preferences.getString(Constants.MOVIE_FAV_PREF_KEY, null);
//                        jsonStr = jsonStr.substring(0, jsonStr.length() - 2);
//                        jsonStr += ",";
//
//                        jsonInner.put(Constants.MOVIE_ID_KEY, Integer.parseInt(movieID.trim()));
//                        jsonInner.put(Constants.MOVIE_TITLE_KEY, title.getText().toString().trim());
//                        jsonInner.put(Constants.MOVIE_OVERVIEW_KEY, synopsis.getText().toString().trim());
//                        jsonInner.put(Constants.MOVIE_RELEASE_KEY, date.getText().toString().trim());
//                        jsonInner.put(Constants.MOVIE_VOTE_AVG_KEY, Double.parseDouble((vote.getText()
//                                .toString().trim())));
//                        jsonInner.put(Constants.MOVIE_POSTER_PATH_KEY, posterPath.trim());
//
//                        jsonStr += jsonInner.toString().trim();
//                        jsonStr += "]}"; // closing the json array and the json object
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    Log.i(LOG_TAG, jsonStr);
//                    editor.putString(Constants.MOVIE_FAV_PREF_KEY, jsonStr);
//                }
//                /**
//                 * this else is a first time to mark a fav,
//                 * so I make a json array inside a json object
//                 * to use the @parseJson method in the @MovieParser class
//                 */
//                else {
//                    try {
//                        jsonInner.put(Constants.MOVIE_ID_KEY, movieID.trim());
//                        jsonInner.put(Constants.MOVIE_TITLE_KEY, title.getText().toString().trim());
//                        jsonInner.put(Constants.MOVIE_OVERVIEW_KEY, synopsis.getText().toString().trim());
//                        jsonInner.put(Constants.MOVIE_RELEASE_KEY, date.getText().toString().trim());
//                        jsonInner.put(Constants.MOVIE_VOTE_AVG_KEY, Double.parseDouble((vote.getText()
//                                .toString().trim())));
//                        jsonInner.put(Constants.MOVIE_POSTER_PATH_KEY, posterPath.trim());
//                        array.put(jsonInner);
//                        jsonObj.put(Constants.MOVIES_KEY, array);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    editor.putString(Constants.MOVIE_FAV_PREF_KEY, jsonObj.toString().trim());
//                }
//                editor.apply();
//                Toast.makeText(mContext, "Added to favorites", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void setDataIntoViews() {
        if (mMovie != null) {
            synopsis.setText(mMovie.getOverview());
            title.setText(mMovie.getTitle());
            date.setText(mMovie.getReleaseDate());
            vote.setText(String.valueOf(mMovie.getVoteAverage()));
            movieID = String.valueOf(mMovie.getId());
            Picasso.with(mContext).load(mMovie.getPoster()).into(poster);
        } else {
            Log.e(LOG_TAG, "Error getting extras!");
            // TODO: 1/15/2016 notify user to know what went wrong
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_fav:
                DatabaseSource source = new DatabaseSource(mContext);
                if(source.insertMovie(mMovie)) {
                    Toast.makeText(mContext, "Saved to favorites.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(mContext, "This move is already marked as favorite", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (listView.getAdapter().getItemViewType(position)
                == MovieDetailsListAdapter.RowType.TRAILER_ITEM.ordinal()) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(Constants.BASE_YOUTUBE_URL + trailers.get(position-1).getKey()));
            startActivity(intent);
        }
    }

}
