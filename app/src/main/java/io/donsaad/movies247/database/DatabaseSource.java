package io.donsaad.movies247.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

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
        if(findMovie(movie.getId())) {
            close();
            return false;
        }
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

    public List<Movie> getFavorites() {
        open();
        Cursor cursor = mDatabase.query(
                DatabaseHelper.TABLE_MOVIE, //table
                null, // null for all columns
                null, // where clause
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            List<Movie> list = new ArrayList<>();
            Movie movie;
            while (!cursor.isAfterLast()) {
                movie = new Movie();
                movie.setId(cursor.getInt(cursor.getColumnIndex(Constants.MOVIE_ID_KEY)));
                movie.setPoster(cursor.getString(cursor.getColumnIndex(Constants.MOVIE_POSTER_PATH_KEY)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(Constants.MOVIE_OVERVIEW_KEY)));
                movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(Constants.MOVIE_VOTE_AVG_KEY)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(Constants.MOVIE_RELEASE_KEY)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(Constants.MOVIE_TITLE_KEY)));

                list.add(movie);
                cursor.moveToNext();
            }
            cursor.close();
            close();
            return list;
        } else {
            close();
            return null;
        }
    }

    private boolean findMovie(int id) {
        Cursor cursor = mDatabase.query(
                DatabaseHelper.TABLE_MOVIE, //table
                null, // columns
                Constants.MOVIE_ID_KEY + " = " + id, // where clause
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        else {
            return false;
        }
    }
}

