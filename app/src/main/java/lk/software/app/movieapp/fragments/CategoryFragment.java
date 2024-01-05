package lk.software.app.movieapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import lk.software.app.movieapp.adapters.CastAdapter;
import lk.software.app.movieapp.adapters.MoviePosterAdapter;
import lk.software.app.movieapp.adapters.TVPosterAdapter;
import lk.software.app.movieapp.model.Movie;
import lk.software.app.movieapp.model.TVShow;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryFragment extends Fragment {
    ArrayList<Movie> popularMovies;
    ArrayList<Movie> upcomingMovies;
    ArrayList<Movie> topRatedMovies;
    ArrayList<TVShow> popularTvShows;
    ArrayList<TVShow> topRatedTvShows;
    MoviePosterAdapter posterAdapter;
    TVPosterAdapter tvPosterAdapter;
static Context context;
    public CategoryFragment() {
        // Required empty public constructor
    }

    public static CategoryFragment newInstance(Context context) {
        CategoryFragment fragment = new CategoryFragment();
        CategoryFragment.context = context;
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        popularMovies = new ArrayList<>();
        popularTvShows = new ArrayList<>();
        topRatedMovies = new ArrayList<>();
        upcomingMovies = new ArrayList<>();
        topRatedTvShows = new ArrayList<>();
        callPopularMovies(view);
        callPopularTvShows(view);
        callTopRatedMovies(view);
        callUpcomingMovies(view);
        callTopRatedTvShows(view);
    }

    private void callPopularTvShows(View view) {
        new Thread(()->{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/tv/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MovieApi movieApi = retrofit.create(MovieApi.class);
            String key = "4cdd13dbb5e2bcba4ac9ae8c5e53a297";
            Call<JsonObject> objectCall = movieApi.getPopularTvShows(key);

            objectCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body() != null) {
                        JsonObject jsonObject = response.body();
                        JsonArray results = jsonObject.getAsJsonArray("results");
                        for (int i = 0; i < results.size(); i++) {
                            JsonObject movieObject = results.get(i).getAsJsonObject();
                            JsonPrimitive title = movieObject.getAsJsonObject().getAsJsonPrimitive("name");
                            JsonPrimitive backdrop_img = movieObject.getAsJsonObject().getAsJsonPrimitive("poster_path");
                            JsonPrimitive item_id = movieObject.getAsJsonObject().getAsJsonPrimitive("id");
                            //String poster = "https://api.themoviedb.org/3/network/network_id/images"+backdrop_img;
                            Log.i("getAsString", title.toString());
                            TVShow tvShow = new TVShow();
                            tvShow.setId(Integer.parseInt(item_id.toString()));
                            tvShow.setTitle(title.toString());
                            String baseUrl = "https://image.tmdb.org/t/p/w500";
                            String concat = baseUrl.concat(backdrop_img.getAsString());
                            tvShow.setPoster_uri(concat);

                            popularTvShows.add(tvShow);
                        }

                        Log.i("tv shows", String.valueOf(popularTvShows.size()));
                        RecyclerView recyclerView = view.findViewById(R.id.populatTvRecycler);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                        tvPosterAdapter = new TVPosterAdapter(context, popularTvShows);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(tvPosterAdapter);
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

    private void callTopRatedMovies(View view) {
        new Thread(()->{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/movie/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MovieApi movieApi = retrofit.create(MovieApi.class);
            String key = "4cdd13dbb5e2bcba4ac9ae8c5e53a297";
            Call<JsonObject> objectCall = movieApi.getTopRatedMovies(key);

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

                            topRatedMovies.add(movie);
                        }

                        Log.i("movies", String.valueOf(topRatedMovies.size()));
                        RecyclerView recyclerView = view.findViewById(R.id.topRatedRecycler);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                        posterAdapter = new MoviePosterAdapter(context, topRatedMovies);
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
    private void callTopRatedTvShows(View view) {
        new Thread(()->{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/tv/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MovieApi movieApi = retrofit.create(MovieApi.class);
            String key = "4cdd13dbb5e2bcba4ac9ae8c5e53a297";
            Call<JsonObject> objectCall = movieApi.getTopRatedTvShows(key);

            objectCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body() != null) {
                        JsonObject jsonObject = response.body();
                        JsonArray results = jsonObject.getAsJsonArray("results");
                        for (int i = 0; i < results.size(); i++) {
                            JsonObject movieObject = results.get(i).getAsJsonObject();
                            JsonPrimitive title = movieObject.getAsJsonObject().getAsJsonPrimitive("name");
                            JsonPrimitive item_id = movieObject.getAsJsonObject().getAsJsonPrimitive("id");

                            JsonPrimitive backdrop_img = movieObject.getAsJsonObject().getAsJsonPrimitive("poster_path");
                            //String poster = "https://api.themoviedb.org/3/network/network_id/images"+backdrop_img;
                            Log.i("getAsString", title.toString());
                            TVShow tvShow = new TVShow();
                            tvShow.setId(Integer.parseInt(item_id.toString()));

                            tvShow.setTitle(title.toString());
                            String baseUrl = "https://image.tmdb.org/t/p/w500";
                            String concat = baseUrl.concat(backdrop_img.getAsString());
                            tvShow.setPoster_uri(concat);

                            topRatedTvShows.add(tvShow);
                        }

                        Log.i("movies", String.valueOf(topRatedTvShows.size()));
                        RecyclerView recyclerView = view.findViewById(R.id.topRatedTvRecycler);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                        tvPosterAdapter = new TVPosterAdapter(context, topRatedTvShows);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(tvPosterAdapter);
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
    private void callUpcomingMovies(View view) {
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
                            JsonPrimitive item_id = movieObject.getAsJsonObject().getAsJsonPrimitive("id");

                            JsonPrimitive backdrop_img = movieObject.getAsJsonObject().getAsJsonPrimitive("poster_path");


                            Log.i("getAsString", title.toString());
                            Movie movie = new Movie();
                            movie.setId(Integer.parseInt(item_id.toString()));

                            movie.setTitle(title.getAsString());

                            String baseUrl = "https://image.tmdb.org/t/p/w500";
                            String concat = baseUrl.concat(backdrop_img.getAsString());
                            movie.setPoster_uri(concat);

                            upcomingMovies.add(movie);
                        }

                        Log.i("movies", String.valueOf(upcomingMovies.size()));
                        RecyclerView recyclerView = view.findViewById(R.id.upcomingRecycler);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                        posterAdapter = new MoviePosterAdapter(context, upcomingMovies);
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

    private void callPopularMovies(View view) {
        new Thread(()->{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/movie/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MovieApi movieApi = retrofit.create(MovieApi.class);
            String key = "4cdd13dbb5e2bcba4ac9ae8c5e53a297";
            Call<JsonObject> objectCall = movieApi.getJson(key);

            objectCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body() != null) {
                        JsonObject jsonObject = response.body();
                        JsonArray results = jsonObject.getAsJsonArray("results");
                        for (int i = 0; i < results.size(); i++) {
                            JsonObject movieObject = results.get(i).getAsJsonObject();
                            JsonPrimitive title = movieObject.getAsJsonObject().getAsJsonPrimitive("title");
                            JsonPrimitive item_id = movieObject.getAsJsonObject().getAsJsonPrimitive("id");

                            JsonPrimitive backdrop_img = movieObject.getAsJsonObject().getAsJsonPrimitive("poster_path");
                            //String poster = "https://api.themoviedb.org/3/network/network_id/images"+backdrop_img;
                            Log.i("getAsString", title.toString());
                            Movie movie = new Movie();
                            movie.setTitle(title.toString());
                            movie.setId(Integer.parseInt(item_id.toString()));

                            String baseUrl = "https://image.tmdb.org/t/p/w500";
                            String concat = baseUrl.concat(backdrop_img.getAsString());
                            movie.setPoster_uri(concat);

                            popularMovies.add(movie);
                        }

                        Log.i("movies", String.valueOf(popularMovies.size()));
                        RecyclerView recyclerView = view.findViewById(R.id.popularMoviesRecycler);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                        posterAdapter = new MoviePosterAdapter(context, popularMovies);
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
        return inflater.inflate(R.layout.fragment_category, container, false);
    }
}