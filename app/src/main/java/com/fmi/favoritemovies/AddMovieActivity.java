package com.fmi.favoritemovies;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class AddMovieActivity extends Activity  {
    private ObjectMapper objectMapper = new ObjectMapper();
    private EditText searchBar;
    private EditText title;
    private ImageView image;
    private EditText desc;
    private EditText year;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch watched;
    private EditText rating;
    private Button save;
    private Button delete;
    private Button search;
    private boolean isPopulated = false;
    private MovieResult result;
    private MovieDetails details;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_movie);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        searchBar = findViewById(R.id.searchBar);
        title = findViewById(R.id.movieTitle);
        image = findViewById(R.id.movieView);
        desc = findViewById(R.id.movieDesc);
        year = findViewById(R.id.movieYear);
        watched = findViewById(R.id.watched);
        rating = findViewById(R.id.rating);
        save = findViewById(R.id.saveMovie);
        delete = findViewById(R.id.deleteMovie);
        search = findViewById(R.id.searchMovie);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             Serializable result = extras.getSerializable("result");
            if (result != null) {
                isPopulated = true;
                this.result = (MovieResult) result;
                LinearLayout layout = findViewById(R.id.searchLayout);
                layout.setVisibility(View.GONE);
                populateFields(this.result.getId());
            }
        }

        search.setOnClickListener(this::searchMovie);
        save.setOnClickListener(this::saveMovie);
        delete.setOnClickListener(this::deleteMovie);


    }

    private void deleteMovie(View v) {
        Intent myIntent = new Intent(v.getContext(), MainActivity.class);
        if (!isPopulated) {
            startActivityForResult(myIntent, 0);
            return;
        }
        MovieDBHelper helper = new MovieDBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        String selection = MovieContract.MovieEntry.COLUMN_NAME_IMDB_ID + " LIKE ?";
        String[] selectionArgs = { result.getId() };

        db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
        startActivityForResult(myIntent, 0);
    }

    private void saveMovie(View v) {
        MovieDBHelper helper = new MovieDBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_NAME_IMDB_ID, result.getId());
        String r = rating.getText().toString();
        if (!r.isEmpty()) {
            values.put(MovieContract.MovieEntry.COLUMN_NAME_RATING, Integer.parseInt(r));
        }
        values.put(MovieContract.MovieEntry.COLUMN_NAME_TITLE, result.getTitle());
        values.put(MovieContract.MovieEntry.COLUMN_NAME_WATCHED, watched.isChecked() ? "yes" : "no");

        if (isPopulated) {
            String selection = MovieContract.MovieEntry.COLUMN_NAME_IMDB_ID + " LIKE ?";
            String[] selectionArgs = { result.getId() };
            db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
        } else {
            db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
        }
        Intent myIntent = new Intent(v.getContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
    }

    private void searchMovie(View v) {
        String movieName = searchBar.getText().toString();
        if (movieName.isEmpty()) {
            Toast.makeText(this, "Моля въведете филм", Toast.LENGTH_SHORT).show();
            return;
        }
        URL endpoint = null;
        try {
            String spec = "https://imdb-api.com/en/API/SearchTitle/k_7e3c0f26/"+movieName;
            endpoint = new URL(spec);
            URL finalEndpoint = endpoint;
            Thread thread = new Thread(() -> {
                try {
                    HttpsURLConnection myConnection = (HttpsURLConnection) finalEndpoint.openConnection();
                    myConnection.setHostnameVerifier((hostname, session) -> true);
                    if (myConnection.getResponseCode() != 200) {
                        runOnUiThread(() -> Toast.makeText(this, "Няма такъв филм", Toast.LENGTH_SHORT).show());
                        return;
                    }

                    InputStream responseBody = myConnection.getInputStream();

                    IMDBResponse resp = objectMapper.readValue(responseBody, IMDBResponse.class);
                    if (!resp.getErrorMessage().isEmpty() || resp.getResults() == null || resp.getResults().size() == 0) {
                        runOnUiThread(() -> Toast.makeText(this, "Няма такъв филм", Toast.LENGTH_SHORT).show());
                        return;
                    }

                    MovieResult movieResult = resp.getResults().get(0);
                    this.result = movieResult;
                    populateFields(movieResult.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            ;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void populateFields(String id) {
        URL endpoint = null;
        try {
            String spec = "https://imdb-api.com/en/API/Title/k_7e3c0f26/"+id;
            endpoint = new URL(spec);
            URL finalEndpoint = endpoint;
            Thread thread = new Thread(() -> {
                try {
                    HttpsURLConnection myConnection = (HttpsURLConnection) finalEndpoint.openConnection();
                    myConnection.setHostnameVerifier((hostname, session) -> true);
                    if (myConnection.getResponseCode() != 200) {
                        runOnUiThread(() -> Toast.makeText(this, "Няма такъв филм", Toast.LENGTH_SHORT).show());
                        return;
                    }

                    InputStream responseBody = myConnection.getInputStream();

                    MovieDetails resp = objectMapper.readValue(responseBody, MovieDetails.class);
                    if (resp.getId() == null) {
                        runOnUiThread(() -> Toast.makeText(this, "Няма такъв филм", Toast.LENGTH_SHORT).show());
                        return;
                    }
                    
                    runOnUiThread(() -> {
                        Thread thread2 = new Thread(() -> {
                            URL imageUrl = null;
                            try {
                                imageUrl = new URL(resp.getImage());
                                URL finalImageUrl = imageUrl;
                                runOnUiThread(() -> {
                                    try {
                                        image.setImageBitmap(BitmapFactory.decodeStream(finalImageUrl.openConnection().getInputStream()));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        thread2.start();
                        title.setText(resp.getTitle());
                        desc.setText(resp.getPlot());
                        year.setText(resp.getYear());
                        if (result != null) {
                            rating.setText(result.getRating() > 0 ? String.valueOf(result.getRating()) : null);
                            watched.setChecked(result.isWatched());
                        }

                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            ;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
