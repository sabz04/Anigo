package com.example.anigo;

public interface HomeFragmentContract {
    interface View{
        void OnSuccess(String message);
        void OnSuccess(Anime[] animes, int current_page, int page_count);
        void OnError(String error);
    }
    interface Presenter{
        void GetFavs(int page);
    }
}
