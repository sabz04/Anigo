package com.example.anigo.Activities.AnimeActivityLogic;

import com.example.anigo.Models.Anime;
import com.example.anigo.Models.Screenshot;

public interface AnimeActivityContract {
    interface View {
        void OnSuccess(Anime anime);
        void OnError(String message);
        void OnSuccess(String message);
        void OnSuccess(Screenshot[] screenshots);
        void OnSuccessCheck(String msg_is_has);
        void OnErrorCheck(String msg_is_has);
        void OnSuccessDelete(String deleted_message);
        void OnErrorDelete(String undeleted_message);
    }
    interface Presenter{
        void GetAnime(int id);
    }
    interface PresenterAddToFavs{
        void FavsAdd(String comment, int anime_id);
    }
    interface PresenterCheckIfExist{
        void Check(int anime_id);
    }
    interface PresenterDeleteFromFav{
        void Delete(int anime_id);
    }
}
