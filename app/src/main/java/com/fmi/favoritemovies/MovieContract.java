package com.fmi.favoritemovies;

import android.provider.BaseColumns;

public final class MovieContract {

    private MovieContract() {}

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + MovieEntry.TABLE_NAME + " (" +
                    MovieEntry._ID + " INTEGER PRIMARY KEY," +
                    MovieEntry.COLUMN_NAME_IMDB_ID + " TEXT," +
                    MovieEntry.COLUMN_NAME_RATING + " INTEGER," +
                    MovieEntry.COLUMN_NAME_TITLE + " TEXT," +
                    MovieEntry.COLUMN_NAME_WATCHED + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME;

    /* Inner class that defines the table contents */
    public static class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "FavoriteMovies";

        public static final String _ID = "id";
        public static final String COLUMN_NAME_IMDB_ID = "imdb_id";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_NAME_TITLE = "movie_title";
        public static final String COLUMN_NAME_WATCHED = "watched";
    }
}