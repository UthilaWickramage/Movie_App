package lk.software.app.movieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import lk.software.app.movieapp.fragments.CategoryFragment;
import lk.software.app.movieapp.fragments.SearchFragment;
import lk.software.app.movieapp.fragments.SliderFragment;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        movies = new ArrayList<>();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://api.themoviedb.org/3/movie/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        MovieApi movieApi = retrofit.create(MovieApi.class);
//        String key = "4cdd13dbb5e2bcba4ac9ae8c5e53a297";
//        Call<JsonObject> objectCall = movieApi.getJson(key);
//
//        objectCall.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                if (response.body() != null) {
//                    JsonObject jsonObject = response.body();
//                    JsonArray results = jsonObject.getAsJsonArray("results");
//                    for (int i = 0; i < results.size(); i++) {
//                        JsonObject movieObject = results.get(i).getAsJsonObject();
//                        JsonPrimitive title = movieObject.getAsJsonObject().getAsJsonPrimitive("title");
//                        JsonPrimitive backdrop_img = movieObject.getAsJsonObject().getAsJsonPrimitive("poster_path");
//                        //String poster = "https://api.themoviedb.org/3/network/network_id/images"+backdrop_img;
//                        Log.i("getAsString", title.toString());
//                        Movie movie = new Movie();
//                        movie.setTitle(title.toString());
//                        String baseUrl = "https://image.tmdb.org/t/p/w500";
//                        String concat = baseUrl.concat(backdrop_img.getAsString());
//                        movie.setPoster_uri(concat);
//
//                        movies.add(movie);
//                    }
//
//                    Log.i("movies", String.valueOf(movies.size()));
//                    RecyclerView recyclerView = findViewById(R.id.movieRecycler);
//                    GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,2);
//
//                    posterAdapter = new PosterAdapter(MainActivity.this,movies);
//                    recyclerView.setLayoutManager(gridLayoutManager);
//                    recyclerView.setAdapter(posterAdapter);
//                } else {
//                    Log.i("response", "null");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                Log.e("response", t.getMessage());
//            }
//        });

        loadFragment(R.id.searchContainer, SearchFragment.newInstance());
        loadFragment(R.id.fragmentContainer, SliderFragment.newInstance());
loadFragment(R.id.categoryView, CategoryFragment.newInstance());


    }
    private void loadFragment(int fragmentContainerView, Fragment
            fragment) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();

        fragmentTransaction.replace(fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }
    private void callApi() {

    }
}