package lk.software.app.movieapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;

import lk.software.app.movieapp.MovieApi;
import lk.software.app.movieapp.R;
import lk.software.app.movieapp.adapters.MoviePosterAdapter;
import lk.software.app.movieapp.model.Movie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieFragment extends Fragment {
    MoviePosterAdapter posterAdapter;
    ArrayList<Movie> movies;
    String search;
    public static Context context;
    public MovieFragment() {
        // Required empty public constructor
    }
//    public MovieFragment(String search) {
//        this.search = search;
//        // Required empty public constructor
//    }
    public static MovieFragment newInstance(Context context) {
        MovieFragment fragment = new MovieFragment();
MovieFragment.context = context;
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        movies = new ArrayList<>();
        callPopularMovies(view);
    }



    private void callPopularMovies(View view) {
        new Thread(()->{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/movie/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MovieApi movieApi = retrofit.create(MovieApi.class);
            String key = "4cdd13dbb5e2bcba4ac9ae8c5e53a297";

            Call<JsonObject> objectCall = movieApi.getUpcomingMovies(key);

            objectCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body() != null) {
                        JsonObject jsonObject = response.body();
                        JsonArray results = jsonObject.getAsJsonArray("results");
                        for (int i = 0; i < results.size(); i++) {
                            JsonObject movieObject = results.get(i).getAsJsonObject();
                            JsonPrimitive title = movieObject.getAsJsonObject().getAsJsonPrimitive("title");
                            JsonPrimitive backdrop_img = movieObject.getAsJsonObject().getAsJsonPrimitive("poster_path");
                            JsonPrimitive item_id = movieObject.getAsJsonObject().getAsJsonPrimitive("id");

                            //String poster = "https://api.themoviedb.org/3/network/network_id/images"+backdrop_img;
                            Log.i("getAsString", title.toString());
                            Movie movie = new Movie();
                            movie.setId(Integer.parseInt(item_id.toString()));

                            movie.setTitle(title.toString());
                            String baseUrl = "https://image.tmdb.org/t/p/w500";
                            String concat = baseUrl.concat(backdrop_img.getAsString());
                            movie.setPoster_uri(concat);

                            movies.add(movie);
                        }


                        Log.i("movies", String.valueOf(movies.size()));
                        RecyclerView recyclerView = view.findViewById(R.id.movie_recycler);
                        GridLayoutManager linearLayoutManager = new GridLayoutManager(context,2);
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        posterAdapter = new MoviePosterAdapter(context, movies);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(posterAdapter);
                    } else {
                        Log.i("response", "null");
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("response", t.getMessage());
                }
            });
        }).start();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }
}