package com.example.anigo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class NavigationActivity extends AppCompatActivity {

    public static View search_fragment_instance;

    public static View home_fragment_instance;

    public static View fav_fragment_instance;

    private BottomNavigationView bottomNavigationView;
    private NavHostFragment fragment;
    private NavController navController;

    private String  destination_str = "";

    public static ArrayList<Anime> animes_pagination = new ArrayList<>();

    public static ArrayList<Favourite> favourites_pagination = new ArrayList<>();

    public static ArrayList<Anime> animes_pagination_popular = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController = fragment.getNavController();

        animes_pagination.clear();
        animes_pagination_popular.clear();
        favourites_pagination.clear();

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onWindowFocusChanged (boolean hasFocus){
        destination_str = "";
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                destination_str = destination.getLabel().toString();
                Log.d("navcontroller", "onDestinationChanged: "+destination.getLabel());
            }
        });
        if(hasFocus && destination_str.contains("fragment_liked")){
            navController.navigate(R.id.fragmentLiked);
        }
        if(hasFocus && destination_str.contains("fragment_search")){
            navController.navigate(R.id.searchFragment);
        }

    }
}