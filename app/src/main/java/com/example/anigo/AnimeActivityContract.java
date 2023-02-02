package com.example.anigo;

public interface AnimeActivityContract {
    interface View {
        void OnSuccess();
        void OnError();
    }
    interface Presenter{
        void GetAnime(int id);
    }
}
