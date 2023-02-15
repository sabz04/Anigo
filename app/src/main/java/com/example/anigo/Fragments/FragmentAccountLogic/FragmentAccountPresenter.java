package com.example.anigo.Fragments.FragmentAccountLogic;

import android.content.Context;

import com.example.anigo.AuthentificationLogic.Authentification;
import com.example.anigo.AuthentificationLogic.AuthentificationInterface;
import com.example.anigo.InnerDatabaseLogic.FeedUserDbHelper;
import com.example.anigo.InnerDatabaseLogic.FeedUserLocal;
import com.example.anigo.Models.User;
import com.example.anigo.RequestsHelper.RequestOptions;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentAccountPresenter implements  FragmentAccountContract.Presenter, AuthentificationInterface.Listener{

    FragmentAccountContract.View view;

    Authentification auth;
    Context context;

    OkHttpClient client;

    FeedUserDbHelper db_helper;

    Gson gson;
    public FragmentAccountPresenter(FragmentAccountContract.View view, Context context) {
        this.view = view;
        this.context = context;
        this.db_helper = new FeedUserDbHelper(context);
        this.gson = new Gson();
        client = new OkHttpClient();
    }

    @Override
    public void GetUser() {
        auth = new Authentification(this, context);

        auth.Auth();

    }

    @Override
    public void Exit(Context context) {

        FeedUserDbHelper db = new FeedUserDbHelper(context);

        FeedUserLocal user_local = db.CheckIfExist();

        if(user_local == null){
            view.onError("Пользователя не было в БД.");
            return;
        }
        else {
            db.Delete();
            view.onSuccess("Пользователь удален.");
        }

    }

    @Override
    public void AuthSuccess(String token) {

        FeedUserLocal user = db_helper.CheckIfExist();

        Request request = new Request.Builder()

                .url(String.format(RequestOptions.request_url_user_get, user.Login, user.Password))
                .get()
                .addHeader("Authorization", "Bearer " + token )
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
                if(response.code() == 201 || response.code() == 200 || response.code() == 204){


                    User user_current = gson.fromJson(json_body, User.class);
                    System.out.printf("2");
                    view.onSuccess(user_current);

                }
                else {
                    view.onError("error " + json_body);
                }

            }
        });
    }

    @Override
    public void AuthError(String message) {
       /* FeedUserDbHelper db = new FeedUserDbHelper(context);

        FeedUserLocal user_local = db.CheckIfExist();

        if(user_local == null){
            view.onError("Пользователя не было в БД.");
            return;
        }
        else {
            db.Delete();
            view.onSuccess("Пользователь удален.");
        }*/
    }

    @Override
    public void AuthSuccess(String token, int user_id) {

    }
}
