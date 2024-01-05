package lk.software.app.movieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;

import lk.software.app.movieapp.adapters.CastAdapter;
import lk.software.app.movieapp.adapters.SeasonAdapter;
import lk.software.app.movieapp.model.Actor;
import lk.software.app.movieapp.model.Item;
import lk.software.app.movieapp.model.Movie;
import lk.software.app.movieapp.model.Season;
import lk.software.app.movieapp.model.TVShow;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsActivity extends AppCompatActivity {
    Item movie;

    int id;
    ArrayList<Actor> actors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        actors = new ArrayList<>();

        Intent intent = getIntent();
        Serializable serializableExtra = intent.getSerializableExtra("movie");
        if (serializableExtra instanceof Movie) {
            movie = (Movie) serializableExtra;
        }
        if (serializableExtra instanceof TVShow) {
            movie = (TVShow) serializableExtra;
        }

        id = movie.getId();
        TextView textView = findViewById(R.id.Title);
        textView.setText(movie.getTitle());
        ImageView poster = findViewById(R.id.poster);
        Picasso.get().load(movie.getPoster_uri()).into(poster);
        ImageView backdrop = findViewById(R.id.backdrop);
        Picasso.get().load(movie.getBackdrop_url()).into(backdrop);
        TextView overview = findViewById(R.id.overview);
        overview.setText(movie.getOverview());
        LinearLayout layout = findViewById(R.id.genreLayout);
        TextView rating = findViewById(R.id.rating);
        rating.setText(movie.getRating());
        TextView votes = findViewById(R.id.votes);
        votes.setText(movie.getVotes());
        TextView runtime = findViewById(R.id.duration);
        runtime.setText(movie.getDuration() + " min");
        TextView release = findViewById(R.id.release);
        release.setText(movie.getRelease());
        TextView country = findViewById(R.id.country);
        RecyclerView recyclerView = findViewById(R.id.seasonRecycler);
        if (movie instanceof TVShow) {
            TextView textView1 = findViewById(R.id.textView18);
            textView1.setText("Episodes");
            Log.i("details episodes", movie.getNumber_of_episodes());
//    TextView noe = findViewById(R.id.country);
            country.setText(movie.getNumber_of_episodes());
            TextView seasonHeader = findViewById(R.id.textView21);
            seasonHeader.setVisibility(View.VISIBLE);

            recyclerView.setVisibility(View.VISIBLE);
            ArrayList<Season> seasons = movie.getSeasons();
            SeasonAdapter seasonAdapter = new SeasonAdapter(DetailsActivity.this, seasons,String.valueOf(movie.getId()));

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailsActivity.this);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(seasonAdapter);
            getTvCast();
        }

        if (movie instanceof Movie) {
            country.setText(movie.getCountry());
            new Thread(() -> {
                getCast();
            }).start();
        }

        TextView lang = findViewById(R.id.lang);
        lang.setText(movie.getLang());


        for (String name : movie.getGenres()) {

            View inflate = getLayoutInflater().inflate(R.layout.genre_item, null);
            TextView textView1 = inflate.findViewById(R.id.textView6);
            textView1.setText(name);
            layout.addView(inflate);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(10, ViewGroup.LayoutParams.MATCH_PARENT);
            Space space = new Space(this);
            space.setLayoutParams(layoutParams);
            layout.addView(space);

        }

        findViewById(R.id.imageView5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void getTvCast() {
        String baseUrl = "https://api.themoviedb.org/3/tv/" + id + "/";
        Log.d("base url", baseUrl);
        new Thread(()->{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MovieApi movieApi = retrofit.create(MovieApi.class);
            String key = "4cdd13dbb5e2bcba4ac9ae8c5e53a297";
            Call<JsonObject> objectCall = movieApi.getCredits(key);

            objectCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body() != null) {
                        JsonObject jsonObject = response.body();
                        JsonArray cast = jsonObject.getAsJsonArray("cast");
                        for (int i = 0; i < cast.size(); i++) {
                            JsonObject castObject = cast.get(i).getAsJsonObject();
                            int id = castObject.getAsJsonObject().getAsJsonPrimitive("id").getAsInt();

                            String name = castObject.getAsJsonObject().getAsJsonPrimitive("name").getAsString();
                            String character = castObject.getAsJsonObject().getAsJsonPrimitive("character").getAsString();
                            String profile = null;
                            String baseUrl = "https://image.tmdb.org/t/p/w500";
                            if (!(castObject.getAsJsonObject().get("profile_path") instanceof JsonNull)) {
                                profile = baseUrl.concat(castObject.getAsJsonObject().getAsJsonPrimitive("profile_path").getAsString());
                            }


                            Actor actor = new Actor();

                            actor.setId(id);
                            actor.setName(name);
                            actor.setCharacter(character);
                            actor.setProfile_path(profile);
                            Log.i("cast", String.valueOf(id));
                            actors.add(actor);
                        }

                        CastAdapter castAdapter = new CastAdapter(DetailsActivity.this, actors);
                        RecyclerView recyclerView = findViewById(R.id.caseRecycler);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailsActivity.this);
                        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(castAdapter);
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
    private void getCast() {
        String baseUrl = "https://api.themoviedb.org/3/movie/" + id + "/";
        Log.d("base url", baseUrl);
        new Thread(()->{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MovieApi movieApi = retrofit.create(MovieApi.class);
            String key = "4cdd13dbb5e2bcba4ac9ae8c5e53a297";
            Call<JsonObject> objectCall = movieApi.getCredits(key);

            objectCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body() != null) {
                        JsonObject jsonObject = response.body();
                        JsonArray cast = jsonObject.getAsJsonArray("cast");
                        for (int i = 0; i < cast.size(); i++) {
                            JsonObject castObject = cast.get(i).getAsJsonObject();
                            int id = castObject.getAsJsonObject().getAsJsonPrimitive("id").getAsInt();

                            String name = castObject.getAsJsonObject().getAsJsonPrimitive("name").getAsString();
                            String character = castObject.getAsJsonObject().getAsJsonPrimitive("character").getAsString();
                            String profile = null;
                            String baseUrl = "https://image.tmdb.org/t/p/w500";
                            if (!(castObject.getAsJsonObject().get("profile_path") instanceof JsonNull)) {
                                profile = baseUrl.concat(castObject.getAsJsonObject().getAsJsonPrimitive("profile_path").getAsString());
                            }


                            Actor actor = new Actor();
                            actor.setId(id);
                            actor.setName(name);
                            actor.setCharacter(character);
                            actor.setProfile_path(profile);
                            Log.i("cast", name);
                            actors.add(actor);
                        }

                        CastAdapter castAdapter = new CastAdapter(DetailsActivity.this, actors);
                        RecyclerView recyclerView = findViewById(R.id.caseRecycler);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailsActivity.this);
                        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(castAdapter);
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