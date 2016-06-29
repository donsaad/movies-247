package io.donsaad.movies247.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import io.donsaad.movies247.movies.Movie;
import io.donsaad.movies247.utils.Constants;

/**
 * Created by donsaad on 29/06/2016.
 * database manipulation abstraction
 */
public class DatabaseSource {

    private SQLiteDatabase mDatabase;
    private DatabaseHelper mHelper;

    public DatabaseSource(Context context) {
        mHelper = new DatabaseHelper(context);
    }

    private void open() {
        mDatabase = mHelper.getWritableDatabase();
    }

    private void close() {
        mDatabase.close();
    }

    public boolean insertMovie(Movie movie) {
        open();
        ContentValues values = new ContentValues();
        values.put(Constants.MOVIE_ID_KEY, movie.getId());
        values.put(Constants.MOVIE_TITLE_KEY, movie.getTitle());
        values.put(Constants.MOVIE_OVERVIEW_KEY, movie.getOverview());
        values.put(Constants.MOVIE_RELEASE_KEY, movie.getReleaseDate());
        values.put(Constants.MOVIE_VOTE_AVG_KEY, movie.getVoteAverage());
        values.put(Constants.MOVIE_POSTER_PATH_KEY, movie.getPoster());
        long r = mDatabase.insert(DatabaseHelper.TABLE_MOVIE, null, values);
        close();
        return (r != -1);

    }
}

