package lk.software.app.movieapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.MaskTransformation;
import lk.software.app.movieapp.ActorDetailsActvity;
import lk.software.app.movieapp.R;
import lk.software.app.movieapp.model.Actor;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder> {

    private Context context;
private ArrayList<Actor> actors;

    public CastAdapter(Context context, ArrayList<Actor> actors) {
        this.context = context;
        this.actors = actors;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.actor_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Actor actor = actors.get(position);
holder.name.setText(actor.getName());
holder.character.setText(actor.getCharacter());

    Picasso.get().load(actor.getProfile_path()).centerCrop()
            .resize(120, 120).into(holder.imageView);
holder.imageView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, ActorDetailsActvity.class);
        intent.putExtra("id",actor.getId());
        context.startActivity(intent);
    }
});
    }

    @Override
    public int getItemCount() {
        return actors.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
ImageView imageView;
TextView name,character;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           imageView =  itemView.findViewById(R.id.imageView4);
           name = itemView.findViewById(R.id.textView7);
           character = itemView.findViewById(R.id.textView9);
        }
    }
}
