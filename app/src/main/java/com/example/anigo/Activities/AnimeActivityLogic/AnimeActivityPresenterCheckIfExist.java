package com.example.anigo.Activities.AnimeActivityLogic;

import android.content.Context;

import com.example.anigo.AuthentificationLogic.Authentification;
import com.example.anigo.AuthentificationLogic.AuthentificationInterface;
import com.example.anigo.RequestsHelper.RequestOptions;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AnimeActivityPresenterCheckIfExist implements AnimeActivityContract.PresenterCheckIfExist, AuthentificationInterface.Listener {

    AnimeActivityContract.View view;

    Authentification authentification;

    OkHttpClient client;

    Gson gson;

    int id =-1;

    Context context;


    public AnimeActivityPresenterCheckIfExist(AnimeActivityContract.View view, Context context) {

        this.view = view;
        this.client = new OkHttpClient();
        this.gson = new Gson();
        this.context = context;
    }

    @Override
    public void Check(int anime_id) {

        authentification = new Authentification(this, this.context);

        this.id = anime_id;

        authentification.Auth();
    }

    @Override
    public void AuthSuccess(String token) {

    }

    @Override
    public void AuthError(String message) {

    }

    @Override
    public void AuthSuccess(String token, int user_id) {
        String end = String.format(RequestOptions.request_url_check_in_fav, id, user_id);
        Request request = new Request.Builder()

                .url(String.format(RequestOptions.request_url_check_in_fav, id, user_id))
                .get()
                .addHeader("Authorization", "Bearer " + token )
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                view.OnError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json_body = response.body().string();
                if(response.code() == 201 || response.code() == 200 || response.code() == 204){

                    view.OnSuccessCheck(json_body);

                }
                else {
                    view.OnErrorCheck(json_body);
                }

            }
        });
    }
}
