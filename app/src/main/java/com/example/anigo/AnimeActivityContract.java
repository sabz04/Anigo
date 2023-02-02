package com.example.anigo;

public interface AnimeActivityContract {
    interface View {
        void OnSuccess(Anime anime);
        void OnError(String message);
    }
    interface Presenter{
        void GetAnime(int id);
    }
}
