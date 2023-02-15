package com.example.anigo.Activities.AnimeActivityLogic;

import android.content.Context;

import com.example.anigo.AuthentificationLogic.Authentification;
import com.example.anigo.AuthentificationLogic.AuthentificationInterface;
import com.example.anigo.Models.FavouriteAddClass;
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

public class AnimeActivityPresenterAddToFavs implements  AnimeActivityContract.PresenterAddToFavs, AuthentificationInterface.Listener {

    AnimeActivityContract.View view;
    Authentification authentification;

    OkHttpClient client;

    Gson gson;

    Context context;

    String comment ="";

    int anime_id = -1;

    public AnimeActivityPresenterAddToFavs(AnimeActivityContract.View view, Context context) {
        this.view = view;
        this.context = context;
        this.gson = new Gson();
        this.client = new OkHttpClient();
    }

    @Override
    public void FavsAdd(String comment, int anime_id) {
        this.anime_id = anime_id;
        this.comment = comment;

        authentification = new Authentification(this, this.context);

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

        FavouriteAddClass fav = new FavouriteAddClass(user_id,this.anime_id,this.comment);
        //converting to json
        String json = new Gson().toJson(fav);

        RequestBody formBody = RequestBody.create(
                MediaType.parse("application/json"), json);
        Request request_screens = new Request.Builder()

                .url(RequestOptions.request_url_add_to_favs)
                .post(formBody)
                .addHeader("Authorization", "Bearer " + token )
                .build();
        Call call_screens = client.newCall(request_screens);

        call_screens.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                view.OnError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json_body = response.body().string();
                if(response.code() == 201 || response.code() == 200 || response.code() == 204){

                    view.OnSuccess(json_body);

                }
                else {
                    view.OnError("error " + json_body);
                }

            }
        });
    }
}
