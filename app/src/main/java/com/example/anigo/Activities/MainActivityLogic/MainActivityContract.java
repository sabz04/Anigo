package com.example.anigo.Activities.MainActivityLogic;

import android.content.Context;

public interface MainActivityContract {
    interface View {
        void onSuccess(String message);
        void onError(String message, String body);
        void onError(String message);
    }
    interface Presenter{
        void Login(String password, String login, Context context);
    }
}
