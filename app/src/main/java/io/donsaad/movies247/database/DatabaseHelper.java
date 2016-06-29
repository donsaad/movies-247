package io.donsaad.movies247.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import io.donsaad.movies247.utils.Constants;

/**
 * Created by donsaad on 26/06/2016.
 * database
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "movies.db";
    private static final int DB_VERSION = 1;

    // Movies table
    public static final String TABLE_MOVIE = "movie";
    private static final String CREATE_TABLE_MOVIE = "create table " + TABLE_MOVIE + " (" +
            Constants.MOVIE_ID_KEY + " integer primary key autoincrement, " +
            Constants.MOVIE_OVERVIEW_KEY + " text not null, " +
            Constants.MOVIE_TITLE_KEY + " text not null, " +
            Constants.MOVIE_POSTER_PATH_KEY + " text not null, " +
            Constants.MOVIE_RELEASE_KEY + " date not null, " +
            Constants.MOVIE_VOTE_AVG_KEY + " double not null" + ")";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_MOVIE);
        onCreate(db);
    }
}
