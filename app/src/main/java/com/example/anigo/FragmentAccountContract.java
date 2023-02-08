package com.example.anigo;

import android.content.Context;

public interface FragmentAccountContract {
    interface View {
        void onSuccess(String message);
        void onSuccess(User user);
        void onError(String message, String body);
        void onError(String message);
    }
    interface Presenter{
        void GetUser();
        void Exit(Context context);
    }
}
