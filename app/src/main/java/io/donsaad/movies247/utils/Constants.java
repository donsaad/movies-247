package io.donsaad.movies247.utils;

/**
 * Created by donsaad on 25/06/2016.
 * constants for use across app
 */
public class Constants {
    // MOVIE
    public static final String MOVIE_BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185";
    public static final String MOVIE_OVERVIEW_KEY = "overview";
    public static final String MOVIE_VOTE_AVG_KEY = "vote_average";
    public static final String MOVIE_RELEASE_KEY = "release_date";
    public static final String MOVIE_TITLE_KEY = "original_title";
    public static final String MOVIE_POSTER_PATH_KEY = "poster_path";
    public static final String MOVIES_KEY = "results";
    public static final String MOVIE_FAV_PREF_KEY = "adult";
    public static final String MOVIE_ADULT_KEY = "adult";
    public static final String MOVIE_ID_KEY = "id";
    public static final String MOVIES_PREF_NAME = "MOVIES_PREF";

    // REVIEW
    public static final String REVIEW_JSON_KEY = "results";
    public static final String REVIEW_AUTHOR_KEY = "author";
    public static final String REVIEW_CONTENT_KEY = "content";
    public static final String REVIEW_URL_KEY = "url";

    // TRAILER
    public static final String TRAILER_JSON_KEY = "results";
    public static final String TRAILER_ID_KEY = "id";
    public static final String TRAILER_VIDEO_KEY = "key";
    public static final String TRAILER_NAME_KEY = "name";

    // DETAILS FRAGMENT
    public static final String DATA_FETCH_URL = "http://api.themoviedb.org/3/movie/";
    public static final String TRAILER_ENDPOINT = "/videos?api_key=";
    public static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    public static final String REVIEW_ENDPOINT = "/reviews?api_key=";
}
