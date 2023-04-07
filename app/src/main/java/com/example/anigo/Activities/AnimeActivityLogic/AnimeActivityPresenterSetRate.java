package com.example.anigo.Activities.AnimeActivityLogic;

import android.content.Context;

import com.example.anigo.AuthentificationLogic.Authentification;
import com.example.anigo.AuthentificationLogic.AuthentificationInterface;
import com.example.anigo.Models.AnimeCommentResponse;
import com.example.anigo.Models.RateResponse;
import com.example.anigo.RequestsHelper.RequestOptions;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AnimeActivityPresenterSetRate implements AuthentificationInterface.Listener, AnimeActivityContract.PresenterSetRating {


    AnimeActivityContract.View view;

    Authentification authentification;

    OkHttpClient client;

    int animeId =-1, rateValue;

    Context context;

    public AnimeActivityPresenterSetRate(AnimeActivityContract.View view, Context context) {
        this.view = view;
        this.context = context;
        client=new OkHttpClient();
        authentification = new Authentification(this, context);
    }

    @Override
    public void AuthSuccess(String message) {

    }

    @Override
    public void AuthError(String message) {

    }

    @Override
    public void AuthSuccess(String token, int user_id) {
        Request request = new Request.Builder()

                .url(String.format(RequestOptions.request_url_set_rating, animeId,user_id,rateValue))
                .get()
                .addHeader("Authorization", "Bearer " + token )
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                view.OnErrorSetRate(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json_body = response.body().string();
                if(response.code() == 201 || response.code() == 200 || response.code() == 204){
                    RateResponse rateResponse = new Gson().fromJson(json_body, RateResponse.class);
                    view.OnSuccessSetRate(rateResponse);
                }
                else {
                    view.OnErrorSetRate(json_body);
                }

            }
        });
    }

    @Override
    public void SetRating(int animeId, int rateValue) {
        this.animeId = animeId;
        this.rateValue = rateValue;
        authentification.Auth();
    }
}
