package lk.software.app.movieapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import lk.software.app.movieapp.MovieApi;
import lk.software.app.movieapp.R;
import lk.software.app.movieapp.adapters.SliderAdapter;
import lk.software.app.movieapp.model.Slide;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SliderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SliderFragment extends Fragment {
    private ArrayList<Slide> slides;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private static FragmentActivity context;
private static Context context1;
    private SliderAdapter sliderAdapter;

    public SliderFragment() {
        // Required empty public constructor
    }

    public static SliderFragment newInstance() {

        SliderFragment fragment = new SliderFragment();

        return fragment;
    }
public static SliderFragment sliderFragment;
    public static SliderFragment getInstance(Context context) {
        if(sliderFragment==null){
            sliderFragment = new SliderFragment();
        }
        SliderFragment.context1 = context;
        return sliderFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewPager = view.findViewById(R.id.slider_pager);
        tabLayout = view.findViewById(R.id.indicator);
        context = getActivity();
        slides = new ArrayList<>();
       new Thread(()->{
           Retrofit retrofit = new Retrofit.Builder()
                   .baseUrl("https://api.themoviedb.org/3/movie/")
                   .addConverterFactory(GsonConverterFactory.create())
                   .build();
           MovieApi movieApi = retrofit.create(MovieApi.class);
           String key = "4cdd13dbb5e2bcba4ac9ae8c5e53a297";
           Call<JsonObject> objectCall = movieApi.getUpcomingMovies(key);

           objectCall.enqueue(new Callback<JsonObject>() {
               @Override
               public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                   if (response.body() != null) {
                       JsonObject jsonObject = response.body();
                       JsonArray results = jsonObject.getAsJsonArray("results");
                       for (int i = 0; i < 5; i++) {
                           JsonObject movieObject = results.get(i).getAsJsonObject();
                           JsonPrimitive title = movieObject.getAsJsonObject().getAsJsonPrimitive("title");
                           JsonPrimitive desc = movieObject.getAsJsonObject().getAsJsonPrimitive("overview");
                           JsonPrimitive backdrop_img = movieObject.getAsJsonObject().getAsJsonPrimitive("backdrop_path");

                           //String poster = "https://api.themoviedb.org/3/network/network_id/images"+backdrop_img;
                           Log.i("getAsString", title.toString());
                           String baseUrl = "https://image.tmdb.org/t/p/w500";
                           String concat = baseUrl.concat(backdrop_img.getAsString());
                           Slide slide = new Slide(concat, title.getAsString());
                           int reduce = 100;
                           String substring;
                           if(reduce<desc.toString().length()){
                               substring = desc.getAsString().substring(0, reduce);
                           }else {
                               substring =  desc.toString();
                           }

                           slide.setDesc(substring);
                           slides.add(slide);
                       }
                       System.out.println(slides.size());

                       sliderAdapter = new SliderAdapter(context1, slides);
                       viewPager.setAdapter(sliderAdapter);
                       Timer timer = new Timer();
                       timer.scheduleAtFixedRate(new SliderTimer(),4000,6000);
                       tabLayout.setupWithViewPager(viewPager,true);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_slider, container, false);
    }

    class SliderTimer extends TimerTask {

        @Override
        public void run() {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < slides.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }

                }
            });
        }
    }
}