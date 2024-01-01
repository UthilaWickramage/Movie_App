package lk.software.app.movieapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import lk.software.app.movieapp.DetailsActivity;
import lk.software.app.movieapp.MovieApi;
import lk.software.app.movieapp.R;
import lk.software.app.movieapp.model.Episode;
import lk.software.app.movieapp.model.Movie;
import lk.software.app.movieapp.model.Season;
import lk.software.app.movieapp.model.TVShow;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TVPosterAdapter extends RecyclerView.Adapter<TVPosterAdapter.ViewHolder> {
    private Context context;
    private ArrayList<TVShow> tvshows;

    public TVPosterAdapter(Context context, ArrayList<TVShow> tvshows) {
        this.context = context;
        this.tvshows = tvshows;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.poster_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.i("poster", tvshows.get(position).getPoster_uri());
        String path = tvshows.get(position).getPoster_uri();
        Log.i("path", path);
        Picasso.get().load(path).centerCrop().resize(160, 240).into(holder.poster);
        holder.poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int movie_id = tvshows.get(position).getId();
                Log.i("tv poster id", String.valueOf(movie_id));
                new Thread(() -> {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://api.themoviedb.org/3/tv/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    MovieApi movieApi = retrofit.create(MovieApi.class);
                    String key = "4cdd13dbb5e2bcba4ac9ae8c5e53a297";
                    Call<JsonObject> objectCall = movieApi.getTvDetails(tvshows.get(position).getId(), key);

                    objectCall.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.body() != null) {
                                JsonObject jsonObject = response.body();
                                TVShow movie = new TVShow();
                                JsonPrimitive title = jsonObject.getAsJsonPrimitive("name");
                                JsonPrimitive poster_path = jsonObject.getAsJsonPrimitive("poster_path");
                                JsonPrimitive backdrop_img = jsonObject.getAsJsonPrimitive("backdrop_path");
                                JsonPrimitive overview = jsonObject.getAsJsonPrimitive("overview");
                                JsonPrimitive vote_average = jsonObject.getAsJsonPrimitive("vote_average");
                                JsonPrimitive episodes_number = jsonObject.getAsJsonPrimitive("number_of_episodes");
                                try {
                                    JsonPrimitive runtime = jsonObject.getAsJsonArray("episode_run_time").get(0).getAsJsonPrimitive();
                                    movie.setDuration(runtime.getAsString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    movie.setDuration("N/A");
                                }
                                String release_date = jsonObject.getAsJsonPrimitive("first_air_date").getAsString();
                                JsonPrimitive vote_count = jsonObject.getAsJsonPrimitive("vote_count");
                                JsonPrimitive item_id = jsonObject.getAsJsonPrimitive("id");

                                movie.setId(Integer.parseInt(item_id.toString()));

                                String[] split = release_date.split("-");
                                movie.setRelease(split[0]);

                                try {

                                    JsonArray spoken_languages = jsonObject.getAsJsonArray("spoken_languages");
                                    String lang = spoken_languages.get(0).getAsJsonObject().get("english_name").getAsString();
                                    movie.setLang(lang);
                                } catch (ArrayIndexOutOfBoundsException exception) {
                                    Log.e("ArrayIndexOutOfBoundsException", exception.getMessage());
                                }
                                JsonArray genres = jsonObject.getAsJsonArray("genres");
                                ArrayList<String> genreNames = new ArrayList<>();
                                if (3 <= genres.size()) {
                                    for (int x = 0; x < 3; x++) {
                                        JsonObject genre = genres.get(x).getAsJsonObject();
                                        String name = genre.getAsJsonPrimitive("name").getAsString();
                                        genreNames.add(name);
                                    }
                                } else {
                                    for (int x = 0; x < genres.size(); x++) {
                                        JsonObject genre = genres.get(x).getAsJsonObject();
                                        String name = genre.getAsJsonPrimitive("name").getAsString();
                                        genreNames.add(name);
                                    }
                                }

                                movie.setGenres(genreNames);

                                JsonArray seasonsArray = jsonObject.getAsJsonArray("seasons");
                                ArrayList<Season> seasons = new ArrayList<>();

                                for (int x = 0; x < seasonsArray.size(); x++) {
                                    JsonObject season = seasonsArray.get(x).getAsJsonObject();
                                    Season season1 = new Season();
                                    season1.setId(season.getAsJsonPrimitive("id").getAsString());
                                    String name = season.getAsJsonPrimitive("name").getAsString();
                                    String season_number = season.getAsJsonPrimitive("season_number").getAsString();
//                                    ArrayList<Episode> episodes = new ArrayList<>();
//                                    Log.i("season details",season.getAsJsonArray("episodes").getAsString());
//                                    JsonArray episodesArray = season.getAsJsonArray("episodes");
//                                    for (int y = 0; y < episodesArray.size(); y++) {
//                                        JsonObject episode = episodesArray.get(y).getAsJsonObject();
//                                        Episode ep = new Episode();
//Log.i("season Details",episode.getAsString());
//                                    }
                                    season1.setSeason_number(name);
                                    season1.setId(season_number);
                                    String episode_count = season.getAsJsonPrimitive("episode_count").getAsString();
                                    season1.setNumber_of_episodes(episode_count);

                                    String profile = "https://image.tmdb.org/t/p/w500";

                                    if (!(season.getAsJsonObject().get("poster_path") instanceof JsonNull)) {
                                        profile = profile.concat(season.getAsJsonObject().getAsJsonPrimitive("poster_path").getAsString());
                                    }
                                    Log.i("poster", profile);
                                    season1.setPoster(profile);
                                    seasons.add(season1);
                                }


                                movie.setSeasons(seasons);

                                DecimalFormat decimalFormat = new DecimalFormat("0.0");
                                Log.i("vote", vote_average.getAsString());
                                String avg = decimalFormat.format(Double.parseDouble(vote_average.getAsString()));
                                movie.setRating(avg);
                                movie.setVotes(vote_count.getAsString());
                                movie.setTitle(title.getAsString());
                                String baseUrl = "https://image.tmdb.org/t/p/w500";
                                String concat = baseUrl.concat(poster_path.getAsString());
                                movie.setPoster_uri(concat);
                                String backdrop_concat = baseUrl.concat(backdrop_img.getAsString());
                                movie.setBackdrop_url(backdrop_concat);
                                movie.setOverview(overview.getAsString());
                                movie.setNumber_of_episodes(episodes_number.getAsString());
                                Log.i("episodes", movie.getNumber_of_episodes());
                                Log.i("movies", title.toString());
                                Log.i("movies", item_id.toString());
                                Log.i("movies", backdrop_concat);
                                Log.i("movies", concat);

                                Intent intent = new Intent(context, DetailsActivity.class);
                                intent.putExtra("movie", movie);
                                context.startActivity(intent);

                                Log.i("movies", response.body().toString());

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
        });
    }

    @Override
    public int getItemCount() {
        return tvshows.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView poster;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            poster = itemView.findViewById(R.id.imageView);
        }
    }
}
