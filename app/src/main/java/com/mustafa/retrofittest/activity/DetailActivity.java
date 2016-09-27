package com.mustafa.retrofittest.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mustafa.retrofittest.R;
import com.mustafa.retrofittest.model.Genre;
import com.mustafa.retrofittest.model.Movie;
import com.mustafa.retrofittest.rest.ApiClient;
import com.mustafa.retrofittest.rest.ApiInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    TextView txtTitle,txtOverView,txtGenre;
    ImageView movieView;

    private static final String TAG = DetailActivity.class.getSimpleName();

    private final static String API_KEY = "53479e4e234d5f3d8e75129fbe63a849";
    private String imgUrl = "http://image.tmdb.org/t/p/w150";

    Integer id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
        }
        else {
            finish();
        }

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtOverView = (TextView) findViewById(R.id.txtOverview);
        txtGenre = (TextView) findViewById(R.id.txtGenre);
        movieView = (ImageView) findViewById(R.id.movieVie);


        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<Movie> callDetail = apiService.getMovieDetails(id,API_KEY);
        callDetail.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                Movie movie = response.body();
                txtTitle.setText(movie.getTitle());
                txtOverView.setText(movie.getOverview());
                List<Genre> genre = movie.getGenres();
                List<String> genreName = new ArrayList<String>();
                for (int i=0;i<genre.size();i++){
                    genreName.add(genre.get(i).getName());
                }
                txtGenre.setText("Genre: "+genreName.toString().replaceAll("\\[|\\]", ""));
                Log.d(TAG,"movie url = "+imgUrl+movie.getPosterPath());
                Glide.with(getApplicationContext()).load(imgUrl+movie.getPosterPath()).crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(movieView);
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e(TAG,toString());
            }
        });


    }
}
