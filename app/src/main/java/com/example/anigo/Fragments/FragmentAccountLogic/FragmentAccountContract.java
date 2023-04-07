package com.example.anigo.Fragments.FragmentAccountLogic;

import android.content.Context;
import android.net.Uri;

import com.example.anigo.Models.User;

import java.io.File;

public interface FragmentAccountContract {
    interface View {
        void onSuccess(String message);
        void onSuccess(User user);
        void onError(String message, String body);
        void onError(String message);
        void OnSuccessChangePhoto(String message);
        void OnErrorChangePhoto(String message);
    }
    interface Presenter{
        void GetUser();
        void Exit(Context context);
    }
    interface PresenterChangePhoto{
        void ChangePhoto(Uri file);
    }
}
