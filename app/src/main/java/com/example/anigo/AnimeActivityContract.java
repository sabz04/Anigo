package com.example.anigo;

public interface AnimeActivityContract {
    interface View {
        void OnSuccess(Anime anime);
        void OnError(String message);
        void OnSuccess(String message);
        void OnSuccess(Screenshot[] screenshots);
        void OnSuccessCheck(String msg_is_has);
        void OnErrorCheck(String msg_is_has);
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
}
