package io.donsaad.movies247.movies;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.donsaad.movies247.utils.Constants;

/**
 * Created by donsaad on 1/7/2016.
 * Parser class to parse JSON string of movies
 */
public class MovieParser {
    
    public ArrayList<Movie> parseJson(String s) {
        ArrayList<Movie> movies = null;
        Movie movie;
        JSONArray array;
        JSONObject object;
        try {
            array = (new JSONObject(s)).getJSONArray(Constants.MOVIES_KEY);
            movies = new ArrayList<>();
            for (int i = 0, length = array.length(); i < length; i++) {
                object = array.getJSONObject(i);
                movie = new Movie();
                movie.setPoster(Constants.MOVIE_BASE_POSTER_URL +
                        object.getString(Constants.MOVIE_POSTER_PATH_KEY));
                movie.setOverview(object.getString(Constants.MOVIE_OVERVIEW_KEY));
                movie.setVoteAverage(object.getDouble(Constants.MOVIE_VOTE_AVG_KEY));
                movie.setReleaseDate(object.getString(Constants.MOVIE_RELEASE_KEY));
                movie.setTitle(object.getString(Constants.MOVIE_TITLE_KEY));
                movie.setId(object.getInt(Constants.MOVIE_ID_KEY));
                movies.add(movie);
            }


        } catch (JSONException e) {
            Log.e("MovieParser", "Error parsing Movie JSON: ", e);
            //// TODO: 1/15/2016 don't hide errors show it to user later
        }

        return movies;
    }

}