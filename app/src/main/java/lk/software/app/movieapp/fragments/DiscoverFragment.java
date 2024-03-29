package lk.software.app.movieapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverFragment extends Fragment {
    ArrayList<Movie> movies;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
String search;
MoviePosterAdapter posterAdapter;
    public DiscoverFragment() {
        // Required empty public constructor
    }
    public DiscoverFragment(String search) {
        // Required empty public constructor
        this.search = search;
    }

    // TODO: Rename and change types and number of parameters
    public static DiscoverFragment newInstance(String param1, String param2) {
        DiscoverFragment fragment = new DiscoverFragment();

        return fragment;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        movies  = new ArrayList<>();
        callSearchMovies(view);
    }

    private void callSearchMovies(View view) {
        new Thread(()->{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/search/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MovieApi movieApi = retrofit.create(MovieApi.class);
            String key = "4cdd13dbb5e2bcba4ac9ae8c5e53a297";
            System.out.println(search);
            Call<JsonObject> objectCall = movieApi.getSearchMovies(key,search);

            objectCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body() != null) {
                        JsonObject jsonObject = response.body();
                        JsonArray results = jsonObject.getAsJsonArray("results");
                        for (int i = 0; i < results.size(); i++) {
                            JsonObject movieObject = results.get(i).getAsJsonObject();
                            System.out.println(movieObject.toString());
                            JsonPrimitive title = movieObject.getAsJsonObject().getAsJsonPrimitive("title");
                            String backdrop_img ;
                            String baseUrl = "https://image.tmdb.org/t/p/w500";
                            if (!(movieObject.getAsJsonObject().get("poster_path") instanceof JsonNull)) {
                                backdrop_img = movieObject.getAsJsonObject().getAsJsonPrimitive("poster_path").getAsString();
                            } else {
                                backdrop_img = "N/A";
                            }
                            JsonPrimitive item_id = movieObject.getAsJsonObject().getAsJsonPrimitive("id");

                            //String poster = "https://api.themoviedb.org/3/network/network_id/images"+backdrop_img;
                            Log.i("getAsString", title.toString());
                            Movie movie = new Movie();
                            movie.setId(Integer.parseInt(item_id.toString()));

                            movie.setTitle(title.toString());

                            String concat = baseUrl.concat(backdrop_img);
                            movie.setPoster_uri(concat);

                            movies.add(movie);
                        }

                        Log.i("movies", String.valueOf(movies.size()));
                        RecyclerView recyclerView = view.findViewById(R.id.movieRecycler);
                        GridLayoutManager linearLayoutManager = new GridLayoutManager(mcontext,2);
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        posterAdapter = new MoviePosterAdapter(mcontext, movies);

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
Context mcontext;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mcontext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }
}