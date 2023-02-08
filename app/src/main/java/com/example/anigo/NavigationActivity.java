package com.example.anigo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
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

    public static ArrayList<Anime> animes_pagination = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        /*if(savedInstanceState != null){
            search_fragment = (SearchFragment) getSupportFragmentManager().getFragment(savedInstanceState, "FRAGMENT_SEARCH");

            *//*Log.d("key from search fragment", savedInstanceState.getString("Key_string"));*//*
        }*/

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavHostFragment fragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController = fragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
      /* getSupportFragmentManager().putFragment(outState, "FRAGMENT_SEARCH", search_fragment);*/
    }
}