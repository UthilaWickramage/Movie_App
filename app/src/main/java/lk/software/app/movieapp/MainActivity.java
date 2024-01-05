package lk.software.app.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import lk.software.app.movieapp.fragments.CategoryFragment;
import lk.software.app.movieapp.fragments.DiscoverFragment;
import lk.software.app.movieapp.fragments.MovieFragment;
import lk.software.app.movieapp.fragments.SearchFragment;
import lk.software.app.movieapp.fragments.SliderFragment;
import lk.software.app.movieapp.fragments.TvFragment;

public class MainActivity extends AppCompatActivity implements SearchFragment.SearchFragmentListener, NavigationView.OnNavigationItemSelectedListener, NavigationBarView.OnItemSelectedListener{

BottomNavigationView bottomNavigationView;
boolean night_mode;
SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        SwitchMaterial switchMaterial = findViewById(R.id.switchMaterial);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        night_mode = sharedPreferences.getBoolean("night",false);

if(night_mode){
    switchMaterial.setChecked(true);
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
}

switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(night_mode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor = sharedPreferences.edit();
            editor.putBoolean("night",false);
            recreate();
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor = sharedPreferences.edit();
            editor.putBoolean("night",true);
            recreate();
        }

        editor.apply();
    }
});

bottomNavigationView.setOnItemSelectedListener(this);
        loadFragment(R.id.searchContainer, SearchFragment.newInstance());
        loadFragment(R.id.fragmentContainer, SliderFragment.getInstance(MainActivity.this));
loadFragment(R.id.categoryView, CategoryFragment.newInstance(getApplicationContext()));


    }
    FragmentManager supportFragmentManager = getSupportFragmentManager();
    private void loadFragment(int fragmentContainerView, Fragment
            fragment) {

        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();

        fragmentTransaction.replace(fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }

    private void removeFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }


    SliderFragment sliderFragment;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        System.out.println(itemId);
CategoryFragment categoryFragment = CategoryFragment.newInstance(getApplicationContext());
        MovieFragment movieFragment = MovieFragment.newInstance(MainActivity.this);
        SearchFragment searchFragment = SearchFragment.newInstance();
sliderFragment = SliderFragment.getInstance(MainActivity.this);
      TvFragment tvFragment = TvFragment.getInstance(getApplicationContext());
        if (itemId == R.id.bottomNavDiscover) {
            loadFragment(R.id.searchContainer, searchFragment);
            loadFragment(R.id.fragmentContainer, sliderFragment);
            loadFragment(R.id.categoryView,categoryFragment);
        } else if (itemId == R.id.bottomNavMovies) {
            removeFragment(sliderFragment);
            System.out.println("Working");
            loadFragment(R.id.categoryView,movieFragment);
//            if (currentUser != null) {
//                loadFragment(fragmentContainer, cartFragment);
//                removeFragment(searchFragment);
//
//            } else {
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
//                alertDialog.setTitle("Go to Login?");
//                alertDialog.setMessage("You need to login to create a cart");
//                alertDialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
//                    }
//                });
//                alertDialog.show();
//            }

        } else if (itemId == R.id.bottomNavTvSHows) {
            removeFragment(sliderFragment);
loadFragment(R.id.categoryView,tvFragment);
//            if (currentUser != null) {
//                Intent intent = new Intent(HomeActivity.this, AccountActivity.class);
//                startActivity(intent);
//
//            } else {
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
//                alertDialog.setTitle("Go to Login?");
//                alertDialog.setMessage("You need to login to view your account");
//                alertDialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
//                    }
//                });
//                alertDialog.show();
//            }


        } else if (itemId == R.id.bottomNavAccount) {
            removeFragment(sliderFragment);
            loadFragment(R.id.categoryView,tvFragment);
//            if (currentUser != null) {
//                Intent intent = new Intent(HomeActivity.this, AccountActivity.class);
//                startActivity(intent);
//
//            } else {
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
//                alertDialog.setTitle("Go to Login?");
//                alertDialog.setMessage("You need to login to view your account");
//                alertDialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
//                    }
//                });
//                alertDialog.show();
//            }


        } else {

        }
        return true;
    }

    @Override
    public void passSearchText(String searchText) {
        System.out.println(searchText);
removeFragment(SliderFragment.getInstance(MainActivity.this));
        loadFragment(R.id.categoryView,new DiscoverFragment(searchText));
    }
}