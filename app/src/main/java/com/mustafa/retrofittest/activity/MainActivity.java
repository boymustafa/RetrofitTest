package com.mustafa.retrofittest.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.mustafa.retrofittest.R;
import com.mustafa.retrofittest.adapter.MoviesAdapter;
import com.mustafa.retrofittest.model.Genre;
import com.mustafa.retrofittest.model.Movie;
import com.mustafa.retrofittest.model.MovieResponse;
import com.mustafa.retrofittest.rest.ApiClient;
import com.mustafa.retrofittest.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private final static String API_KEY = "53479e4e234d5f3d8e75129fbe63a849";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (API_KEY.isEmpty()){

            Toast.makeText(getApplicationContext(), "please obtain API_KEY first", Toast.LENGTH_SHORT).show();
            return;
        }

        final RecyclerView recyclerView  = (RecyclerView) findViewById(R.id.movies_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MovieResponse> call = apiService.getTopRatedMovies(API_KEY);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(final Call<MovieResponse> call, Response<MovieResponse> response) {
//                List<Movie> movies = response.body().getResults();
//                Log.d(TAG,"Number of movies received: "+movies.size());
                int statusCode = response.code();
                List<Movie> movies = response.body().getResults();
                recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.list_item_movie, getApplicationContext(), new MoviesAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClick(Movie movie) {
                        Toast.makeText(getApplicationContext(), "movie id = "+movie.getId(), Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), DetailActivity.class);

                        i.putExtra("id",movie.getId());
                        startActivity(i);

//                        Call<Movie> callDetail = apiService.getMovieDetails(movie.getId(),API_KEY);
//                        callDetail.enqueue(new Callback<Movie>() {
//                            @Override
//                            public void onResponse(Call<Movie> call, Response<Movie> response) {
//                                Movie movie = response.body();
//                                Log.d(TAG,"Movie OverView = "+movie.getOverview());
//                                Log.d(TAG,"Poster path = "+movie.getPosterPath());
//                                Log.d(TAG,"Genre array = "+movie.getGenres());
////                                Genre genre = (Genre) movie.getGenres();
//                                List<Genre> genre = movie.getGenres();
//                                Log.d(TAG,"Genre 1 = "+genre.get(0).getName());
//                                Log.d(TAG,"Genre 2 = "+genre.get(1).getName());
////                                Log.d(TAG,"Genre = "+movie.getGenreIds());
//                            }
//
//                            @Override
//                            public void onFailure(Call<Movie> call, Throwable t) {
//                                Log.e(TAG,t.toString());
//                            }
//                        });

                    }
                }));

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(TAG,t.toString());
            }
        });


    }
}
