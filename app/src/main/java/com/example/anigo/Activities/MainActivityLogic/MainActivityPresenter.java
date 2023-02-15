package com.example.anigo.Activities.MainActivityLogic;


import android.content.Context;
import android.util.Log;

import com.example.anigo.InnerDatabaseLogic.FeedUserDbHelper;
import com.example.anigo.InnerDatabaseLogic.FeedUserLocal;
import com.example.anigo.RequestsHelper.RequestOptions;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivityPresenter implements MainActivityContract.Presenter{

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
                    FeedUserLocal user_Local = db_helper.CheckIfExist();
                    view.onSuccess(json_body);

                }
                else {
                    view.onError(json_body);
                }

            }
        });

    }

}
