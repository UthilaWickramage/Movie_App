package lk.software.app.movieapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import lk.software.app.movieapp.R;
import lk.software.app.movieapp.model.Episode;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.ViewHolder> {
Context context;
ArrayList<Episode> episodes;
    public EpisodeAdapter(Context context, ArrayList<Episode> episodes){
        this.context = context;
        this.episodes = episodes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.episode_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
Episode episode = episodes.get(position);
holder.votes.setText(episode.getVote_count());
holder.rating.setText("Rating : "+episode.getVote_average());
holder.ep.setText("Ep "+episode.getNumber());
holder.runtime.setText(episode.getRuntime()+" min");
holder.overview.setText(episode.getOverview());
holder.name.setText(episode.getName());
        Picasso.get().load(episode.getStill_path()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, eye;
        TextView ep, name, overview, runtime, rating, votes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView10);
            eye = itemView.findViewById(R.id.imageView11);
            name = itemView.findViewById(R.id.textView23);
            ep = itemView.findViewById(R.id.textView22);
            overview = itemView.findViewById(R.id.textView24);
            runtime = itemView.findViewById(R.id.textView25);
            votes = itemView.findViewById(R.id.textView29);
            rating = itemView.findViewById(R.id.textView28);

        }
    }
}
