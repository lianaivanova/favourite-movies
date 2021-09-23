package com.fmi.favoritemovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView myList;
    private Button addMovie;
    private Button deleteAllMovies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        myList = findViewById(R.id.listView);
        addMovie = findViewById(R.id.addMovie);
        deleteAllMovies = findViewById(R.id.deleteAllMovies);
        List<MovieResult> myContacts = getMyMovies();
        myList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myContacts));
        Helper.getListViewSize(myList);
        myList.setOnItemClickListener(this::listener);

        addMovie.setOnClickListener((view) -> {
            Intent myIntent = new Intent(view.getContext(), AddMovieActivity.class);
            startActivityForResult(myIntent, 0);
        });

        deleteAllMovies.setOnClickListener(this::deleteAll);
    }

    private void deleteAll(View v) {
        MovieDBHelper helper = new MovieDBHelper(this);
        helper.purgeAll(helper.getWritableDatabase());

        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }

    private void listener(AdapterView<?> adapter, View view, int position, long arg) {
        MovieResult item = (MovieResult) myList.getItemAtPosition(position);

        Intent myIntent = new Intent(view.getContext(), AddMovieActivity.class);
        myIntent.putExtra("result", item);
        startActivityForResult(myIntent, 0);
    }

    private List<MovieResult> getMyMovies() {
        MovieDBHelper helper = new MovieDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(
                MovieContract.MovieEntry.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        List<MovieResult> records = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(MovieContract.MovieEntry._ID));
            String title = cursor.getString(
                    cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_TITLE)
            );

            int rating = cursor.getInt(
                    cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_RATING)
            );

            String watched = cursor.getString(
                    cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_WATCHED)
            );

            String imdbId = cursor.getString(
                    cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_NAME_IMDB_ID)
            );

            MovieResult e = new MovieResult(imdbId, null, null, title, null);
            e.setWatched("yes".equals(watched));
            e.setRating(rating);
            records.add(e);
        }
        cursor.close();
        return records;
    }
}