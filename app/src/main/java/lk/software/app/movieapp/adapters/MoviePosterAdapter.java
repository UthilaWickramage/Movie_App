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
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import lk.software.app.movieapp.DetailsActivity;
import lk.software.app.movieapp.MovieApi;
import lk.software.app.movieapp.R;
import lk.software.app.movieapp.model.Movie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Movie> movies;

    public MoviePosterAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.poster_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.i("poster", movies.get(position).getPoster_uri());
        String path = movies.get(position).getPoster_uri();
        Log.i("path", path);
        Picasso.get().load(path).centerCrop().resize(160, 240).into(holder.poster);
        holder.poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int movie_id = movies.get(position).getId();
                Log.i("id", String.valueOf(movie_id));
                new Thread(()->{
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://api.themoviedb.org/3/movie/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    MovieApi movieApi = retrofit.create(MovieApi.class);
                    String key = "4cdd13dbb5e2bcba4ac9ae8c5e53a297";
                    Call<JsonObject> objectCall = movieApi.getMovieDetails(movie_id, key);

                    objectCall.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.body() != null) {
                                JsonObject jsonObject = response.body();

                                JsonPrimitive title = jsonObject.getAsJsonPrimitive("title");
                                JsonPrimitive poster_path = jsonObject.getAsJsonPrimitive("poster_path");
                                JsonPrimitive backdrop_img = jsonObject.getAsJsonPrimitive("backdrop_path");
                                JsonPrimitive overview = jsonObject.getAsJsonPrimitive("overview");
                                JsonPrimitive vote_average = jsonObject.getAsJsonPrimitive("vote_average");
                                JsonPrimitive runtime = jsonObject.getAsJsonPrimitive("runtime");
                                String release_date = jsonObject.getAsJsonPrimitive("release_date").getAsString();
                                JsonPrimitive vote_count = jsonObject.getAsJsonPrimitive("vote_count");
                                JsonPrimitive item_id = jsonObject.getAsJsonPrimitive("id");
                                Movie movie = new Movie();
                                movie.setId(Integer.parseInt(item_id.toString()));
                                movie.setDuration(runtime.getAsString());
                                String[] split = release_date.split("-");
                                movie.setRelease(split[0]);
                                try {
                                    JsonArray production_countries = jsonObject.getAsJsonArray("production_countries");
                                    String country = production_countries.get(0).getAsJsonObject().get("name").getAsString();
                                    if(country!=null){
                                        if(country.equals("United States of America")){
                                            country = production_countries.get(0).getAsJsonObject().get("iso_3166_1").getAsString();
                                        }
                                        movie.setCountry(country);
                                    }
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

                                Log.i("movies", title.toString());
                                Log.i("movies", item_id.toString());
                                Log.i("movies", backdrop_concat);
                                Log.i("movies", concat);

                                Intent intent = new Intent(context, DetailsActivity.class);
                                intent.putExtra("movie", movie);
                                context.startActivity(intent);

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
        return movies.size();
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
