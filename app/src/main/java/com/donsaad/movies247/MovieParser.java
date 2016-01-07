package com.donsaad.movies247;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by donsaad on 1/7/2016.
 */
public class MovieParser {


    public ArrayList<Movie> parseJson(String s) {
        ArrayList<Movie> movies = null;
        Movie movie;
        JSONArray array;
        JSONObject object;
        try {
            array = (new JSONObject(s)).getJSONArray(MoviesActivity.MOVIES_KEY);
            movies = new ArrayList<>();
            for (int i = 0, length = array.length(); i < length; i++) {
                object = array.getJSONObject(i);
                movie = new Movie();
                movie.setPoster(MoviesActivity.BASE_POSTER_URL +
                        object.getString(MoviesActivity.MOVIE_POSTER_PATH_KEY));
                movie.setOverview(object.getString(MoviesActivity.MOVIE_OVERVIEW_KEY));
                movie.setVoteAverage(object.getDouble(MoviesActivity.MOVIE_VOTE_AVG_KEY));
                movie.setReleaseDate(object.getString(MoviesActivity.MOVIE_RELEASE_KEY));
                movie.setTitle(object.getString(MoviesActivity.MOVIE_TITLE_KEY));
                movies.add(movie);
            }


        } catch (JSONException e) {
            Log.e("MovieParser", "Error parsing Movie JSON: ", e);
        }

        return movies;
    }

}