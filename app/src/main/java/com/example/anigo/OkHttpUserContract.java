package com.example.anigo;

import okhttp3.OkHttpClient;

public interface OkHttpUserContract {
    interface OkHttpUser{
        String GetToken();
        String GetToken(String login, String password);
        String GetToken(FeedUserLocal user);
        FeedUserLocal GetUser(String login, String password);
    }
    interface Presenter{
        void OnSuccess(String message);
        void OnError(String message);
    }

}
