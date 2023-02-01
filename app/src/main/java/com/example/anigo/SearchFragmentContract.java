package com.example.anigo;

public interface SearchFragmentContract {
    interface View {
        void onSuccess(String message, Anime[] animes, int currentpage, int pagecount);
        void onSuccess(String message);
        void onError(String message, String body);
        void onError(String message);
    }
    interface Presenter{
        void Search(String search);
        void Search(String search, int page);
    }
}
