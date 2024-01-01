package lk.software.app.movieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

import lk.software.app.movieapp.adapters.EpisodeAdapter;
import lk.software.app.movieapp.model.Episode;
import lk.software.app.movieapp.model.Season;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SeasonDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season_details);

        String series_id = getIntent().getStringExtra("series_id");
        Season season = (Season) getIntent().getSerializableExtra("season");
        System.out.println(season);
        System.out.println(series_id);
        System.out.println(season.getId());
        CallEpisodes(series_id,season);

        ImageView imageView = findViewById(R.id.imageView8);
        Picasso.get().load(season.getPoster()).into(imageView);
        TextView season_num = findViewById(R.id.textView15);
        TextView episodes = findViewById(R.id.textView20);
        season_num.setText(season.getSeason_number());
        episodes.setText(season.getNumber_of_episodes());


        findViewById(R.id.imageView9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void CallEpisodes(String series_id, Season season) {
        new Thread(() -> {
            String baseUrl = "https://api.themoviedb.org/3/tv/";
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MovieApi movieApi = retrofit.create(MovieApi.class);
            String key = "4cdd13dbb5e2bcba4ac9ae8c5e53a297";
            Call<JsonObject> objectCall = movieApi.getSeasonDetails(series_id, season.getSeason_number(), key);

            objectCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body() != null) {
                        JsonObject jsonObject = response.body();
                        JsonArray episodes = jsonObject.getAsJsonArray("episodes");
                        ArrayList<Episode> episodesList = new ArrayList<>();
                        for (int z = 0; z < episodes.size(); z++) {
                            String name = episodes.get(z).getAsJsonObject().getAsJsonPrimitive("name").getAsString();

                            String overview = null;

                            if (!(episodes.get(z).getAsJsonObject().get("overview") instanceof JsonNull)) {
                                overview = episodes.get(z).getAsJsonObject().getAsJsonPrimitive("overview").getAsString();
                            } else {
                                overview = "N/A";
                            }

                            String runtime = null;
                            if (!(episodes.get(z).getAsJsonObject().get("runtime") instanceof JsonNull)) {
                                runtime = episodes.get(z).getAsJsonObject().getAsJsonPrimitive("runtime").getAsString();
                            } else {
                                runtime = "N/A";
                            }
                            String still_path = null;
                            String baseUrl = "https://image.tmdb.org/t/p/w500";
                            if (!(episodes.get(z).getAsJsonObject().get("still_path") instanceof JsonNull)) {
                                still_path = baseUrl.concat(episodes.get(z).getAsJsonObject().getAsJsonPrimitive("still_path").getAsString());
                            } else {
                                still_path = baseUrl;
                            }

                            String vote_average = null;
                            if (!(episodes.get(z).getAsJsonObject().get("vote_average") instanceof JsonNull)) {
                                vote_average = episodes.get(z).getAsJsonObject().getAsJsonPrimitive("vote_average").getAsString();
                            } else {
                                vote_average = "N/A";
                            }

                            String episode_number = episodes.get(z).getAsJsonObject().getAsJsonPrimitive("episode_number").getAsString();
                            String vote_count = null;
                            if (!(episodes.get(z).getAsJsonObject().get("vote_count") instanceof JsonNull)) {
                                vote_count = episodes.get(z).getAsJsonObject().getAsJsonPrimitive("vote_count").getAsString();
                            } else {
                                vote_count = "N/A";
                            }
                            Episode ep = new Episode();
                            ep.setNumber(episode_number);
                            ep.setName(name);
                            ep.setOverview(overview);
                            ep.setRuntime(runtime);
                            ep.setStill_path(still_path);
                            ep.setVote_average(vote_average);
                            ep.setVote_count(vote_count);
                            System.out.println(ep);
                            episodesList.add(ep);


                        }

                        RecyclerView recyclerView2 = findViewById(R.id.episodeRecycler);
                        recyclerView2.post(()->{
                            EpisodeAdapter episodeAdapter = new EpisodeAdapter(SeasonDetailsActivity.this,episodesList);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SeasonDetailsActivity.this);
                            recyclerView2.setAdapter(episodeAdapter);
                            recyclerView2.setLayoutManager(linearLayoutManager);
                        });



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