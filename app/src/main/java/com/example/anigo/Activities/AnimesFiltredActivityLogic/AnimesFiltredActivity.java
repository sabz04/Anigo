package com.example.anigo.Activities.AnimesFiltredActivityLogic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.anigo.Models.Anime;
import com.example.anigo.R;

public class AnimesFiltredActivity extends AppCompatActivity implements AnimeFiltredContract.View {

    String filterName;
    Integer ANIME_STUDIO = 0, ANIME_TYPE = 1, ANIME_GENRE = 2;
    int currentPage =1;


    StudioPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animes_filtred);
        filterName = getIntent().getExtras().getString("filter");
        if(getIntent().getExtras().getInt("identificator") == ANIME_STUDIO){
            presenter = new StudioPresenter(this,this);
            presenter.GetAnimes(currentPage, filterName);
            currentPage++;
        }
    }

    @Override
    public void SuccessGetAnimesByStudio(Anime[] anime) {

    }

    @Override
    public void ErrorGetAnimesByStudio(String message) {

    }
}