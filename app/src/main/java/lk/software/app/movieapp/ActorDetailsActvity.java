package lk.software.app.movieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import lk.software.app.movieapp.adapters.CastAdapter;
import lk.software.app.movieapp.adapters.MoviePosterAdapter;
import lk.software.app.movieapp.model.Actor;
import lk.software.app.movieapp.model.Movie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActorDetailsActvity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor_details_actvity);

        int id = getIntent().getExtras().getInt("id");
callActorDetails(id);
callActorCredits(id);
        System.out.println(id);

        findViewById(R.id.imageView13).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void callActorCredits(int id) {
        ArrayList<Movie> movieCredits = new ArrayList<>();
        String baseUrl = "https://api.themoviedb.org/3/person/";
        Log.d("base url", baseUrl);
        new Thread(()->{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MovieApi movieApi = retrofit.create(MovieApi.class);
            String key = "4cdd13dbb5e2bcba4ac9ae8c5e53a297";
            Call<JsonObject> objectCall = movieApi.getActorCredits(id,key);

            objectCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body() != null) {
                        JsonObject jsonObject = response.body();
                        JsonArray results = jsonObject.getAsJsonArray("cast");
                        for (int i = 0; i < results.size(); i++) {
                            JsonObject movieObject = results.get(i).getAsJsonObject();
                            JsonPrimitive title = movieObject.getAsJsonObject().getAsJsonPrimitive("title");
                            String profile = null;
                            String baseUrl = "https://image.tmdb.org/t/p/w500";
                            if (!(movieObject.getAsJsonObject().get("poster_path") instanceof JsonNull)) {
                                profile = baseUrl.concat(movieObject.getAsJsonObject().getAsJsonPrimitive("poster_path").getAsString());
                            }else{
                                profile = baseUrl;
                            }

                            JsonPrimitive item_id = movieObject.getAsJsonObject().getAsJsonPrimitive("id");

                            //String poster = "https://api.themoviedb.org/3/network/network_id/images"+backdrop_img;
                            Log.i("getAsString", title.toString());
                            Movie movie = new Movie();
                            movie.setId(Integer.parseInt(item_id.toString()));

                            movie.setTitle(title.toString());


                            movie.setPoster_uri(profile);

                            movieCredits.add(movie);
                        }

                        Log.i("movies", String.valueOf(movieCredits.size()));
                        RecyclerView recyclerView = findViewById(R.id.movieCreditRecycler);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ActorDetailsActvity.this);
                        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                        MoviePosterAdapter moviePosterAdapter = new MoviePosterAdapter(ActorDetailsActvity.this, movieCredits);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(moviePosterAdapter);


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

    private void callActorDetails(int id) {
        String baseUrl = "https://api.themoviedb.org/3/person/";
        Log.d("base url", baseUrl);
        new Thread(()->{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MovieApi movieApi = retrofit.create(MovieApi.class);
            String key = "4cdd13dbb5e2bcba4ac9ae8c5e53a297";
            Call<JsonObject> objectCall = movieApi.getActorDetails(id,key);

            objectCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body() != null) {
                        JsonObject jsonObject = response.body();


                            String name = jsonObject.getAsJsonPrimitive("name").getAsString();
                        String bio;
                        if (!(jsonObject.getAsJsonObject().get("biography") instanceof JsonNull)) {
                            bio = jsonObject.getAsJsonPrimitive("biography").getAsString();
                        } else {
                            bio = "";
                        }

                            String profile = null;
                            String baseUrl = "https://image.tmdb.org/t/p/w500";
                            if (!(jsonObject.getAsJsonObject().get("profile_path") instanceof JsonNull)) {
                                profile = baseUrl.concat(jsonObject.getAsJsonObject().getAsJsonPrimitive("profile_path").getAsString());
                            }else{
                                profile = baseUrl;
                            }

                            ImageView imageView = findViewById(R.id.imageView12);
                        Picasso.get().load(profile).into(imageView);

                        TextView full_name = findViewById(R.id.textView26);
                        TextView overview = findViewById(R.id.textView27);

                        full_name.setText(name);
                        overview.setText(bio);

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
}