package com.example.anigo;

import okhttp3.RequestBody;

public interface OkHttpContract {
    interface Presenter{
        void OnSuccess(String message);
        void OnSuccess(String message, Anime[] animes, int current_page, int pages_count);
        void OnError(String message);
        void OnSuccess(Anime anime);
    }
    interface OkHttpRequest{
        void SendPostLogin(String login, String password);
        void SendPostRegister(String login, String password, String email, int RoleId);
        void SendGetAnimes(String search, int page);
        void SendGetAnime(int id);
    }
}
