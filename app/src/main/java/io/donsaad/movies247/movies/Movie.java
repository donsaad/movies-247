package io.donsaad.movies247.movies;

import android.os.Bundle;

import io.donsaad.movies247.utils.Constants;

public class Movie {

    private String title;
    private String releaseDate;
    private String poster;
    private double voteAverage;
    private String overview;
    private boolean isAdult;
    private int id;

    public Bundle asBundle() {
        Bundle b = new Bundle();
        b.putInt(Constants.MOVIE_ID_KEY, id);
        b.putString(Constants.MOVIE_OVERVIEW_KEY, overview);
        b.putString(Constants.MOVIE_RELEASE_KEY, releaseDate);
        b.putString(Constants.MOVIE_TITLE_KEY, title);
        b.putDouble(Constants.MOVIE_VOTE_AVG_KEY, voteAverage);
        b.putString(Constants.MOVIE_POSTER_PATH_KEY, poster);
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
