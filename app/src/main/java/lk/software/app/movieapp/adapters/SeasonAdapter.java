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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import lk.software.app.movieapp.DetailsActivity;
import lk.software.app.movieapp.MovieApi;
import lk.software.app.movieapp.R;
import lk.software.app.movieapp.SeasonDetailsActivity;
import lk.software.app.movieapp.model.Actor;
import lk.software.app.movieapp.model.Episode;
import lk.software.app.movieapp.model.Season;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SeasonAdapter extends RecyclerView.Adapter<SeasonAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Season> seasons;
    private String series_id;

    public SeasonAdapter(Context context, ArrayList<Season> seasons, String id) {
        this.context = context;
        this.seasons = seasons;
        this.series_id = id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.season_poster_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Season season = seasons.get(position);
        holder.name.setText(season.getSeason_number());
        holder.character.setText(season.getNumber_of_episodes() + " Episodes");
        Log.i("season.getPoster()", season.getPoster());
        if (season.getPoster() != null) {
            Picasso.get().load(season.getPoster()).centerCrop()
                    .resize(200, 300).into(holder.imageView);
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, SeasonDetailsActivity.class);
                intent.putExtra("series_id", series_id);
                intent.putExtra("season", season);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return seasons.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, character;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.textView13);
            character = itemView.findViewById(R.id.textView17);
        }
    }
}
