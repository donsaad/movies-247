package com.donsaad.movies247.movies;

import android.os.Bundle;

public class Movie {

    public static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185";
    public static final String MOVIE_OVERVIEW_KEY = "overview";
    public static final String MOVIE_VOTE_AVG_KEY = "vote_average";
    public static final String MOVIE_RELEASE_KEY = "release_date";
    public static final String MOVIE_TITLE_KEY = "original_title";
    public static final String MOVIE_POSTER_PATH_KEY = "poster_path";
    public static final String MOVIES_KEY = "results";
    public static final String MOVIE_ADULT_KEY = "adult";
    public static final String MOVIE_ID_KEY = "id";

    private String title;
    private String releaseDate;
    private String poster;
    private double voteAverage;
    private String overview;
    private boolean isAdult;
    private int id;

    public Bundle asBundle() {
        Bundle b = new Bundle();
        b.putInt(MOVIE_ID_KEY, id);
        b.putString(MOVIE_OVERVIEW_KEY, overview);
        b.putString(MOVIE_RELEASE_KEY, releaseDate);
        b.putString(MOVIE_TITLE_KEY, title);
        b.putDouble(MOVIE_VOTE_AVG_KEY, voteAverage);
        b.putString(MOVIE_POSTER_PATH_KEY, poster);
        return b;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public void setIsAdult(boolean isAdult) {
        this.isAdult = isAdult;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
