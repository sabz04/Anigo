package com.example.anigo.Activities.AnimesFiltredActivityLogic;

import com.example.anigo.Models.Anime;
import com.example.anigo.Models.Studio;

public interface AnimeFiltredContract {
    interface View{
        void SuccessGetAnimesByStudio(Anime[] anime);
        void ErrorGetAnimesByStudio(String message);
    }
    interface PresenterStudio{
        void GetAnimes( int page,String studioName);
    }
}
