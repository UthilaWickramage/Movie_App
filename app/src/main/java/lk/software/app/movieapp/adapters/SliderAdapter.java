package lk.software.app.movieapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import lk.software.app.movieapp.R;
import lk.software.app.movieapp.model.Slide;

public class SliderAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<Slide> sliders;

    public SliderAdapter(Context context, ArrayList<Slide> sliders) {
        this.context = context;
        this.sliders = sliders;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.slide_item,null);
        ImageView slide_img = inflate.findViewById(R.id.slide_img);
        TextView slide_name = inflate.findViewById(R.id.slide_title);
TextView slide_desc = inflate.findViewById(R.id.desc);
        Picasso.get().load(sliders.get(position).getImage()).resize(0,275).into(slide_img);
        String title = sliders.get(position).getTitle();
        String concatTitle = title.replace('"','"');

        slide_name.setText(concatTitle);
        String desc = sliders.get(position).getDesc();
        String concatDesc = desc.replace('"', '"');

        slide_desc.setText(concatDesc);
        container.addView(inflate);
        return inflate;
    }

    @Override
    public int getCount() {
        return sliders.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view ==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
