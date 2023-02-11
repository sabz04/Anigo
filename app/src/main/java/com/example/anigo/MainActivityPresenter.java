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


    OkHttpClient client;
    Context context;
    FeedUserDbHelper db_helper;

    String password = "";
    String login = "";

    public MainActivityPresenter(MainActivityContract.View view, Context context){
        this.view = view;
        this.context = context;
        this.client = new OkHttpClient();
        this.db_helper = new FeedUserDbHelper(context);
    }

    @Override
    public void  Login(String log, String pass, Context context) {
        this.login = log;
        this.password = pass;

        UserLoginAuthClass auth_user = new UserLoginAuthClass(login, password);
        //converting to json
        String json = new Gson().toJson(auth_user);

        RequestBody formBody = RequestBody.create(
                MediaType.parse("application/json"), json);
        Request request = new Request.Builder()

                .url(RequestOptions.request_url_login)
                .post(formBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                view.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json_body = response.body().string();
                if(response.code() == 200){
                    FeedUserLocal userLocal = db_helper.CheckIfExist();
                    if(userLocal == null){
                        db_helper.Create(login, password, json_body);
                        Log.d("LOCAL_DATABASE", "hehe, yes! he is actually added to inner database");
                    }
                    else {
                        Log.d("LOCAL_DATABASE ", "USER is already here");
                    }
                    view.onSuccess(json_body);

                }
                else {
                    view.onError(json_body);
                }

            }
        });

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

    @Override
    public void OnSuccess(Anime anime) {

    }
}
