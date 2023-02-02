package com.example.anigo;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivityPresenter implements MainActivityContract.Presenter, OkHttpContract.Presenter{

    MainActivityContract.View view;

    OkHttpUserHelper presenter;

    String password = "";
    String login = "";

    public MainActivityPresenter(MainActivityContract.View view){
        this.view = view;
    }

    @Override
    public void  Login(String pass, String log, Context context) {

        presenter = new OkHttpUserHelper(this, context);

        presenter.SendPostLogin(log, pass);

    }

    @Override
    public void OnSuccess(String message) {
        view.onSuccess(message);
    }

    @Override
    public void OnSuccess(String message, Anime[] animes, int current_page, int pages_count) {

    }

    @Override
    public void OnError(String message) {
        view.onError(message);
    }
}
